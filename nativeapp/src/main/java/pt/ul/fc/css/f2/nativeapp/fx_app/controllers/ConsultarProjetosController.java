package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.ProjetoDeLei;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.ProjetosDeLeiModel;

public class ConsultarProjetosController extends Controller {

  @FXML private TableView<ProjetoDeLei> tableView;
  @FXML private TableColumn<ProjetoDeLei, Long> id;
  @FXML private TableColumn<ProjetoDeLei, String> projetoDeLeiTitulo;
  @FXML private TableColumn<ProjetoDeLei, String> tema;
  @FXML private TableColumn<ProjetoDeLei, String> detalhes;

  public void initController() {
    ProjetosDeLeiModel pdlm = new ProjetosDeLeiModel();

    this.id.setCellValueFactory(new PropertyValueFactory<ProjetoDeLei, Long>("id"));
    this.projetoDeLeiTitulo.setCellValueFactory(
        new PropertyValueFactory<ProjetoDeLei, String>("titulo"));
    this.tema.setCellValueFactory(new PropertyValueFactory<ProjetoDeLei, String>("tema"));
    detalhes.setCellFactory(
        new Callback<TableColumn<ProjetoDeLei, String>, TableCell<ProjetoDeLei, String>>() {
          @Override
          public TableCell<ProjetoDeLei, String> call(TableColumn<ProjetoDeLei, String> coluna) {
            return new TableCell<ProjetoDeLei, String>() {
              @Override
              protected void updateItem(String link, boolean empty) {
                super.updateItem(link, empty);

                if (link == null || empty) {
                  setText(null);
                  setGraphic(null);
                } else {
                  Hyperlink hyperlink = new Hyperlink("Detalhes");
                  hyperlink.setOnAction(
                      event -> {
                        ProjetoDeLei projeto = getTableView().getItems().get(getIndex());
                        String view = "projeto-detalhes";
                        FXMLLoader pageLoader = null;
                        try {
                          pageLoader = goToScene(event, view);
                        } catch (IOException e) {
                          throw new RuntimeException(e);
                        }
                        ProjetoDetalhesController controller = pageLoader.getController();
                        controller.initModel(projeto.getId());
                        controller.initController();
                      });
                  setGraphic(hyperlink);
                }
              }
            };
          }
        });

    detalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
    try {
      tableView.setItems(pdlm.getProjetosList());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
