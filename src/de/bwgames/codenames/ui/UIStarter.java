package de.bwgames.codenames.ui;

import java.net.URI;
import java.nio.file.Paths;

import de.bwgames.codenames.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UIStarter extends Application{
	
    @Override
    public void start(Stage primaryStage) throws Exception{
		URI uri = Paths.get("C:\\dev\\codenames\\Codenames\\test").toUri();
		Game game = new Game(0, uri);
    	
    	
        FXMLLoader fxmlMainView = new FXMLLoader();
        Parent mainView = fxmlMainView.load(Paths.get("C:\\dev\\codenames\\Codenames\\codenames.fxml").toUri().toURL().openStream());
        MainViewController mainViewController = (MainViewController) fxmlMainView.getController();
        mainViewController.start(game);
        
        FXMLLoader fxmlOverview = new FXMLLoader();
        AnchorPane overview = fxmlOverview.load(Paths.get("C:\\dev\\codenames\\Codenames\\overview.fxml").toUri().toURL().openStream());
        OverviewController overviewController = (OverviewController) fxmlOverview.getController();
        overviewController.start(game);
        
        Stage dialog = new Stage();
        dialog.setScene(new Scene(overview, 800, 500));
        dialog.show();
        
        
        primaryStage.setTitle("Codenames");
        primaryStage.setScene(new Scene(mainView, 800, 500));
        primaryStage.show();
        
        
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
