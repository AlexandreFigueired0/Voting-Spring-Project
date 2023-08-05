package pt.ul.fc.css.f2.nativeapp.fx_app.models;

import javafx.beans.property.*;

public class ProjetoDeLei {

  private LongProperty id = new SimpleLongProperty();
  private StringProperty titulo = new SimpleStringProperty();
  private StringProperty tema = new SimpleStringProperty();
  private StringProperty detalhes = new SimpleStringProperty();

  public long getId() {
    return id.get();
  }

  public LongProperty idProperty() {
    return id;
  }

  public void setId(long id) {
    this.id.set(id);
  }

  public String getTitulo() {
    return titulo.get();
  }

  public StringProperty tituloProperty() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo.set(titulo);
  }

  public String getTema() {
    return tema.get();
  }

  public StringProperty temaProperty() {
    return tema;
  }

  public void setTema(String tema) {
    this.tema.set(tema);
  }

  public String getDetalhes() {
    return detalhes.get();
  }

  public StringProperty detalhesProperty() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes.set(detalhes);
  }

  public ProjetoDeLei(Long id, String titulo, String tema, String detalhes) {
    setId(id);
    setTitulo(titulo);
    setTema(tema);
    setDetalhes(detalhes);
  }
}
