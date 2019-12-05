/**
 * 
 */
package de.bwgames.codenames.ui;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

import de.bwgames.codenames.Game;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * @author Bastian
 *
 */
public final class StartDialog {
	private static final String DEFAULT_PATH = new File("").getAbsolutePath();

	public static Game createGame(Stage primaryStage) {
		// Create the custom dialog.
		Dialog<Pair<Long, URI>> dialog = new Dialog<>();
		dialog.setTitle("Start Dialog");
		dialog.setHeaderText("Wähle Repo und Schlüssel");

		// Set the button types.
		ButtonType playButtonType = new ButtonType("Spielen", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(playButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField spinner = new TextField();
		spinner.setPromptText("RandomKey");
		spinner.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				spinner.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		spinner.setText(new Random().nextInt()+"");

		TextField dataPath = new TextField();
		dataPath.setText(DEFAULT_PATH + "\\test");
		Button fileChooserOpener = new Button("...");
		fileChooserOpener.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Wähle Wortrepo");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				dataPath.setText(file.getAbsolutePath());
			}
		});

		grid.add(new Label("RandomKey:"), 0, 0);
		grid.add(spinner, 1, 0, 2, 1);
		grid.add(new Label("File:"), 0, 1);
		grid.add(dataPath, 1, 1);
		grid.add(fileChooserOpener, 2, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(playButtonType);

		// Do some validation (using the Java 8 lambda syntax).
		dataPath.textProperty().addListener((observable, oldValue, newValue) -> {
			File file = new File(newValue);
			loginButton.setDisable(!file.exists() || file.isDirectory());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> dataPath.requestFocus());

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == playButtonType) {
				return new Pair<>(Long.parseLong(spinner.getText()), Paths.get(dataPath.getText()).toUri());
			}
			return null;
		});

		Optional<Pair<Long, URI>> result = dialog.showAndWait();

		if (result.isPresent()) {
			System.out.println("Key=" + result.get().getKey() + ", Path=" + result.get().getValue());
			return new Game(result.get().getKey(), result.get().getValue());
		}
		return null;
	}
}
