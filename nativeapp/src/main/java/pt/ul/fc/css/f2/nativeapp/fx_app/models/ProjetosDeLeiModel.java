package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.f2.nativeapp.fx_app.dtos.ProjetoDeLeiDTO;

public class ProjetosDeLeiModel {
  private static final String BASE_URL = "http://localhost:8080/api/projetos-de-lei/";
  private final ObservableList<ProjetoDeLei> projetoList =
      FXCollections.observableArrayList(
          proj ->
              new Observable[] {
                proj.idProperty(),
                proj.tituloProperty(),
                proj.temaProperty(),
                proj.detalhesProperty(),
              });

  public ObservableList<ProjetoDeLei> getProjetosList() throws IOException {
    URL url = new URL(BASE_URL + "projetos-de-lei-nao-expirados");
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
    List<ProjetoDeLeiDTO> projetosAnswer =
        mapper.readValue(jsonResponse, new TypeReference<List<ProjetoDeLeiDTO>>() {});
    projetoList.setAll(
        projetosAnswer
            .stream()
            .map(
                projDTO ->
                    new ProjetoDeLei(
                        projDTO.getId(), projDTO.getTitulo(), projDTO.getTema(), "Ver"))
            .collect(Collectors.toList()));

    return projetoList;
  }

  public ProjetoDeLeiDTO getProjetoDTO(Long projetoId) throws IOException {
    URL url = new URL(BASE_URL + projetoId);
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
    ProjetoDeLeiDTO projetoAnswer =
        mapper.readValue(jsonResponse, new TypeReference<ProjetoDeLeiDTO>() {});

    return projetoAnswer;
  }

  public int apoiaProjeto(String eleitorCC, Long projetoId)
      throws IOException, InterruptedException {
    String url = BASE_URL + "apoia/" + eleitorCC + "/" + projetoId;
    String patchPayload = "{\"field\": \"value\"}";

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .method("PATCH", HttpRequest.BodyPublishers.ofString(patchPayload))
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response.statusCode();
  }

  public byte[] getPdfBytes(long projetoId) throws IOException {
    URL url = new URL(BASE_URL + "download-pdf/" + projetoId);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    byte[] pdfBytes = null;
    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
      try (InputStream inputStream = new BufferedInputStream(con.getInputStream());
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        pdfBytes = outputStream.toByteArray();
      }
    }
    con.disconnect();
    return pdfBytes;
  }
}
