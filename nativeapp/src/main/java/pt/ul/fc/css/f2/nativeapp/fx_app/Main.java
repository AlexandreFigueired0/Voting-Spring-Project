package pt.ul.fc.css.f2.nativeapp.fx_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.css.f2.nativeapp.fx_app.controllers.MainController;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    String prefix = "/fxml/";
    BorderPane root = new BorderPane();

    FXMLLoader mainPageLoader = new FXMLLoader(getClass().getResource(prefix + "main-page.fxml"));
    root.setCenter(mainPageLoader.load());
    Scene scene = new Scene(root, 800, 600);

    MainController mainController = mainPageLoader.getController();

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}
