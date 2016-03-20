package com.wish.googleplay.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import android.os.SystemClock;

public class HttpRetry implements HttpRequestRetryHandler {
	// ������Ϣ��ʱ��
	private static final int RETRY_SLEEP_TIME_MILLIS = 1000;
	// �����쳣������
	private static HashSet<Class<?>> exceptionWhitelist = new HashSet<Class<?>>();
	// �û��쳣�����������磬�û��ж��̣߳�
	private static HashSet<Class<?>> exceptionBlacklist = new HashSet<Class<?>>();

	static {
		// �����쳣����Ҫ���ԣ������쳣����������ɻ�����һЩ����Ҳ��Ч���쳣
		exceptionWhitelist.add(NoHttpResponseException.class);// �����˷���������û��Response
		exceptionWhitelist.add(UnknownHostException.class);// host�������⣬һ���������������
		exceptionWhitelist.add(SocketException.class);// Socket���⣬һ���������������
		// �����쳣��������
		exceptionBlacklist.add(InterruptedIOException.class);// �����жϣ�һ�����������ӳ�ʱ����
		exceptionBlacklist.add(SSLHandshakeException.class);// SSL����ʧ��
	}

	private final int maxRetries;

	public HttpRetry(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		boolean retry = true;
		// �����Ƿ񵽴�
		Boolean b = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (b != null && b.booleanValue());

		if (executionCount > maxRetries) {
			// ���Դ��������û�����Ĳ���
			retry = false;
		} else if (exceptionBlacklist.contains(exception.getClass())) {
			// �̱߳��û��жϣ��򲻼�������
			retry = false;
		} else if (exceptionWhitelist.contains(exception.getClass())) {
			// ���ֵ��쳣��Ҫ������
			retry = true;
		} else if (!sent) {
			// ����û�е���
			retry = true;
		}
		// �����Ҫ����
		if (retry) {
			// ��ȡrequest
			HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			// POST�����ѵ��Ͳ���Ҫ���ԣ�
			//retry = currentReq != null && !"POST".equals(currentReq.getMethod());
			retry = currentReq != null;
		}
		if (retry) {
			// ����1���Ӻ��ټ�������
			SystemClock.sleep(RETRY_SLEEP_TIME_MILLIS);
		} else {
			exception.printStackTrace();
		}
		return retry;
	}
}