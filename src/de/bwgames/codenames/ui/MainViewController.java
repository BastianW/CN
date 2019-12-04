/**
 * 
 */
package de.bwgames.codenames.ui;

import java.net.URI;
import java.nio.file.Paths;

import de.bwgames.codenames.Game;
import de.bwgames.codenames.GameState;
import de.bwgames.codenames.WordState;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Bastian
 *
 */
public class MainViewController {
	private static final String SWITCHUSER_LABEL = "Am Zug ist: ";
	
	
	@FXML
	private GridPane gameBoard;
	
	@FXML
	private Label playerLabel;
	
	@FXML
	private Circle playerLamp;
	
	
	@FXML
	private Button switchPlayerButton;

	public void start(Game game) {
		System.out.println(game.getPlayer() + " beginnt");
		createGameBoard(game);
		
		updateUI(game);
		playerLamp.setFill(game.getPlayer()==WordState.BLUE?Color.BLUE:Color.RED);
		playerLabel.setText(SWITCHUSER_LABEL + game.getPlayer());
		switchPlayerButton.setText("Switch User");
		switchPlayerButton.setOnAction(event -> {
			switchPlayer(game);
		});

	}

	
	private void switchPlayer(Game game) {
		WordState oldPlayer = game.getPlayer();
		Color style;
		if (oldPlayer == WordState.BLUE) {
			game.setPlayer(WordState.RED);
			style = Color.RED;
		} else {
			game.setPlayer(WordState.BLUE);
			style = Color.BLUE;
		}
		playerLamp.setFill(style);
		playerLabel.setText(SWITCHUSER_LABEL + game.getPlayer());
	}

	private void createGameBoard(Game game) {
		String[] words = game.getWords();
		int colums = (int) Math.sqrt(game.getGameSize());
		for (int i = 0; i < game.getGameSize(); i++) {
			AnchorPane ancherPane = new AnchorPane();
			Button button = new Button();
			String word = words[i];
			button.setId(i + "");
			button.setText(word);
			button.setMaxWidth(Double.MAX_VALUE);
			button.setMaxHeight(Double.MAX_VALUE);
			button.setOnAction(event -> {
				String id = ((Node) event.getSource()).getId();
				GameState gameState = game.set(Integer.parseInt(id), game.getPlayer());
				if (gameState == GameState.SWITCH_PLAYER) {
					switchPlayer(game);
				}else if(gameState ==  GameState.WIN) {
					endGame(GameState.WIN, game.getPlayer());
				}else if(gameState ==  GameState.LOOSE) {
					endGame(GameState.LOOSE, game.getPlayer());
				}
				System.out.println(gameState);
				updateUI(game);
			});
			AnchorPane.setTopAnchor(button, 0.);
			AnchorPane.setLeftAnchor(button, 0.);
			AnchorPane.setRightAnchor(button, 0.);
			AnchorPane.setBottomAnchor(button, 0.);
			ancherPane.getChildren().add(button);
			gameBoard.add(ancherPane, i % colums, i / colums);
		}
	}

	private void endGame(GameState state, WordState player) {
		Stage dialog = new Stage();
		dialog.initOwner(gameBoard.getScene().getWindow());
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setScene(new Scene(new Label(player + " " +state), 80, 50));
		dialog.setOnCloseRequest(e->System.exit(0));
		dialog.showAndWait();
	}


	private void updateUI(Game game) {
		WordState[] wordStates = game.getWordStates();
		ObservableList<Node> children = gameBoard.getChildren();
		for (Node node : children) {
			Button button = (Button) ((AnchorPane) node).getChildren().get(0);
			int i = Integer.parseInt(button.getId());
			switch (wordStates[i]) {
			case BLACK:
			case BLUE:
			case EMPTY:
			case RED:
				button.setStyle("");
				break;
			case BLUE_DETECTED:
				button.setStyle("-fx-background-color: #0000ff;");
				break;
			case RED_DETECTED:
				button.setStyle("-fx-background-color: #ff0000;");
				break;
			case BLACK_DETECTED:
				button.setStyle("-fx-background-color: #f2f2f2;");
				break;
			default:
				break;
			}

		}
	}
	
	
	@FXML
	public void openAbout(ActionEvent actionEvent) {
		Stage stage = new Stage();
		stage.setScene(new Scene(new AnchorPane(), 800, 500));
		stage.showAndWait();
		
	}
	
	

}
