package ca.mcgill.splendorclient.gui.lobbyservice;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import ca.mcgill.splendorclient.gui.scenemanager.SceneManager;
import ca.mcgill.splendorclient.lobbyserviceio.LobbyServiceExecutor;
import ca.mcgill.splendorclient.users.User;
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

/**
 * Controls Lobby Service functions.
 */
public class LobbyController implements Initializable {
	private final LobbyServiceExecutor ls = LobbyServiceExecutor.LOBBY_SERVICE_EXECUTOR;
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
	private ChoiceBox<String> gameserviceChoiceBox;
	
	private String selectedSession = "";
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<String> cope = get_gameservices();
		ObservableList<String> listcope = FXCollections.observableArrayList(cope);
		gameserviceChoiceBox.setItems(listcope);
		availableSessionList.getItems().addAll(get_all_sessions());

		joinSessionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				User user = User.THISUSER;
				String toJoin = availableSessionList.getSelectionModel().getSelectedItem();
				add_player_to_session(user.getUsername(), user.getAccessToken(), toJoin);//selectedSession);
				while (!get_session(toJoin).getBoolean("launched")) {
					//wait
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Splendor.transitionTo(SceneManager.getGameScreen(), Optional.of("Game Screen"));
			}
		});

		launchSessionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				User user = User.THISUSER;
				String toJoin = availableSessionList.getSelectionModel().getSelectedItem();
				launch_session(toJoin, user.getAccessToken());
				Splendor.transitionTo(SceneManager.getGameScreen(), Optional.of("Game Screen"));
			}
		});
		
		createSessionButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO: fix this so that it adds the session object to available sessions list
				User user = User.THISUSER;
				//ls.create_session(user.getAccessToken(), user.getUsername(), gameserviceChoiceBox.getValue(), "");
				
				System.out.println(user.getAccessToken());
				create_session(user.getAccessToken(), user.getUsername(), gameserviceChoiceBox.getValue(), "");
				availableSessionList.getItems().clear();
				availableSessionList.getItems().addAll(get_all_sessions());
				/*ArrayList<String> arr = get_all_sessions();
				for (String key : arr) {
					availableSessionList.getItems().add(key);
				}*/
			}
		});

		// launches the selected session
		/*launchSessionButton.setOnAction(new EventHandler<ActionEvent>() {
			// TODO: setup with LS
			@Override
			public void handle(ActionEvent arg0) {
			}
		});*/
		
		/*sessionNameText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				selectedSession = sessionNameText.getSelectedText();
			}
		});*/
	}
	
	/**
	 * 
	 * @param accessToken
	 * @param createUserName
	 * @param gameName
	 * @param saveGame
	 */
	private void create_session(String accessToken, String createUserName, String gameName, String saveGame) {
		checkNotNullNotEmpty(accessToken, createUserName, gameName, saveGame);
//		accessToken = accessToken.replaceAll("\\+", "\\+");

		if (saveGame == null) {
			HttpResponse<String> response = Unirest.post(
							"http://127.0.0.1:4242/api/sessions"
									+ "?access_token="
									+ accessToken+'='
					)
					.header("Authorization", "Bearer" + accessToken)
					.header("Content-Type", "application/json")
					.body(String.format("{\"creator\":\"%s\", \"game\":\"%s\", \"savegame\":\"\"}", createUserName, gameName))
					.asString();
			System.out.println(response.getBody());
		}
		else {
			HttpResponse<String> response2;
			try {
				response2 = Unirest.post(
										"http://127.0.0.1:4242/api/sessions"
										+ "?access_token="
										+ URLEncoder.encode(accessToken, "UTF-8")//accessToken+'='
										)
						.header("Authorization", "Bearer" + accessToken)
						.header("Content-Type", "application/json")
						.body(String.format("{\"creator\":\"%s\", \"game\":\"%s\", \"savegame\":\"%s\"}", createUserName, gameName, saveGame))
						.asString();
						System.out.println(response2.getBody());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 */
	private ArrayList<String> get_all_sessions() {
		HttpResponse<JsonNode> response = Unirest.get(
						"http://127.0.0.1:4242/api/sessions"
				)
				.asJson();
		JSONObject obj = response.getBody().getObject();
		obj = obj.getJSONObject("sessions");
		ArrayList<String> arr = new ArrayList<>();
		Iterator keys = obj.keys();
		while (keys.hasNext()) {
			Object key = keys.next();
			arr.add((String) key);
		}
		return arr;
	}

	/**
	 * @param sessionID
	 * @return
	 */
	private JSONObject get_session(String sessionID) {
		HttpResponse<JsonNode> response = Unirest.get(
						"http://127.0.0.1:4242/api/sessions/"
								+ sessionID
				)
				.asJson();
		System.out.println(response.getBody().toString());
		return response.getBody().getObject();
	}

	/**
	 * @param sessionID
	 * @param accessToken
	 */
	private void launch_session(String sessionID, String accessToken) {
		try {
			HttpResponse<String> response = Unirest.post(
							"http://127.0.0.1:4242/api/sessions/" + sessionID
									+ "?access_token=" + URLEncoder.encode( accessToken, "UTF-8")
					)
					.asString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param sessionID
	 * @param accessToken
	 */
	private void delete_session(String sessionID, String accessToken) {
		HttpResponse<String> response = Unirest.delete(
						"http://127.0.0.1:4242/api/sessions/" + sessionID
								+ "?access_token=" + accessToken
				)
				.header("Authorization", "Bearer" + accessToken)
				.asString();
	}

	/**
	 * @param playerName
	 * @param accessToken
	 * @param sessionID
	 */
	private void add_player_to_session(String playerName, String accessToken, String sessionID) {
		HttpResponse<String> response;
		try {
			response = Unirest.put(
							"http://127.0.0.1:4242/api/sessions/" + sessionID
									+ "/players/" + playerName + "?access_token="
									+ URLEncoder.encode( accessToken, "UTF-8")
					)
					.asString();
					System.out.println(response.getBody());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param playerName
	 * @param accessToken
	 * @param sessionID
	 */
	private void delete_player_from_session(String playerName, String accessToken, String sessionID) {
		HttpResponse<String> response;
		try {
			response = Unirest.delete(
							"http://127.0.0.1:4242/api/sessions/" + sessionID
									+ "/players/" + playerName + "?access_token="
									+ URLEncoder.encode( accessToken, "UTF-8")
					)
					.asString();
					System.out.println(response.getBody());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private ArrayList<String> get_gameservices() {
		HttpResponse<JsonNode> response = Unirest.get(
						"http://127.0.0.1:4242/api/gameservices"
				)
				.asJson();
		//JSONObject obj = response.getBody().getObject();
		ArrayList<String> arr = new ArrayList<>();
		JsonNode json =  response.getBody();//new JSONArray(response.getBody().getObject());
		JSONArray jarr = new JSONArray(json.toString());
		for (Object obj : jarr) {
			arr.add(((JSONObject) obj).get("name").toString());
		}
		return arr;
	}

	/**
	 * @param args
	 */
	private void checkNotNullNotEmpty(String... args) {
		for (String arg : args) {
			assert arg != null && arg.length() != 0 : "Arguments cannot be empty nor null.";
		}
	}
}

