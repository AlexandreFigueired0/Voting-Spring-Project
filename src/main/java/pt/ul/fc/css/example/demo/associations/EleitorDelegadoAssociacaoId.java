package pt.ul.fc.css.example.demo.associations;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EleitorDelegadoAssociacaoId implements Serializable {

  private long temaId;
  private long eleitorId;

  public EleitorDelegadoAssociacaoId() {}

  public EleitorDelegadoAssociacaoId(long temaId, long eleitorId) {
    this.temaId = temaId;
    this.eleitorId = eleitorId;
  }

  public long getTemaId() {
    return temaId;
  }

  public void setTemaId(long tema) {
    this.temaId = tema;
  }

  public long getEleitorId() {
    return eleitorId;
  }

  public void setEleitorId(long eleitorId) {
    this.eleitorId = eleitorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EleitorDelegadoAssociacaoId that = (EleitorDelegadoAssociacaoId) o;
    return eleitorId == that.eleitorId && temaId == that.temaId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(temaId, eleitorId);
  }
}
