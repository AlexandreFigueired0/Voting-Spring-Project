package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class Controller {
  private String prefix = "/fxml/";

  public abstract void initController();

  protected FXMLLoader goToScene(ActionEvent event, String fxmlName) throws IOException {

    BorderPane newRoot = new BorderPane();

    FXMLLoader pageLoader = new FXMLLoader(getClass().getResource(prefix + fxmlName + ".fxml"));
    newRoot.setCenter(pageLoader.load());

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    Scene scene = new Scene(newRoot, 800, 600);
    stage.setScene(scene);
    stage.show();

    return pageLoader;
  }

  @FXML
  public void goToUseCase(ActionEvent event) throws IOException {
    String view = ((Node) event.getSource()).getId();
    FXMLLoader pageLoader = goToScene(event, view);
    Controller controller = pageLoader.getController();
    controller.initController();
  }
}
