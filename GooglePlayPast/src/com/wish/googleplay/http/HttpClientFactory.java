package com.wish.googleplay.http;

import org.apache.http.*;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.util.zip.GZIPInputStream;

public class HttpClientFactory {
	/** http������󲢷������� */
	private static final int MAX_CONNECTIONS = 10;
	/** ��ʱʱ�� */
	private static final int TIMEOUT = 10 * 1000;
	/** �����С */
	private static final int SOCKET_BUFFER_SIZE = 8 * 1024; // 8KB
	/** �����Դ����������쳣������RetryHandler��� */
	private static final int MAX_RETRIES = 5;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	public static DefaultHttpClient create(boolean isHttps) {
		HttpParams params = createHttpParams();
		DefaultHttpClient httpClient = null;
		if (isHttps) {
			// ֧��http��https
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			// ThreadSafeClientConnManager�̰߳�ȫ������
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
			httpClient = new DefaultHttpClient(cm, params);
		} else {
			httpClient = new DefaultHttpClient(params);
		}
		return httpClient;
	}

	private static HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		// �����Ƿ����þ����Ӽ�飬Ĭ���ǿ����ġ��ر���������Ӽ��������һ������ܣ�����������I/O����ķ��գ�������˹ر�����ʱ����
		// �������ѡ������ÿ��ʹ���ϵ�����֮ǰ�����������Ƿ���ã������ʱ�����15-30ms֮��
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);// �������ӳ�ʱʱ��
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);// ����socket��ʱʱ��
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);// ���û����С
		HttpConnectionParams.setTcpNoDelay(params, true);// �Ƿ�ʹ���ӳٷ���(trueΪ���ӳ�)
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); // ����Э��汾
		HttpProtocolParams.setUseExpectContinue(params, true);// �����쳣�������
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);// ���ñ���
		HttpClientParams.setRedirecting(params, false);// �����Ƿ�����ض���

		ConnManagerParams.setTimeout(params, TIMEOUT);// ���ó�ʱ
		ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(MAX_CONNECTIONS));// ���߳����������
		ConnManagerParams.setMaxTotalConnections(params, 10); // ���߳���������
		return params;
	}

	private static void createHttpClient(DefaultHttpClient httpClient) {
		// ���request������������ӱ�Ҫ��ͷ��Ϣ
		httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(HttpRequest request, HttpContext context) {
				if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
					request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
				}
			}
		});

		// ���response��������Ԥ�ȶ�response����һЩ����
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(HttpResponse response, HttpContext context) {
				final HttpEntity entity = response.getEntity();
				if (entity == null) {
					return;
				}
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						// �������GZIPѹ�������ݣ������ڲ����������װһ��Gzip����
						if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
							response.setEntity(new InflatingEntity(response.getEntity()));
							break;
						}
					}
				}
			}
		});
		// �������Դ���
		httpClient.setHttpRequestRetryHandler(new HttpRetry(MAX_RETRIES));
	}

	/** �����������ص���������Gzipѹ���Ĺ�������ݣ����Response���ص�ʵ������ (Description)���򷵻�GZIP��ѹ�� */
	private static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		// ��Ϊ������ѹ�����ݣ�����ʵ�ʳ����޷����ƣ����Է���-1
		@Override
		public long getContentLength() {
			return -1;
		}
	}

	/** �Զ���İ�ȫ�׽���Э���ʵ�֣�Ŀǰ����Ĭ�ϵģ�δʹ�õ� */
	private static class SSLSocketFactoryEx extends SSLSocketFactory {
		// �����ʵ����ʾ��ȫ�׽���Э���ʵ�֣����䵱���ڰ�ȫ�׽��ֹ����� SSLEngine �Ĺ������ÿ�ѡ��һ����Կ�����ι���������ȫ����ֽ�Դ��ʼ�����ࡣ
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			// TrustManager��������������ξ���ʱʹ�õĵ����β��ϣ�Ҳ��������Ƿ����ͬλ���ṩ��ƾ�ݡ�
			// X509TrustManager�˽ӿڵ�ʵ������ʹ����һ�� X509 ֤������֤Զ�˵İ�ȫ�׽��֡������Ǹ������ε�֤����Ȩ��֤�鳷���б�����״̬����������ʽ�����ġ�
			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;// ��������֤ͬλ�����ε���֤���ĵ����顣
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					// ����ͬλ���ṩ�Ĳ��ֻ�������֤�����������������εĸ���֤��·�������ҷ����Ƿ����ȷ�Ϻ����ν������ڻ�����֤���͵Ŀͻ��� SSL ��֤��
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					// ����ͬλ���ṩ�Ĳ��ֻ�������֤�����������������εĸ���֤��·�������ҷ����Ƿ����ȷ�Ϻ����ν������ڻ�����֤���͵ķ����� SSL ��֤��
				}
			};
			sslContext.init(null, new TrustManager[]{tm}, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
