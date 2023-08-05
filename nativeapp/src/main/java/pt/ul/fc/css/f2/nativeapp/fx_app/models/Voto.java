package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;

public class Voto {

  private BooleanProperty valorVoto = new SimpleBooleanProperty();
  private LongProperty eleitorId = new SimpleLongProperty();
  private LongProperty votacaoId = new SimpleLongProperty();

  public BooleanProperty valorVotoProperty() {
    return this.valorVoto;
  }

  public LongProperty eleitorIdProperty() {
    return this.eleitorId;
  }

  public LongProperty votacaoIdProperty() {
    return this.votacaoId;
  }

  public boolean getValorVoto() {
    return valorVoto.get();
  }

  public long getEleitorId() {
    return eleitorId.get();
  }

  public long getVotacaoId() {
    return votacaoId.get();
  }

  public void setValorVoto(boolean valorVoto) {
    this.valorVoto.set(valorVoto);
  }

  public void setEleitorId(long eleitorId) {
    this.eleitorId.set(eleitorId);
  }

  public void setVotacaoId(long votacaoId) {
    this.votacaoId.set(votacaoId);
  }

  public Voto(boolean valorVoto, Long eleitorId, Long votacaoId) {
    setValorVoto(valorVoto);
    setEleitorId(eleitorId);
    setVotacaoId(votacaoId);
  }
}
