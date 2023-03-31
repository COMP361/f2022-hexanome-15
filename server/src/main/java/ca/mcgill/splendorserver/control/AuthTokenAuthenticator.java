package ca.mcgill.splendorserver.control;

import java.util.Optional;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

/**
 * Static utility class intended to authenticate user tokens during token restricted actions.
 *
 * @author Zachary Hayden
 */
@Service
public final class AuthTokenAuthenticator {
  private static final String lobbyServiceLocation = "localhost:4242";
  private static final String GET_USERNAME         = "/oauth/username";
  private static final String GET_ROLE             = "/oauth/role";

  private AuthTokenAuthenticator() {}

  /**
   * Gets the role (ROLE_PLAYER, ROLE_ADMIN) of player associated to token.
   *
   * @param accessToken access token
   * @return role if it exists else empty optional.
   */
  public static Optional<String> getRole(String accessToken) {
    assert accessToken != null;
    HttpResponse<String> httpResponse = Unirest.get(lobbyServiceLocation + GET_ROLE)
                                               .queryString("access_token", accessToken)
                                               .asString();
    if (httpResponse.isSuccess()) {
      return Optional.of(httpResponse.getBody());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Gets the username associated to given access token in the global Lobby service.
   *
   * @param accessToken access token to get username of, cannot be null.
   * @return username if it exists else empty optional
   */
  public static Optional<String> getUserName(String accessToken) {
    assert accessToken != null;
    HttpResponse<String> httpResponse = Unirest
        .get(lobbyServiceLocation + GET_USERNAME)
        .queryString("access_token", accessToken)
        .asString();
    // either give the response or none if there is no name to return
    if (httpResponse.isSuccess()) {
      return Optional.of(httpResponse.getBody());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Tests if the username and the username associated to the auth token are the same.
   *
   * @param userName    username
   * @param accessToken auth token
   * @return if the name associated to the access token is same as given name
   *     if there's no name corresponding to the access token it will return false
   */
  public static boolean isSameName(String userName, String accessToken) {
    assert userName != null && accessToken != null;
    Optional<String> otherName = getUserName(accessToken);
    return otherName.filter(userName::equals)
                    .isPresent();
  }

  /**
   * Checks if the user is a player.
   *
   * @param accessToken the access token of the user
   * @return boolean determining if the given user is a player
   */
  public static boolean isPlayer(String accessToken) {
    assert accessToken != null;
    Optional<String> otherRole = getRole(accessToken);
    return otherRole.filter(s -> s.trim().equals("ROLE_PLAYER")
                                   || s.trim().equals("ROLE_ADMIN")).isPresent();
  }

  /**
   * Validates that the given username and corresponding access token are valid and agree.
   *
   * @param userName    username
   * @param accessToken user's access token
   * @throws TokenAuthenticationException if the username and access token
   *     don't correspond to each other or if the user isn't a player
   */
  public static void authenticate(String userName, String accessToken) {
    if (isSameName(userName, accessToken) && isPlayer(accessToken)) {
      System.out.println("couldn't auth?");
      return;
    }
    throw new TokenAuthenticationException(
        String.format("Username (%s) invalid against access token (%s)", userName, accessToken));
  }


}
