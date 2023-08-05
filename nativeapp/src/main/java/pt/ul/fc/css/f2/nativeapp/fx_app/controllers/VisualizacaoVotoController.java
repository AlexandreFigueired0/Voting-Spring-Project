package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.Voto;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.VotosModel;

public class VisualizacaoVotoController extends Controller {

  @FXML private Text valorVisualizado;

  @FXML private Text idVotacaoVisualizado;

  @Override
  public void initController() {
    VotosModel vm = VotosModel.getInstance();
    Voto voto = vm.getCurrentVoto();

    if (voto == null) {
      this.idVotacaoVisualizado.setText("O voto que procurou nao existe");
      this.valorVisualizado.setText("");
      return;
    }

    this.idVotacaoVisualizado.setText("O seu voto para a votacao com id " + voto.getVotacaoId());
    this.valorVisualizado.setText(voto.getValorVoto() ? "Aprovado" : "Rejeitado");
  }
}
