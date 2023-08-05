package pt.ul.fc.css.f2.nativeapp.fx_app.dtos;

import java.util.Objects;

public class ProjetoDeLeiDTO {

  private long id;

  private String titulo;

  private String descricao;

  private String tema;

  private long idDelegadoProponente;

  private String ccDelegadoProponente;

  private String dataValidadeString;

  private int nApoios;

  private byte[] anexoPdf;

  public ProjetoDeLeiDTO() {}

  public long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public void setTema(String tema) {
    this.tema = tema;
  }

  public void setIdDelegadoProponente(long idDelegadoProponente) {
    this.idDelegadoProponente = idDelegadoProponente;
  }

  public String getDataValidadeString() {
    return dataValidadeString;
  }

  public String getCcDelegadoProponente() {
    return ccDelegadoProponente;
  }

  public void setCcDelegadoProponente(String ccDelegadoProponente) {
    this.ccDelegadoProponente = ccDelegadoProponente;
  }

  public void setDataValidadeString(String dataValidadeString) {
    this.dataValidadeString = dataValidadeString;
  }

  public void setnApoios(int nApoios) {
    this.nApoios = nApoios;
  }

  public byte[] getAnexoPdf() {
    return anexoPdf;
  }

  public void setAnexoPdf(byte[] anexoPdf) {
    this.anexoPdf = anexoPdf;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getTema() {
    return tema;
  }

  public long getIdDelegadoProponente() {
    return idDelegadoProponente;
  }

  public int getnApoios() {
    return nApoios;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjetoDeLeiDTO that = (ProjetoDeLeiDTO) o;
    return id == that.id
        && Objects.equals(titulo, that.titulo)
        && Objects.equals(descricao, that.descricao)
        && Objects.equals(tema, that.tema)
        && Objects.equals(idDelegadoProponente, that.idDelegadoProponente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, titulo, descricao, tema, idDelegadoProponente);
  }
}
