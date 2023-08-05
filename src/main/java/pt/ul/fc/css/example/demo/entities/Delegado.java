package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.Voto;

@Entity
public class Delegado extends Eleitor {

  @OneToMany(mappedBy = "delegado")
  private Set<EleitorDelegadoAssociacao> eleitores;

  @OneToMany(mappedBy = "delegadoProponente")
  private Set<ProjetoDeLei> projetoDeLeiPropostos;

  public Delegado() {}

  public Delegado(
      String nome,
      String cc,
      String token,
      Set<EleitorDelegadoAssociacao> delegados,
      Set<ProjetoDeLei> projetosDeLeiApoiados,
      Set<Voto> votos,
      Set<EleitorDelegadoAssociacao> eleitores,
      Set<ProjetoDeLei> projetoDeLeiPropostos) {
    super(nome, cc, token, delegados, projetosDeLeiApoiados, votos);
    this.eleitores = eleitores;
    this.projetoDeLeiPropostos = projetoDeLeiPropostos;
  }

  public Delegado(String nome, String cc, String token) {
    super(nome, cc, token);
    this.eleitores = new HashSet<>();
    this.projetoDeLeiPropostos = new HashSet<>();
  }

  public Set<EleitorDelegadoAssociacao> getEleitores() {
    return eleitores;
  }

  public void setEleitores(Set<EleitorDelegadoAssociacao> eleitores) {
    this.eleitores = eleitores;
  }

  public Set<ProjetoDeLei> getProjetoDeLeiPropostos() {
    return projetoDeLeiPropostos;
  }

  public void setProjetoDeLeiPropostos(Set<ProjetoDeLei> projetoDeLeiPropostos) {
    this.projetoDeLeiPropostos = projetoDeLeiPropostos;
  }

  @Override
  public Voto setupVoto(Voto voto) {
    return voto;
  }
}
