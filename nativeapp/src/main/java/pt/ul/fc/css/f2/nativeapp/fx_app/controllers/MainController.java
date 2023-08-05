package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class MainController extends Controller {

  private String prefix = "/fxml/";

  @FXML
  public void goToUseCase(ActionEvent event) throws IOException {
    String view = ((Node) event.getSource()).getId();
    FXMLLoader pageLoader = goToScene(event, view);
    Controller controller = pageLoader.getController();
    controller.initController();
  }

  @Override
  public void initController() {}
}
