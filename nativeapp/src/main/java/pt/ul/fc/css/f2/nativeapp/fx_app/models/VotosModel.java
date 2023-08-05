package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import pt.ul.fc.css.f2.nativeapp.fx_app.dtos.VotoDTO;

public class VotosModel {

  private static VotosModel INSTANCE = new VotosModel();

  private Voto currentVoto;

  private VotosModel() {}

  public static VotosModel getInstance() {
    return INSTANCE;
  }

  public void setVoto(String eleitorCC, String votacaoId) throws IOException {
    URL url =
        new URL("http://localhost:8080/api/votacoes/voto-omissao/" + eleitorCC + "/" + votacaoId);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    if (con.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
      this.currentVoto = null;
      return;
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    String jsonResponse = response.toString();
    ObjectMapper mapper = new ObjectMapper();
    VotoDTO votosAnswer = mapper.readValue(jsonResponse, new TypeReference<VotoDTO>() {});

    this.currentVoto =
        new Voto(
            votosAnswer.getValorVoto(), votosAnswer.getEleitorId(), votosAnswer.getVotacaoId());
  }

  public Voto getCurrentVoto() {
    return this.currentVoto;
  }
}
