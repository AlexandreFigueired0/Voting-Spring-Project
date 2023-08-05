package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.ProjetosDeLeiModel;

public class ApoiaProjetoController extends Controller {

  @FXML TextField eleitorCCInput;

  @FXML TextField projetoIdInput;

  @FXML Text mensagem;

  ProjetosDeLeiModel projetosDeLeiModel;

  @Override
  public void initController() {
    this.projetosDeLeiModel = new ProjetosDeLeiModel();
  }

  public void apoia() {
    if (eleitorCCInput.getText().isEmpty() || projetoIdInput.getText().isEmpty()) {
      this.mensagem.setText("Todos os campos precisam de ser preenchidos.");
      return;
    }

    long projetoId;

    try {
      projetoId = Long.parseLong(this.projetoIdInput.getText());
    } catch (NumberFormatException e) {
      this.mensagem.setText("Por favor insira um número inteiro no campo votacaoId.");
      return;
    }

    String eleitorId = this.eleitorCCInput.getText();
    int status = 0;
    try {
      status = this.projetosDeLeiModel.apoiaProjeto(eleitorId, projetoId);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    if (status == 200) {
      this.mensagem.setText("Projeto de Lei apoiado com sucesso.");
    } else if (status == 404) {
      this.mensagem.setText("ProjetoId e/ou EleitorCC não existem.");
    } else if (status == 409) {
      this.mensagem.setText("O eleitor já apoiou este Projeto de Lei");
    }
  }
}
