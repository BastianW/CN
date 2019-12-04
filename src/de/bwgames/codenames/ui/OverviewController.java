/**
 * 
 */
package de.bwgames.codenames.ui;

import de.bwgames.codenames.Game;
import de.bwgames.codenames.GameState;
import de.bwgames.codenames.WordState;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * @author Bastian
 *
 */
public class OverviewController {
	
	@FXML
	private GridPane overview; 
	
	public void initialize() {
		
	}
	
	public void start(Game game) {
		createGameBoard(game);
	}

	private void createGameBoard(Game game) {
		String[] words = game.getWords();
		int colums = (int) Math.sqrt(game.getGameSize());
		for (int i = 0; i < game.getGameSize(); i++) {
			AnchorPane ancherPane = new AnchorPane();
			Label label = new Label();
			String word = words[i];
			label.setId(i + "");
			label.setText(word);
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMaxHeight(Double.MAX_VALUE);
			setStyleToLabel(game, label);
			
			AnchorPane.setTopAnchor(label, 0.);
			AnchorPane.setLeftAnchor(label, 0.);
			AnchorPane.setRightAnchor(label, 0.);
			AnchorPane.setBottomAnchor(label, 0.);
			ancherPane.getChildren().add(label);
			overview.add(ancherPane, i % colums, i / colums);
		}
		
	}
	
	private void setStyleToLabel(Game game, Label label) {
		WordState[] wordStates = game.getWordStates();
		int i = Integer.parseInt(label.getId());
		switch (wordStates[i]) {
		case EMPTY:
			break;
		case BLUE:
		case BLUE_DETECTED:
			label.setStyle("-fx-background-color: #0000ff;");
			break;
		case RED:
		case RED_DETECTED:
			label.setStyle("-fx-background-color: #ff0000;");
			break;
		case BLACK:
		case BLACK_DETECTED:
			label.setStyle("-fx-background-color: #f2f2f2;");
			break;
		default:
			break;
		}
	}
	

}
