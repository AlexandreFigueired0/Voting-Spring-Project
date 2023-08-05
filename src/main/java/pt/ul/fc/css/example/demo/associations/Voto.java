package pt.ul.fc.css.example.demo.associations;

import jakarta.persistence.*;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Votacao;

@Entity
public class Voto {

  @EmbeddedId private VotoId id = new VotoId();

  private Boolean valorVoto;

  @ManyToOne
  @MapsId("eleitorId")
  private Eleitor eleitor;

  @ManyToOne
  @MapsId("votacaoId")
  private Votacao votacao;

  public Voto() {}

  public Voto(Boolean valorVoto, Eleitor eleitor, Votacao votacao) {
    this.valorVoto = valorVoto;
    this.eleitor = eleitor;
    this.votacao = votacao;
  }

  public VotoId getId() {
    return id;
  }

  public void setId(VotoId id) {
    this.id = id;
  }

  public Boolean isValorVoto() {
    return valorVoto;
  }

  public void setValorVoto(Boolean valorVoto) {
    this.valorVoto = valorVoto;
  }

  public Eleitor getEleitor() {
    return eleitor;
  }

  public void setEleitor(Eleitor eleitor) {
    this.eleitor = eleitor;
  }

  public Votacao getVotacao() {
    return votacao;
  }

  public void setVotacao(Votacao votacao) {
    this.votacao = votacao;
  }
}
