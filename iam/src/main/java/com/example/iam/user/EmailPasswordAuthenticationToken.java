/**
 * @author [author]
 * @email [example@mail.com]
 * @create date 2020-10-15 11:13:02
 * @modify date 2020-10-15 11:13:02
 * @desc [description]
 */
package com.example.iam.user;

import java.security.Principal;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class EmailPasswordAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 4305403417316811853L;

  private final Object principal;
  private Object credentials;

  public EmailPasswordAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  public EmailPasswordAuthenticationToken(
      Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  public Object getCredentials() {
    return this.credentials;
  }

  public Object getPrincipal() {
    return this.principal;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) {
    if (isAuthenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list"
              + " instead");
    }

    super.setAuthenticated(false);
  }

  @Override
  public String getName() {
    if (this.getPrincipal() instanceof CustomUserDetails) {
      return ((CustomUserDetails) this.getPrincipal()).getUsername();
    }
    if (this.getPrincipal() instanceof UserDetails) {
      return ((UserDetails) this.getPrincipal()).getUsername();
    }
    if (this.getPrincipal() instanceof AuthenticatedPrincipal) {
      return ((AuthenticatedPrincipal) this.getPrincipal()).getName();
    }
    if (this.getPrincipal() instanceof Principal) {
      return ((Principal) this.getPrincipal()).getName();
    }
    return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    credentials = null;
  }
}
