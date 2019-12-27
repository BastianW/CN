/**
 * 
 */
package de.bwgames.codenames.ui;

import de.bwgames.codenames.Game;
import de.bwgames.codenames.GameListener;
import de.bwgames.codenames.WordState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author Bastian
 *
 */
public class OverviewController implements GameListener{
	
	private static final Font FONT = new Font("Cambria", 24);
	@FXML
	private AnchorPane anchorpane;
	@FXML
	private GridPane overview; 
	private Label[] labels;
	private Line[][] lines; 
	
	public void initialize() {
		
	}
	
	public void start(Game game) {
		labels = new Label[game.getGameSize()];
		lines = new Line[labels.length][2];
		createGameBoard(game);
		game.register(this);
	}
	
	@Override
	public void update(GameContext ctx) {
		for (int i = 0; i < labels.length; i++) {
			Label l = labels[i];
			setStyleToLabel(ctx.getGame(), l);
		}
	}

	private void createGameBoard(Game game) {
		String[] words = game.getWords();
		int colums = (int) Math.sqrt(game.getGameSize());
		for (int i = 0; i < game.getGameSize(); i++) {
			AnchorPane ancherPane = new AnchorPane();
			Label label = new Label();
			labels[i]=label;
			String word = words[i];
			label.setId(i + "");
			label.setText(word);
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMaxHeight(Double.MAX_VALUE);
			label.setTextAlignment(TextAlignment.CENTER);
			label.setAlignment(Pos.CENTER);
			label.setFont(FONT);
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
		case BLACK:
		case BLUE:
		case EMPTY:
		case RED:
			label.setBackground(Colors.WHITE);
			break;
		case EMPTY_DETECTED:
			setCross(i, label);
			break;
		case BLUE_DETECTED:
			label.setBackground(Colors.BLUE);
			break;
		case RED_DETECTED:
			label.setBackground(Colors.RED);
			break;
		case BLACK_DETECTED:
			label.setBackground(Colors.BLACK);
			break;
		default:
			break;
		}
	}

	private void setCross(int i, Label l) {
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


}
