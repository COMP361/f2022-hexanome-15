package ca.mcgill.splendorclient.control;

import ca.mcgill.splendorclient.model.MoveInfo;
import ca.mcgill.splendorclient.model.users.User;
import ca.mcgill.splendorclient.view.gameboard.GameBoardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

/**
 * Game Controller.
 */
public class GameController {

  private Long gameId;
  private static String currentState;
  private GameBoardView localView;

  private static GameController instance = new GameController();
  private BoardUpdater updaterThread;

  /**
   * Sets the GameBoardView to the given view.
   *
   * @param view the given view
   */
  public void bindView(GameBoardView view) {
    localView = view;
  }

  /**
   * Creates a GameController object.
   */
  private GameController() {

  }

  /**
   * Returns this instance of GameController.
   *
   * @return this instance of GameController
   */
  public static GameController getInstance() {
    return instance;
  }

  /**
   * Sets the game id of the game.
   *
   * @param gameId the game id
   */
  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  /**
   * Returns the game id of the game.
   *
   * @return the game id of the game
   */
  public Long getGameId() {
    return gameId;
  }

  /**
   * Starts the game updater.
   */
  public static void start() {
    instance.updaterThread = instance.new BoardUpdater();
    instance.updaterThread.start();
  }

  /**
   * Sets the updaterThread to exit.
   */
  public static void stop() {
    instance.updaterThread.setExit();
    currentState = "";
  }


  private class BoardUpdater extends Thread {
    
    private boolean exit = false;
    
    public void setExit() {
      exit = true;
    }

