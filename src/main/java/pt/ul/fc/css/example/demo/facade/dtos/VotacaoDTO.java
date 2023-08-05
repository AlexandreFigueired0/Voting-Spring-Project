package pt.ul.fc.css.example.demo.facade.dtos;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;

public class VotacaoDTO {

  private long id;

  private int votosPositivos;

  private int votosNegativos;

  private String dataValidadeString;

  private EstadoValidade estado;

  private ProjetoDeLeiDTO projetoDeLei;

  public VotacaoDTO() {}

  public VotacaoDTO(Votacao votacao) {
    this.id = votacao.getId();
    this.votosNegativos = votacao.getVotosNegativos();
    this.votosPositivos = votacao.getVotosPositivos();
    DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    this.dataValidadeString = votacao.getDataValidade().format(dataFormatter);
    this.estado = votacao.getEstado();
    this.projetoDeLei = new ProjetoDeLeiDTO(votacao.getProjetoDeLei());
  }

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

  public EstadoValidade getEstado() {
    return estado;
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

  public void setEstado(EstadoValidade estado) {
    this.estado = estado;
  }

  public void setProjetoDeLei(ProjetoDeLeiDTO projetoDeLei) {
    this.projetoDeLei = projetoDeLei;
  }
}
