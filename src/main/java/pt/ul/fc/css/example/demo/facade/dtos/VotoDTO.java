package pt.ul.fc.css.example.demo.facade.dtos;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.associations.Voto;

@Component
public class VotoDTO {

  private Boolean valorVoto;
  private long eleitorId;

  private String eleitorCC;
  private long votacaoId;

  public VotoDTO() {}

  public VotoDTO(Voto voto) {
    this.valorVoto = voto.isValorVoto();
    this.eleitorId = voto.getEleitor().getId();
    this.votacaoId = voto.getVotacao().getId();
    this.eleitorCC = voto.getEleitor().getCc();
  }

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
