package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.f2.nativeapp.fx_app.dtos.VotacaoDTO;

public class VotacoesModel {

  private final ObservableList<Votacao> votacaoList =
      FXCollections.observableArrayList(
          votacao ->
              new Observable[] {
                votacao.idProperty(),
                votacao.projetoDeLeiTituloProperty(),
                votacao.votosNegativosProperty(),
                votacao.votosPositivosProperty(),
                votacao.dataValidadeProperty()
              });

  public ObservableList<Votacao> getVotacaoList() throws IOException {
    URL url = new URL("http://localhost:8080/api/votacoes/votacoes-em-curso");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    String jsonResponse = response.toString();
    ObjectMapper mapper = new ObjectMapper();
    List<VotacaoDTO> l = new ArrayList<>();
    List<VotacaoDTO> projetosAnswer =
        mapper.readValue(jsonResponse, new TypeReference<List<VotacaoDTO>>() {});

    votacaoList.setAll(
        projetosAnswer
            .stream()
            .map(
                votDTO ->
                    new Votacao(
                        votDTO.getId(),
                        votDTO.getProjetoDeLei().getTitulo(),
                        votDTO.getVotosNegativos(),
                        votDTO.getVotosPositivos(),
                        votDTO.getDataValidadeString()))
            .collect(Collectors.toList()));

    return votacaoList;
  }
}
