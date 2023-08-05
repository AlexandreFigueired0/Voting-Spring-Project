package pt.ul.fc.css.example.demo.associations;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VotoId implements Serializable {

  private long eleitorId;
  private long votacaoId;

  public VotoId() {}

  public VotoId(long eleitorId, long votacaoId) {
    this.eleitorId = eleitorId;
    this.votacaoId = votacaoId;
  }

  public long getEleitorId() {
    return eleitorId;
  }

  public void setEleitorId(long eleitorId) {
    this.eleitorId = eleitorId;
  }

  public long getVotacaoId() {
    return votacaoId;
  }

  public void setVotacaoId(long votacaoId) {
    this.votacaoId = votacaoId;
  }
}
