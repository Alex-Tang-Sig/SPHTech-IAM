package com.example.iam.user;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
 
  public static final String SPRING_SECURITY_FORM_EMAIL_KEY = "email";
  public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

  private String emailParameter = SPRING_SECURITY_FORM_EMAIL_KEY;
  private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
  private boolean postOnly = true;

 
  public EmailPasswordAuthenticationFilter() {
		super(new AntPathRequestMatcher("/emailLogin", "POST"));
	}

	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}

		String email = obtainEmail(request);
		String password = obtainPassword(request);

		if (email == null) {
			email = "";
		}

		if (password == null) {
			password = "";
		}

		email = email.trim();

		EmailPasswordAuthenticationToken authRequest = new EmailPasswordAuthenticationToken(
				email, password);

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Nullable
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	@Nullable
	protected String obtainEmail(HttpServletRequest request) {
		return request.getParameter(emailParameter);
	}

	protected void setDetails(HttpServletRequest request,
			EmailPasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	public void setEmailnameParameter(String emailParameter) {
		Assert.hasText(emailParameter, "Email parameter must not be empty or null");
		this.emailParameter = emailParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getEmailParameter() {
		return emailParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
