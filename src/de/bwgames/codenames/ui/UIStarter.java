package de.bwgames.codenames.ui;

import java.nio.file.Paths;

import de.bwgames.codenames.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIStarter extends Application{
	
    protected double xOffset;
	protected double yOffset;


	@Override
    public void start(Stage primaryStage) throws Exception{
    	Game game = StartDialog.createGame(primaryStage);
    	if(game == null) {
    		System.exit(0);
    	}
        FXMLLoader fxmlMainView = new FXMLLoader();
        Parent mainView = fxmlMainView.load(Paths.get("C:\\dev\\codenames\\Codenames\\codenames.fxml").toUri().toURL().openStream());
        MainViewController mainViewController = (MainViewController) fxmlMainView.getController();
        mainViewController.start(game);
        
        FXMLLoader fxmlOverview = new FXMLLoader();
        AnchorPane overview = fxmlOverview.load(Paths.get("C:\\dev\\codenames\\Codenames\\overview.fxml").toUri().toURL().openStream());
        OverviewController overviewController = (OverviewController) fxmlOverview.getController();
        overviewController.start(game);
        
        Stage dialog = new Stage(StageStyle.UNIFIED);
        dialog.setOnCloseRequest(e->e.consume());
        dialog.setScene(new Scene(overview, 1200, 800));
        dialog.show();
        overview.setOnMousePressed(event -> {
		    xOffset = dialog.getX() - event.getScreenX();
		    yOffset = dialog.getY() - event.getScreenY();
		});
        overview.setOnMouseDragged(event -> {
			dialog.setX(event.getScreenX() + xOffset);
			dialog.setY(event.getScreenY() + yOffset);
		});
        
        primaryStage.setTitle("Codenames");
        primaryStage.setScene(new Scene(mainView, 1200, 800));
        primaryStage.show();
        primaryStage.setOnCloseRequest(ev->System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
