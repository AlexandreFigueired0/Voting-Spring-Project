package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.VotosModel;

public class VotoOmissaoFormsController extends Controller {

  @FXML TextField votacaoIdField;

  @FXML TextField eleitorCCField;

  @FXML Text mensagem;

  @Override
  public void initController() {
    mensagem.setVisible(false);
  }

  @FXML
  public void obterVoto(ActionEvent event) throws IOException {
    mensagem.setVisible(true);
    VotosModel vm = VotosModel.getInstance();

    try {
      Long.parseLong(votacaoIdField.getText());
    } catch (NumberFormatException e) {
      this.mensagem.setText("Por favor insira um número inteiro no campo votacaoId.");
      return;
    }

    if (eleitorCCField.getText().isEmpty() || votacaoIdField.getText().isEmpty()) {
      this.mensagem.setText("Todos os campos precisam de ser preenchidos.");
      return;
    }

    vm.setVoto(this.eleitorCCField.getText(), this.votacaoIdField.getText());

    goToUseCase(event);
  }
}
