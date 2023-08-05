package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pt.ul.fc.css.f2.nativeapp.fx_app.dtos.VotoDTO;

public class VotarController extends Controller {

  @FXML TextField eleitorCCInput;

  @FXML TextField votacaoIdInput;
  @FXML ChoiceBox<String> valorVotoInput;
  @FXML Text votoMensagem;

  @Override
  public void initController() {
    this.votoMensagem.setVisible(false);
  }

  public void votar() throws IOException {
    this.votoMensagem.setVisible(true);

    if (eleitorCCInput.getText().isEmpty()
        || votacaoIdInput.getText().isEmpty()
        || valorVotoInput.getValue() == null) {
      this.votoMensagem.setText("Todos os campos precisam de ser preenchidos.");
      return;
    }

    boolean valorVotoBoolean = valorVotoInput.getValue().equals("Rejeita") ? false : true;

    URL url = new URL("http://localhost:8080/api/votacoes/votos");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    DataOutputStream out = new DataOutputStream(con.getOutputStream());

    VotoDTO voto = new VotoDTO();
    voto.setValorVoto(valorVotoBoolean);
    voto.setEleitorCC(this.eleitorCCInput.getText());
    try {
      voto.setVotacaoId(Long.parseLong(votacaoIdInput.getText()));
    } catch (NumberFormatException e) {
      this.votoMensagem.setText("Por favor insira um número inteiro no campo votacaoId.");
      return;
    }

    ObjectMapper mapper = new ObjectMapper();
    String request = mapper.writeValueAsString(voto);

    out.writeBytes(request);
    out.flush();
    out.close();

    int respondeCode = con.getResponseCode();

    if (respondeCode == HttpURLConnection.HTTP_OK)
      this.votoMensagem.setText("O seu voto foi registado com sucesso.");

    if (respondeCode == HttpURLConnection.HTTP_NOT_FOUND)
      this.votoMensagem.setText("VotacaoId e/ou EleitorCC não existem.");

    if (respondeCode == HttpURLConnection.HTTP_CONFLICT)
      this.votoMensagem.setText("O eleitor indicado ja votou nesta votacao.");
  }
}
