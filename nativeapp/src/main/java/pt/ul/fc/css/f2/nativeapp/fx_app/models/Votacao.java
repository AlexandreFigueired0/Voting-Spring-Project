package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import javafx.beans.property.*;

public class Votacao {

  private LongProperty id = new SimpleLongProperty();
  private StringProperty tituloProjetoDeLei = new SimpleStringProperty();
  private IntegerProperty votosNegativos = new SimpleIntegerProperty();
  private IntegerProperty votosPositivos = new SimpleIntegerProperty();
  private StringProperty dataValidade = new SimpleStringProperty();

  public final LongProperty idProperty() {
    return this.id;
  }

  public final StringProperty projetoDeLeiTituloProperty() {
    return this.tituloProjetoDeLei;
  }

  public final IntegerProperty getVotosNegativosProperty() {
    return this.votosNegativos;
  }

  public final IntegerProperty getVotosPositivosProperty() {
    return this.votosPositivos;
  }

  public final StringProperty dataValidadeProperty() {
    return this.dataValidade;
  }

  public long getId() {
    return id.get();
  }

  public String getTituloProjetoDeLei() {
    return tituloProjetoDeLei.get();
  }

  public StringProperty tituloProjetoDeLeiProperty() {
    return tituloProjetoDeLei;
  }

  public int getVotosNegativos() {
    return votosNegativos.get();
  }

  public IntegerProperty votosNegativosProperty() {
    return votosNegativos;
  }

  public int getVotosPositivos() {
    return votosPositivos.get();
  }

  public IntegerProperty votosPositivosProperty() {
    return votosPositivos;
  }

  public String getDataValidade() {
    return dataValidade.get();
  }

  public void setId(long id) {
    this.id.set(id);
  }

  public void setTituloProjetoDeLei(String tituloProjetoDeLei) {
    this.tituloProjetoDeLei.set(tituloProjetoDeLei);
  }

  public void setVotosNegativos(int votosNegativos) {
    this.votosNegativos.set(votosNegativos);
  }

  public void setVotosPositivos(int votosPositivos) {
    this.votosPositivos.set(votosPositivos);
  }

  public void setDataValidade(String dataValidade) {
    this.dataValidade.set(dataValidade);
  }

  public Votacao(
      Long id,
      String tituloProjetoDeLei,
      int votosNegativos,
      int votosPositivos,
      String dataValidade) {
    setId(id);
    setTituloProjetoDeLei(tituloProjetoDeLei);
    setVotosNegativos(votosNegativos);
    setVotosPositivos(votosPositivos);
    setDataValidade(dataValidade);
  }
}
