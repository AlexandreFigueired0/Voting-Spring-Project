package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.lang.NonNull;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.Voto;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_eleitor")
public class Eleitor {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String nome;

  @NonNull
  @Column(nullable = false, unique = true)
  private String cc;

  @Column(nullable = false, unique = true)
  private String token;

  @OneToMany(mappedBy = "eleitor")
  private Set<EleitorDelegadoAssociacao> delegados;

  @ManyToMany(mappedBy = "apoiantes", fetch = FetchType.EAGER)
  private Set<ProjetoDeLei> projetosDeLeiApoiados;

  @OneToMany(mappedBy = "eleitor")
  private Set<Voto> votos;

  public Eleitor() {}

  public Eleitor(
      String nome,
      @NonNull String cc,
      String token,
      Set<EleitorDelegadoAssociacao> delegados,
      Set<ProjetoDeLei> projetosDeLei,
      Set<Voto> votos) {
    this.nome = nome;
    this.cc = cc;
    this.token = token;
    this.delegados = delegados;
    this.projetosDeLeiApoiados = projetosDeLei;
    this.votos = votos;
  }

  public Eleitor(String nome, String cc, String token) {
    this.nome = nome;
    this.cc = cc;
    this.token = token;
    this.delegados = new HashSet<>();
    this.projetosDeLeiApoiados = new HashSet<>();
    this.votos = new HashSet<>();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  @NonNull
  public String getCc() {
    return cc;
  }

  public void setCc(@NonNull String cc) {
    this.cc = cc;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Set<EleitorDelegadoAssociacao> getDelegados() {
    return delegados;
  }

  public void setDelegados(Set<EleitorDelegadoAssociacao> delegados) {
    this.delegados = delegados;
  }

  public Set<ProjetoDeLei> getProjetosDeLeiApoiados() {
    return projetosDeLeiApoiados;
  }

  public void setProjetosDeLeiApoiados(Set<ProjetoDeLei> projetosDeLei) {
    this.projetosDeLeiApoiados = projetosDeLei;
  }

  public Set<Voto> getVotos() {
    return votos;
  }

  public void setVotos(Set<Voto> votos) {
    this.votos = votos;
  }

  public Voto setupVoto(Voto voto) {
    voto.setValorVoto(null);
    return voto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Eleitor eleitor = (Eleitor) o;
    return id == eleitor.id
        && Objects.equals(nome, eleitor.nome)
        && Objects.equals(cc, eleitor.cc)
        && Objects.equals(token, eleitor.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, cc, token);
  }
}
