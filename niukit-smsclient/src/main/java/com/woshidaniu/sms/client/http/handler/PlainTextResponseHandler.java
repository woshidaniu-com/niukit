package com.woshidaniu.sms.client.http.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.woshidaniu.sms.client.http.HttpIOUtils;
import com.woshidaniu.sms.client.http.HttpStatus;
import com.woshidaniu.sms.client.http.exception.HttpResponseException;

public class PlainTextResponseHandler implements ResponseHandler<String> {

	@Override
	public void preHandle(HttpURLConnection httpConn) {
		httpConn.setConnectTimeout(5000);
		httpConn.setReadTimeout(30000);
	}

	@Override
	public String handleResponse(HttpURLConnection httpConn, String charset) throws IOException {
		int status = httpConn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_OK && status < HttpURLConnection.HTTP_MULT_CHOICE) {
			return HttpIOUtils.toInputText(httpConn.getInputStream(), charset);
		} else {
			String error = HttpIOUtils.toInputText(httpConn.getErrorStream(), charset);
			if(error != null && error.trim().length() > 0) {
				throw new HttpResponseException(status, error);
			}
			throw new HttpResponseException(status, HttpStatus.getStatusText(status));
		}
	}

}
