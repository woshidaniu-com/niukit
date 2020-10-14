package com.woshidaniu.db.core.callback;

import javax.security.auth.callback.NameCallback;

public class DecodeNameCallback extends NameCallback {

	public DecodeNameCallback(String prompt, String defaultName) {
		super(prompt, defaultName);
	}

	

}
