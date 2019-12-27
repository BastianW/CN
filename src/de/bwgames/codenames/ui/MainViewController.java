/**
 * 
 */
package de.bwgames.codenames.ui;

import de.bwgames.codenames.Game;
import de.bwgames.codenames.GameState;
import de.bwgames.codenames.WordState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
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

	@FXML
	private AnchorPane anchorpane;

	private Line[][] lines; 
	
	public void start(Game game) {
		System.out.println(game.getPlayer() + " beginnt");
		lines = new Line[game.getGameSize()][2];
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
			button.setFont(new Font("Cambria", 24));
			button.setMaxWidth(Double.MAX_VALUE);
			button.setMaxHeight(Double.MAX_VALUE);
			button.setOnAction(event -> {
				String id = ((Node) event.getSource()).getId();
				GameState gameState = game.set(Integer.parseInt(id), game.getPlayer());
				System.out.println(gameState);
				updateUI(game);
				if (gameState == GameState.SWITCH_PLAYER) {
					switchPlayer(game);
				}else if(gameState ==  GameState.WIN) {
					endGame(GameState.WIN, game.getPlayer());
				}else if(gameState ==  GameState.LOOSE) {
					endGame(GameState.LOOSE, game.getPlayer());
				}

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
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("End of Game");
		alert.setHeaderText(null);
		alert.setContentText(player + " " +state);
//		alert.setOnCloseRequest(e->System.exit(0));
		alert.showAndWait();
	}


	private void updateUI(Game game) {
		WordState[] wordStates = game.getWordStates();
		ObservableList<Node> children = gameBoard.getChildren();
		for (Node node : children) {
			Button button = (Button) ((AnchorPane) node).getChildren().get(0);
			int i = Integer.parseInt(button.getId());
		switch (wordStates[i]) {
		case EMPTY:
			button.setBackground(Colors.WHITE);
			break;
		case BLUE_DETECTED:
			setCross(i, button);
		case BLUE:
			button.setBackground(Colors.BLUE);
			break;
		case RED_DETECTED:
			setCross(i, button);
		case RED:
			button.setBackground(Colors.RED);
			break;
		case BLACK_DETECTED:
			setCross(i, button);
		case BLACK:
			button.setBackground(Colors.BLACK);
			break;
		default:
			break;
		}
	}
	}

		
	private void setCross(int i, Button l) {
			//Cross already set
			if(lines[i][0]!=null) {
				return;
			}
			Bounds labelBounce = l.localToScene(l.getBoundsInLocal());
			Bounds labelInAnchor = anchorpane.sceneToLocal(labelBounce);
			Line l1 = new Line(labelInAnchor.getMinX(), labelInAnchor.getMinY(), labelInAnchor.getMaxX(), labelInAnchor.getMaxY());
			Line l2 = new Line(labelInAnchor.getMaxX(), labelInAnchor.getMinY(), labelInAnchor.getMinX(), labelInAnchor.getMaxY());
			l1.setFill(Color.BLACK);
			l2.setFill(Color.BLACK);
			lines[i][0] = l1;
			lines[i][1] = l2;
			
			anchorpane.getChildren().add(l1);
			anchorpane.getChildren().add(l2);
			
			l.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
				@Override
				public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
					Bounds labelBounce = l.localToScene(l.getBoundsInLocal());
					Bounds labelInAnchor = anchorpane.sceneToLocal(labelBounce);
					l1.setStartX(labelInAnchor.getMinX());
					l1.setStartY(labelInAnchor.getMinY());
					l1.setEndX(labelInAnchor.getMaxX());
					l1.setEndY(labelInAnchor.getMaxY());
					
					l2.setStartX(labelInAnchor.getMaxX());
					l2.setStartY(labelInAnchor.getMinY());
					l2.setEndX(labelInAnchor.getMinX());
					l2.setEndY(labelInAnchor.getMaxY());
				}
			});
		}
	
	
	
	
	@FXML
	public void openAbout(ActionEvent actionEvent) {
		Stage stage = new Stage();
		stage.setScene(new Scene(new AnchorPane(), 800, 500));
		stage.showAndWait();
		
	}
	
	
	@FXML
	public void quit(ActionEvent actionEvent) {
		System.exit(0);
	}
	

}
