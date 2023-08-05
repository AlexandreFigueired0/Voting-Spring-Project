package pt.ul.fc.css.f2.nativeapp.fx_app.dtos;

public class VotoDTO {

  private Boolean valorVoto;
  private long eleitorId;

  private String eleitorCC;

  private long votacaoId;

  public VotoDTO() {}

  public String getEleitorCC() {
    return eleitorCC;
  }

  public void setEleitorCC(String eleitorCC) {
    this.eleitorCC = eleitorCC;
  }

  public void setEleitorId(long eleitorId) {
    this.eleitorId = eleitorId;
  }

  public void setVotacaoId(long votacaoId) {
    this.votacaoId = votacaoId;
  }

  public void setValorVoto(boolean valor) {
    this.valorVoto = valor;
  }

  public Boolean getValorVoto() {
    return this.valorVoto;
  }

  public long getEleitorId() {
    return this.eleitorId;
  }

  public long getVotacaoId() {
    return this.votacaoId;
  }
}
