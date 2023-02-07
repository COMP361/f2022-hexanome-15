package ca.mcgill.splendorserver.control;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Provides exception for error caught while authenticating a user and their access token.
 *
 * @author Zachary Hayden
 * Date: 2/6/23
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "Error encountered while authenticating user and their access token")
public class TokenAuthenticationException extends RuntimeException {
  public TokenAuthenticationException(String message) {
    super(message);
  }
}
