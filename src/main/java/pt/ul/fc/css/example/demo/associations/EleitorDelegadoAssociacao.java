package pt.ul.fc.css.example.demo.associations;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;

@Entity
public class EleitorDelegadoAssociacao {

  @EmbeddedId private EleitorDelegadoAssociacaoId id = new EleitorDelegadoAssociacaoId();

  @ManyToOne private Delegado delegado;

  @ManyToOne
  @MapsId("eleitorId")
  private Eleitor eleitor;

  @ManyToOne
  @MapsId("temaId")
  private Tema tema;

  public EleitorDelegadoAssociacao() {}

  public EleitorDelegadoAssociacao(Delegado delegado, Eleitor eleitor, Tema tema) {
    this.delegado = delegado;
    this.eleitor = eleitor;
    this.tema = tema;
  }

  public EleitorDelegadoAssociacaoId getId() {
    return id;
  }

  public void setId(EleitorDelegadoAssociacaoId id) {
    this.id = id;
  }

  public Delegado getDelegado() {
    return delegado;
  }

  public void setDelegado(Delegado delegado) {
    this.delegado = delegado;
  }

  public Eleitor getEleitor() {
    return eleitor;
  }

  public void setEleitor(Eleitor eleitor) {
    this.eleitor = eleitor;
  }
}
