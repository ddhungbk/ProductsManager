package bk.hungd.App;

import bk.hungd.Controller.WelcomeController;
import bk.hungd.Controller.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppController {

	private Stage stage;
	private Scene scene;

	public AppController(Stage stage, Scene scene) {
		this.stage = stage;
		this.scene = scene;
	}

	public void authenticated(AppSession session) {
		showMainScene(session);
	}

	public void showMainScene(AppSession session) {
		try {
			stage.hide();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/MainScene.fxml"));
			Parent root = loader.load();

			MainController mainController = loader.<MainController>getController();
			mainController.initSession(this, session);
			
			scene.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
						Number newSceneWidth) {
					mainController.setSplitPaneDivider();
				}
			});

			scene.setRoot(root);
			scene.getStylesheets().add(getClass().getResource("/bk/hungd/Layout/application.css").toExternalForm());
			stage.setMaximized(true);
			stage.setResizable(true);
			stage.setTitle("Products Manager");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	public void showWelcomeScene(int type) {
		try {
			stage.hide();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/WelcomeScene.fxml"));
			Parent root = loader.load();
			WelcomeController loginController = loader.<WelcomeController>getController();
			loginController.login(this, type);
			
			scene.setRoot(root);
			scene.getStylesheets().add(getClass().getResource("/bk/hungd/Layout/application.css").toExternalForm());
			stage.setMaximized(false);
			stage.setResizable(false);
			stage.centerOnScreen();
			stage.setTitle("Welcome to BK");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	public void logout() {
		showWelcomeScene(WelcomeController.LOG_IN);
	}

	public void setPath() {
		showWelcomeScene(WelcomeController.SET_PATH);
	}
}
