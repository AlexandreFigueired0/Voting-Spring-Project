package pt.ul.fc.css.f2.nativeapp.fx_app.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import pt.ul.fc.css.f2.nativeapp.fx_app.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.f2.nativeapp.fx_app.models.ProjetosDeLeiModel;

public class ProjetoDetalhesController extends Controller {
  private Long projetoId;

  @FXML private Text idOutput;

  @FXML private Text tituloOutput;

  @FXML private Text temaOutput;

  @FXML private Text ccDelegadoOutput;

  @FXML private Text nApoiantesOutput;

  @FXML private Text dataValidadeOutput;

  @FXML private Text descricaoOutput;

  @FXML private Text mensagemDownload;

  public void initModel(long projetoId) {
    this.projetoId = projetoId;
  }

  @Override
  public void initController() {
    ProjetosDeLeiModel pdlm = new ProjetosDeLeiModel();
    ProjetoDeLeiDTO projetoDeLeiDTO = null;
    try {
      projetoDeLeiDTO = pdlm.getProjetoDTO(this.projetoId);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.mensagemDownload.setVisible(false);
    this.idOutput.setText(this.idOutput.getText() + " " + projetoDeLeiDTO.getId());
    this.tituloOutput.setText(this.tituloOutput.getText() + " " + projetoDeLeiDTO.getTitulo());
    this.temaOutput.setText(this.temaOutput.getText() + " " + projetoDeLeiDTO.getTema());
    this.ccDelegadoOutput.setText(
        this.ccDelegadoOutput.getText() + " " + projetoDeLeiDTO.getCcDelegadoProponente());
    this.nApoiantesOutput.setText(
        this.nApoiantesOutput.getText() + " " + projetoDeLeiDTO.getnApoios());
    this.dataValidadeOutput.setText(
        this.dataValidadeOutput.getText() + " " + projetoDeLeiDTO.getDataValidadeString());
    this.descricaoOutput.setText(
        this.descricaoOutput.getText() + " " + projetoDeLeiDTO.getDescricao());
  }

  @FXML
  public void downloadPDF() {
    ProjetosDeLeiModel pdlm = new ProjetosDeLeiModel();
    byte[] pdfFile = null;
    try {
      pdfFile = pdlm.getPdfBytes(this.projetoId);
      File outputFile = new File("downloaded.pdf");
      try (FileOutputStream fos = new FileOutputStream(outputFile)) {
        fos.write(pdfFile);
      }
    } catch (IOException e) {
      mensagemDownload.setText("Erro durante o download do PDF");
      this.mensagemDownload.setVisible(true);
      throw new RuntimeException(e);
    }
    mensagemDownload.setText("Download do PDF concluído");
    this.mensagemDownload.setVisible(true);
  }
}
