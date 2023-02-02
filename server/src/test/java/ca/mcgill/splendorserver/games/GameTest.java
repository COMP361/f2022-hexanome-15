//package ca.mcgill.splendorserver.games;
//
//import org.junit.jupiter.api.Test;
//
//import ca.mcgill.splendorserver.model.Game;
//import ca.mcgill.splendorserver.model.GameBoard;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class GameTest {
//
//  @Test
//  void gameID() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Jeff"));
//    players.add(PlayerWrapper.newPlayerWrapper("Larry"));
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    assertEquals(270189000L, game.gameid(), "");
//  }
//
//  @Test
//  void savegame() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Jeff"));
//    players.add(PlayerWrapper.newPlayerWrapper("Larry"));
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    assertEquals("", game.savegame(), "");
//
//    String sg1 = "splendor1";
//    Game game1 = new Game(sg1, id, players, gb);
//    assertEquals("splendor1", game1.savegame(), "");
//  }
//
//  @Test
//  void removePlayer() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    game.removePlayer(players.get(0).newPlayerWrapper("Zack"));
////    assertFalse(game.iterator().hasNext(), "");
//  }
//
//  @Test
//  void endTurn() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Sofia"));
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    assertEquals(players.get(1),game.endTurn(), "");
//  }
//
//  @Test
//  void whosTurn() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Sofia"));
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    assertEquals(players.get(0),game.whoseTurn(), "");
//  }
//
//  @Test
//  void testToString() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Jeff"));
//    players.add(PlayerWrapper.newPlayerWrapper("Larry"));
//    TurnManager aTakeTurn = new TurnManager(players);
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
//    assertEquals("Game [savegame=" +
//                   sg + ", " +
//                   "gameID=" +
//                   id + ", " + "aTakeTurn=" + aTakeTurn + "]", game.toString(), "");
//
//
//  }
//
//  @Test
//  void iterator() {
//    String sg = "";
//    long id = 270189000;
//    List<PlayerWrapper> players = new ArrayList<>();
//    players.add(PlayerWrapper.newPlayerWrapper("Zack"));
//    players.add(PlayerWrapper.newPlayerWrapper("Jeff"));
//    players.add(PlayerWrapper.newPlayerWrapper("Larry"));
//    TurnManager aTakeTurn = new TurnManager(players);
//    GameBoard gb = new GameBoard();
//    Game game = new Game(sg, id, players, gb);
////    assertTrue(game.iterator().hasNext(), "");
//
//  }
//}
