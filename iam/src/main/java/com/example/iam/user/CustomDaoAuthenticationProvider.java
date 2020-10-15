package com.example.iam.user;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.util.Assert;

public class CustomDaoAuthenticationProvider extends AbstractCustomUserDetailsAuthenticationProvider {
  // ~ Static fields/initializers
  // =====================================================================================

  /**
   * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
   * String)} on when the user is not found to avoid SEC-2056.
   */
  private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

  // ~ Instance fields
  // ================================================================================================

  private PasswordEncoder passwordEncoder;

  /**
   * The password used to perform
   * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is not
   * found to avoid SEC-2056. This is necessary, because some
   * {@link PasswordEncoder} implementations will short circuit if the password is
   * not in a valid format.
   */
  private volatile String userNotFoundEncodedPassword;

  private CustomUserDetailsService userDetailsService;

  private UserDetailsPasswordService userDetailsPasswordService;

  public CustomDaoAuthenticationProvider() {
		setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	// ~ Methods
	// ========================================================================================================

	protected void additionalAuthenticationChecks(CustomUserDetails userDetails,
    EmailPasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractCustomUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
	}

	protected void doAfterPropertiesSet() {
		Assert.notNull(this.userDetailsService, "A CustomUserDetailsService must be set");
	}

	protected final CustomUserDetails retrieveUser(String email,
    EmailPasswordAuthenticationToken authentication)
			throws AuthenticationException {
    prepareTimingAttackProtection();
		try {
      CustomUserDetails loadedUser = this.getUserDetailsService().loadUserByEmail(email);
      System.out.println("**************2*****************");
      System.out.println(loadedUser.getEmail());
      System.out.println("**************2*****************");
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"CustomUserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, CustomUserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null
				&& this.passwordEncoder.upgradeEncoding(user.getPassword());
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = (CustomUserDetails) this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}

	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		}
	}

	private void mitigateAgainstTimingAttack(EmailPasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
		}
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
		this.userNotFoundEncodedPassword = null;
	}

	protected PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setUserDetailsService(CustomUserDetailsService customUserDetailsService) {
		this.userDetailsService = customUserDetailsService;
	}

	protected CustomUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsPasswordService(
			UserDetailsPasswordService userDetailsPasswordService) {
		this.userDetailsPasswordService = userDetailsPasswordService;
	}
}
