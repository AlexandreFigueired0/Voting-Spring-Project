package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;

@Entity
public class Tema {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(unique = true, nullable = false)
  private String nome;

  @ManyToOne private Tema temaPai;

  @OneToMany(mappedBy = "tema")
  private Set<EleitorDelegadoAssociacao> delegadosDeEleitores;

  public Tema() {}

  public Tema(String nome, Tema temaPai, Set<EleitorDelegadoAssociacao> delegadosDeEleitores) {
    this.nome = nome;
    this.temaPai = temaPai;
    this.delegadosDeEleitores = delegadosDeEleitores;
  }

  public Tema(String nome, Tema temaPai) {
    this.nome = nome;
    this.temaPai = temaPai;
    this.delegadosDeEleitores = new HashSet<>();
  }

  public Tema(String nome) {
    this.nome = nome;
    this.delegadosDeEleitores = new HashSet<>();
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

  public Tema getTemaPai() {
    return temaPai;
  }

  public void setTemaPai(Tema temaPai) {
    this.temaPai = temaPai;
  }

  public Set<EleitorDelegadoAssociacao> getDelegadosDeEleitores() {
    return delegadosDeEleitores;
  }

  public void setDelegadosDeEleitores(Set<EleitorDelegadoAssociacao> delegadosDeEleitores) {
    this.delegadosDeEleitores = delegadosDeEleitores;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tema tema = (Tema) o;
    return id == tema.id && temaPai == tema.temaPai && Objects.equals(nome, tema.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, temaPai);
  }
}
