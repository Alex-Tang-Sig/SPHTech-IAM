
package com.example.iam.user;

import org.springframework.security.core.AuthenticationException;

public class EmailNotFoundException extends AuthenticationException {

  private static final long serialVersionUID = 3776379791448855245L;

  public EmailNotFoundException(String msg) {
		super(msg);
	}

	public EmailNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}