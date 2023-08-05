package pt.ul.fc.css.f2.nativeapp.fx_app.dtos;

import java.util.Objects;

public class VotacaoDTO {

  private long id;

  private int votosPositivos;

  private int votosNegativos;

  private String dataValidadeString;

  private String estado;

  private ProjetoDeLeiDTO projetoDeLei;

  public VotacaoDTO() {}

  public long getId() {
    return id;
  }

  public int getVotosPositivos() {
    return votosPositivos;
  }

  public int getVotosNegativos() {
    return votosNegativos;
  }

  public String getDataValidadeString() {
    return dataValidadeString;
  }

  public ProjetoDeLeiDTO getProjetoDeLei() {
    return projetoDeLei;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VotacaoDTO that = (VotacaoDTO) o;
    return id == that.id && Objects.equals(projetoDeLei, that.projetoDeLei);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projetoDeLei);
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setVotosPositivos(int votosPositivos) {
    this.votosPositivos = votosPositivos;
  }

  public void setVotosNegativos(int votosNegativos) {
    this.votosNegativos = votosNegativos;
  }

  public void setDataValidadeString(String dataValidadeString) {
    this.dataValidadeString = dataValidadeString;
  }

  public void setProjetoDeLei(ProjetoDeLeiDTO projetoDeLei) {
    this.projetoDeLei = projetoDeLei;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }
}
