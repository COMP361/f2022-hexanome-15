package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.model.users.User;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Controls Lobby Service functions.
 */
@Component
public class LobbyController implements Initializable {
  @FXML
  private Button createSessionButton;
  @FXML
  private ListView<String> availableSessionList;
  @FXML
  private Button launchSessionButton;
  @FXML
  private Button joinSessionButton;
  @FXML
  private TextField sessionNameText;
  @FXML
  private Button deleteSessionButton;
  @FXML
  private Button logoutButton;
  @FXML
  private ChoiceBox<String> gameserviceChoiceBox;

  // setting lobby service location
  @Value("{lobbyservice.location}")
  private String lobbyServiceLocation = "http://127.0.0.1:4242"; // TODO: fix the value injection

  /**
   * Creates a LobbyController object.
   */
  public LobbyController() {
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    ArrayList<String> cope = get_gameservices();
    ObservableList<String> listcope = FXCollections.observableArrayList(cope);
    gameserviceChoiceBox.setItems(listcope);

    //get session updates by polling
    new Thread(new Runnable() {

      @Override
      public void run() {
        while (true) {
          Platform.runLater(() -> { 
            if (!availableSessionList.getItems().stream()
                   .collect(Collectors.toList()).equals(get_all_sessions())) {
              availableSessionList.getItems().clear();
              availableSessionList.getItems().addAll(get_all_sessions());
            }
          });
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();

    logoutButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        revoke_auth(User.THISUSER.getAccessToken());
        User.logout(User.THISUSER.getUsername());
        Splendor.transitionTo(SceneManager.getLoginScreen(), Optional.of("Login Screen"));
      }
    });

    deleteSessionButton.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        String toDelete = availableSessionList.getSelectionModel().getSelectedItem();
        if (delete_session(toDelete, User.THISUSER.getAccessToken())) {
          //TODO: add something to this if block
        } else {
          //issue deleting the session
          System.out.println("Failed to delete session: " + toDelete);
        }
      }
    });
    joinSessionButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        User user = User.THISUSER;
        String sessionToJoin = availableSessionList
                                 .getSelectionModel().getSelectedItem();
        if (add_player_to_session(user.getUsername(),
            user.getAccessToken(), sessionToJoin)) {
          new Thread(new Runnable() {

            @Override
            public void run() {
              while (true) {
                try {
                  Thread.sleep(2000);
                  JSONObject sessionInfo = get_session(sessionToJoin);
                  if (sessionInfo.getBoolean("launched")) {
                    Platform.runLater(() -> {
                      Splendor.transitionToGameScreen(Long.valueOf(sessionToJoin), sessionInfo);
                      GameController.getInstance().setGameId(Long.valueOf(sessionToJoin));
                      GameController.start();
                    });
                    break;
                  }
                } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
            }
          }).start();;
        } else {
          //failed to join session, notify user.
        }
      }
    });

    launchSessionButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        User user = User.THISUSER;
        String sessionToLaunch = availableSessionList
                                   .getSelectionModel().getSelectedItem();
        if (launch_session(sessionToLaunch, user.getAccessToken())) {
          Splendor.transitionToGameScreen(Long.valueOf(sessionToLaunch),
              get_session(sessionToLaunch));
          GameController.getInstance()
              .setGameId(Long.valueOf(sessionToLaunch));
          GameController.start();
        } else {
          //notify user failure to launch session.
        }
      }
    });

    createSessionButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        //TODO: fix this so that it adds session object to sessions list
        User user = User.THISUSER;
        // ls.create_session(user.getAccessToken(),
        // user.getUsername(), gameserviceChoiceBox.getValue(), "");
        System.out.println(user.getAccessToken());
        create_session(user.getAccessToken(),
            user.getUsername(), gameserviceChoiceBox.getValue(), "");
        availableSessionList.getItems().clear();
        availableSessionList.getItems().addAll(get_all_sessions());
      }
    });
  }

  /**
   * Creates a new game session.
   *
   * @param accessToken the access token
   * @param createUserName the username of the creator
   * @param gameName the name of the game
   * @param saveGame the name of the saved game
   */
  private void create_session(String accessToken,
                              String createUserName, String gameName, String saveGame) {
    checkNotNullNotEmpty(accessToken, createUserName, gameName, saveGame);

    if (saveGame == null) {
      HttpResponse<String> response = Unirest.post(
          lobbyServiceLocation + "/api/sessions"
            + "?access_token="
            + accessToken)
                                        .header("Authorization", "Bearer" + accessToken)
                                        .header("Content-Type", "application/json")
                                        .body(String.format("{\"creator\":\"%s\", "
                                                              + "\"game\":\"%s\", "
                                                              + "\"savegame\":\"\"}",
                                          createUserName, gameName))
                                        .asString();
      System.out.println(response.getBody());
    } else {
      HttpResponse<String> response2;
      response2 = Unirest.post(
        lobbyServiceLocation + "/api/sessions"
          + "?access_token="
          + accessToken)
                    .header("Authorization", "Bearer" + accessToken)
                    .header("Content-Type", "application/json")
                    .body(String.format("{\"creator\":\"%s\", "
                                          + "\"game\":\"%s\", "
                                          + "\"savegame\":\"%s\"}",
                      createUserName, gameName, saveGame))
                    .asString();
      System.out.println(response2.getBody());
    }
  }

  /**
   * Gets all the sessions from the lobby service.
   *
   * @return a list of sessions
   */
  private ArrayList<String> get_all_sessions() {
    HttpResponse<JsonNode> response = Unirest.get(
        lobbyServiceLocation + "/api/sessions").asJson();
    JSONObject obj = response.getBody().getObject();
    obj = obj.getJSONObject("sessions");
    ArrayList<String> arr = new ArrayList<>();
    Iterator<String> keys = obj.keys();
    while (keys.hasNext()) {
      Object key = keys.next();
      arr.add((String) key);
    }
    return arr;
  }

  /**
   * Gets the session from the lobby service based on the session id.
   *
   * @param sessionid the given session id
   * @return a JSONObject of the session
   */
  private JSONObject get_session(String sessionid) {
    HttpResponse<JsonNode> response = Unirest.get(
        lobbyServiceLocation + "/api/sessions/"
        + sessionid).asJson();
    return response.getBody().getObject();
  }

  /**
   * Launches a session.
   *
   * @param sessionid the id of the session to be launched
   * @param accessToken the access token
   */
  private boolean launch_session(String sessionid, String accessToken) {
    try {
      HttpResponse<String> response = Unirest.post(
          lobbyServiceLocation + "/api/sessions/" + sessionid
            + "?access_token=" + URLEncoder.encode(accessToken, "UTF-8")
        )
                                        .asString();
      System.out.println("Response from launch: " + response.getBody().toString());
      //
      return response.isSuccess();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a session.
   *
   * @param sessionid the id of the session to be deleted
   * @param accessToken the access token
   */
  private boolean delete_session(String sessionid, String accessToken) {
    try {
      HttpResponse<String> response = Unirest.delete(
          lobbyServiceLocation + "/api/sessions/" + sessionid
            + "?access_token=" + URLEncoder.encode(accessToken, "UTF-8")
        )
                                        .asString();
      System.out.println("Response from delete: " + response.getBody().toString());
      return response.getStatus() % 100 == 2;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    }
  }

  private void revoke_auth(String accessToken) {
    try {
      HttpResponse<String> response =
          Unirest.delete(lobbyServiceLocation + "/oauth/active?access_token="
                         + URLEncoder.encode(accessToken, "UTF-8")).asString();
      if (!response.isSuccess()) {
        System.out.println("Failed to logout");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a player to the session.
   *
   * @param playerName the name of the player
   * @param accessToken the access token
   * @param sessionid the id of the session
   */
  private boolean add_player_to_session(String playerName, String accessToken, String sessionid) {
    HttpResponse<String> response;
    try {
      response = Unirest.put(
          lobbyServiceLocation + "/api/sessions/" + sessionid
            + "/players/" + playerName + "?access_token="
            + URLEncoder.encode(accessToken, "UTF-8")
        )
                   .asString();
      System.out.println("Response from add player: " + response.getBody());
      if (response.getStatus() % 100 == 4) {
        return false;
      }
      return true;
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a player from the session.
   *
   * @param playerName the name of the player
   * @param accessToken the access token
   * @param sessionid the id of the session
   */
  private void delete_player_from_session(String playerName, String accessToken, String sessionid) {
    HttpResponse<String> response;
    try {
      response = Unirest.delete(
          lobbyServiceLocation + "/api/sessions/" + sessionid
            + "/players/" + playerName + "?access_token="
            + URLEncoder.encode(accessToken, "UTF-8")
        )
                   .asString();
      System.out.println("Response from delete player: " + response.getBody());
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Gets the game services from the lobby service.
   *
   * @return a list of game services
   */
  private ArrayList<String> get_gameservices() {
    HttpResponse<JsonNode> response = Unirest.get(
        lobbyServiceLocation + "/api/gameservices"
      )
                                        .asJson();
    System.out.println("Response from get_gameservices: " + response.getBody().toString());
    ArrayList<String> arr = new ArrayList<>();
    JsonNode json = response.getBody(); //new JSONArray(response.getBody().getObject());
    JSONArray jarr = new JSONArray(json.toString());
    for (Object obj : jarr) {
      arr.add(((JSONObject) obj).get("name").toString());
    }
    return arr;
  }

  /**
   * Checks if arguments are empty or null.
   *
   * @param args the given arguments
   */
  private void checkNotNullNotEmpty(String... args) {
    for (String arg : args) {
      assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
    }
  }
}