    @Override
    public void run() {
      boolean requestedActions = false;
      while (true && !exit) {
        HttpResponse<JsonNode> response = Unirest
                                            .get(String.format("http://%s/api/games/%d/board", LobbyServiceExecutor.SERVERLOCATION, gameId))
                                            .asJson();
        if (response.getStatus() == 404) {
          try {
            System.out.println("Sleeping due to bad board response");
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          continue;
        }
        if (!response.getBody().getObject().optJSONArray("winningPlayers").isEmpty()) {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              JSONArray winnersArray =
                  response.getBody().getObject().optJSONArray("winningPlayers");
              Alert gameEndAlert = new Alert(Alert.AlertType.INFORMATION);
              StringBuilder winners = new StringBuilder();
              if (winnersArray.length() == 1) {
                gameEndAlert.setTitle("Game Ended");
                gameEndAlert.setHeaderText("The game had ended! The winner(s) are: "
                                             + winnersArray.getString(0)
                        + ". Congratulations!");
                gameEndAlert.show();
              } else {
                for (int i = 0; i < winnersArray.length() - 1; i++) {
                  winners.append(winnersArray.getString(i)).append(", ");
                }
                winners.append(winnersArray.getString(winnersArray.length() - 1));
                gameEndAlert.setTitle("Game Ended");
                gameEndAlert.setHeaderText("The game had ended! The winner(s) are: " + winners
                        + ". Congratulations!");
                gameEndAlert.show();
              }
            }
          });
          break;
        }
        if (!response.getBody().toPrettyString().equals(currentState)) {
          System.out.println(response.getBody().toPrettyString());
          currentState = response.getBody().toPrettyString();
          String currentTurn = response.getBody().getObject().getString("whoseTurn");
          Platform.runLater(new Runnable() {

            @Override
            public void run() {
              //update gameboard view
              JSONArray cardArray = response.getBody().getObject().optJSONArray("cardField");
              int[] cardids = new int[cardArray.length()];
              for (int i = 0; i < cardArray.length(); i++) {
                cardids[i] = cardArray.getInt(i);
              }
              GameBoardView.updateCardViews(cardids);

              JSONArray nobleArray = response.getBody().getObject().optJSONArray("nobles");
              int[] nobleids = new int[nobleArray.length()];
              for (int i = 0; i < nobleArray.length(); i++) {
                nobleids[i] = (int) (nobleArray.get(i));
              }
              GameBoardView.updateNobleViews(nobleids);
              
              GameBoardView.updateTurnLabels(currentTurn);

              //update inventories
              JSONArray inventories = response.getBody().getObject().getJSONArray("inventories");
              for (int player = 0; player < inventories.length(); player++) {
                JSONObject inventory = (JSONObject) inventories.get(player);
                final String playerName = inventory.getString("userName");
                JSONArray playerCardArray = inventory.getJSONArray("purchasedCards");
                int[] playerCardids = new int[playerCardArray.length()];
                for (int i = 0; i < playerCardArray.length(); i++) {
                  playerCardids[i] = playerCardArray.getInt(i);
                }

                JSONArray reservedCardArray = inventory.getJSONArray("reservedCards");
                int[] reservedCardids = new int[reservedCardArray.length()];
                for (int i = 0; i < reservedCardArray.length(); i++) {
                  reservedCardids[i] = reservedCardArray.getInt(i);
                }

                JSONArray reservedNobleArray = inventory.getJSONArray("reservedNobles");
                int[] reservedNobleids = new int[reservedNobleArray.length()];
                for (int i = 0; i < reservedNobleArray.length(); i++) {
                  reservedNobleids[i] = reservedNobleArray.getInt(i);
                }

                JSONArray visitingNobleArray = inventory.getJSONArray("visitingNobles");
                int[] visitingNobleids = new int[visitingNobleArray.length()];
                for (int i = 0; i < visitingNobleArray.length(); i++) {
                  visitingNobleids[i] = visitingNobleArray.getInt(i);
                }

                JSONArray acquiredCitiesArray = inventory.getJSONArray("cities");
                int[] acquiredCities = new int[acquiredCitiesArray.length()];
                for (int i = 0; i < acquiredCitiesArray.length(); i++) {
                  acquiredCities[i] = acquiredCitiesArray.getInt(i);
                }

                int[] placeholder = null;



                GameBoardView.updateInventories(playerName,
                    playerCardids,
                    reservedCardids,
                    inventory.getJSONObject("tokens").getInt("DIAMOND"),
                    inventory.getJSONObject("tokens").getInt("SAPPHIRE"),
                    inventory.getJSONObject("tokens").getInt("EMERALD"),
                    inventory.getJSONObject("tokens").getInt("RUBY"),
                    inventory.getJSONObject("tokens").getInt("ONYX"),
                    inventory.getJSONObject("tokens").getInt("GOLD"),
                    inventory.getJSONObject("purchasedCardCount").getInt("DIAMOND"),
                    inventory.getJSONObject("purchasedCardCount").getInt("SAPPHIRE"),
                    inventory.getJSONObject("purchasedCardCount").getInt("EMERALD"),
                    inventory.getJSONObject("purchasedCardCount").getInt("RUBY"),
                    inventory.getJSONObject("purchasedCardCount").getInt("ONYX"),
                    inventory.getJSONObject("purchasedCardCount").getInt("GOLD"),
                    inventory.getInt("prestige"),
                    visitingNobleids,
                    placeholder, reservedNobleids, acquiredCities);
              }

              //update tokens
              JSONObject tokens = response.getBody().getObject().getJSONObject("tokenField");
              GameBoardView.getInstance().getTokenPileViews().get(0).getCounter().setCount(
                  tokens.getInt("DIAMOND"));
              GameBoardView.getInstance().getTokenPileViews().get(1).getCounter().setCount(
                  tokens.getInt("SAPPHIRE"));
              GameBoardView.getInstance().getTokenPileViews().get(2).getCounter().setCount(
                  tokens.getInt("EMERALD"));
              GameBoardView.getInstance().getTokenPileViews().get(3).getCounter().setCount(
                  tokens.getInt("RUBY"));
              GameBoardView.getInstance().getTokenPileViews().get(4).getCounter().setCount(
                  tokens.getInt("ONYX"));
              GameBoardView.getInstance().getTokenPileViews().get(5).getCounter().setCount(
                  tokens.getInt("GOLD"));

              //update nobles


              //update decks
              JSONArray decks = response.getBody().getObject().getJSONArray("decks");
              int[] decksArray = new int[decks.length()];
              for (int i = 0; i < decks.length(); i++) {
                decksArray[i] = ((JSONObject) decks.get(i)).getInt("ncards");
              }
              GameBoardView.updateDecks(decksArray);

              //update Trading
              String gameServer = response.getBody().getObject()
                  .getString("gameServer");
              if (gameServer.equals("SplendorOrientTradingPosts")) {
                JSONArray tradingPostArray = response.getBody().getObject()
                                               .getJSONArray("tradingPosts");

                JSONArray firstShieldArray = tradingPostArray.getJSONObject(0)
                                               .getJSONArray("acquiredCoatOfArmsList");
                JSONArray secondShieldArray = tradingPostArray.getJSONObject(1)
                                                .getJSONArray("acquiredCoatOfArmsList");
                JSONArray thirdShieldArray = tradingPostArray.getJSONObject(2)
                                               .getJSONArray("acquiredCoatOfArmsList");
                JSONArray fourthShieldArray = tradingPostArray.getJSONObject(3)
                                                .getJSONArray("acquiredCoatOfArmsList");
                JSONArray fifthShieldArray = tradingPostArray.getJSONObject(4)
                                               .getJSONArray("acquiredCoatOfArmsList");

                final String[] shields1 = new String[firstShieldArray.length()];
                final String[] shields2 = new String[secondShieldArray.length()];
                final String[] shields3 = new String[thirdShieldArray.length()];
                final String[] shields4 = new String[fourthShieldArray.length()];
                final String[] shields5 = new String[fifthShieldArray.length()];

                for (int i = 0; i < shields1.length; i++) {
                  shields1[i] = firstShieldArray.getString(i);
                }

                for (int i = 0; i < shields2.length; i++) {
                  shields2[i] = secondShieldArray.getString(i);
                }

                for (int i = 0; i < shields3.length; i++) {
                  shields3[i] = thirdShieldArray.getString(i);
                }

                for (int i = 0; i < shields4.length; i++) {
                  shields4[i] = fourthShieldArray.getString(i);
                }

                for (int i = 0; i < shields5.length; i++) {
                  shields5[i] = fifthShieldArray.getString(i);
                }

                GameBoardView.updatePowers(shields1, shields2, shields3, shields4, shields5);
              }

              //update cities
              if (gameServer.equals("SplendorOrientCities")) {
                JSONArray cityArray = response.getBody().getObject().optJSONArray("cities");
                int[] cityids = new int[cityArray.length()];
                for (int i = 0; i < cityArray.length(); i++) {
                  cityids[i] = (int) (cityArray.get(i));
                }
                GameBoardView.updateCityViews(cityids);
              }
              
            }

          });
          if (currentTurn.equals(User.THISUSER.getUsername()) && !requestedActions) {
            requestedActions = true;
            HttpResponse<JsonNode> moveMap = Unirest
                                               .get(String.format("http://%s/api/games/%d/players/%s/actions",
                                                 LobbyServiceExecutor.SERVERLOCATION,
                                                 gameId, currentTurn))
                                               .queryString("access_token",
                                                 User.THISUSER.getAccessToken())
                                               .asJson();
            System.out.println(moveMap.getBody().toPrettyString());
            String moves = moveMap.getBody().toString();
            Gson gson = new Gson();
            Map<String, MoveInfo> availableMoves = gson.fromJson(moves,
                new TypeToken<Map<String, MoveInfo>>() {}.getType());
            ActionManager.setCurrentMoveMap(availableMoves);

            // alert to indicate its their turn
            Platform.runLater(new Runnable() {

              @Override
              public void run() {
                Alert yourTurnAlert = new Alert(Alert.AlertType.INFORMATION);
                yourTurnAlert.setTitle("Turn Information");
                yourTurnAlert.setHeaderText("It's your turn, please make your move.");
                yourTurnAlert.show();
              }

            });
          } else if (!currentTurn.equals(User.THISUSER.getUsername())) {
            // alert to tell user it's not their turn
            requestedActions = false;
            ActionManager.setCurrentMoveMap(new HashMap<String, MoveInfo>());
            Platform.runLater(new Runnable() {

              @Override
              public void run() {
                Alert notYourTurnAlert = new Alert(Alert.AlertType.INFORMATION);
                notYourTurnAlert.setTitle("Turn Information");
                notYourTurnAlert.setHeaderText("It's " + currentTurn
                                                 + "'s turn, please wait for them to go.");
                notYourTurnAlert.show();
              }
            });
          }
        }
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

}
