package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.Votacao;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.VotacoesModel;

public class ListarVotacoesController extends Controller {

  @FXML private TableView<Votacao> table;
  @FXML private TableColumn<Votacao, Long> id;
  @FXML private TableColumn<Votacao, String> projetoDeLeiTitulo;
  @FXML private TableColumn<Votacao, Integer> votosNegativos;
  @FXML private TableColumn<Votacao, Integer> votosPositivos;
  @FXML private TableColumn<Votacao, String> dataValidade;

  public void initController() {
    VotacoesModel vm = new VotacoesModel();

    this.id.setCellValueFactory(new PropertyValueFactory<Votacao, Long>("id"));
    this.projetoDeLeiTitulo.setCellValueFactory(
        new PropertyValueFactory<Votacao, String>("projetoDeLeiTitulo"));
    this.votosNegativos.setCellValueFactory(
        new PropertyValueFactory<Votacao, Integer>("votosNegativos"));
    this.votosPositivos.setCellValueFactory(
        new PropertyValueFactory<Votacao, Integer>("votosPositivos"));
    this.dataValidade.setCellValueFactory(
        new PropertyValueFactory<Votacao, String>("dataValidade"));

    try {
      table.setItems(vm.getVotacaoList());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
