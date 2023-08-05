package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.lang.NonNull;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;

@Entity
public class ProjetoDeLei {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @ManyToOne
  @JoinColumn(name = "delegado_id", nullable = false)
  private Delegado delegadoProponente;

  @NonNull
  @Column(nullable = false)
  private String titulo;

  @NonNull
  @Column(nullable = false)
  private String descricao;

  @NonNull
  @Column(nullable = false)
  @Lob
  private byte[] anexoPdf;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Eleitor> apoiantes;

  @NonNull
  @ManyToOne
  @JoinColumn(name = "tema_id", nullable = false)
  private Tema tema;

  @NonNull
  @Column(name = "data_validade", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime dataValidade;

  private int nApoios;

  @OneToOne(mappedBy = "projetoDeLei")
  private Votacao votacao;

  @Enumerated(EnumType.ORDINAL)
  private EstadoValidade estado;

  public ProjetoDeLei() {}

  public ProjetoDeLei(
      Delegado delegadoProponente,
      @NonNull String titulo,
      @NonNull String descricao,
      @NonNull byte[] anexoPdf,
      @NonNull Tema tema,
      @NonNull LocalDateTime dataValidade,
      int nApoios,
      Votacao votacao,
      EstadoValidade estadoValidade) {
    this.delegadoProponente = delegadoProponente;
    this.titulo = titulo;
    this.descricao = descricao;
    this.anexoPdf = anexoPdf;
    this.tema = tema;
    this.dataValidade = dataValidade;
    this.nApoios = nApoios;
    this.votacao = votacao;
    this.estado = estadoValidade;
    this.apoiantes = new HashSet<>();
  }

  public ProjetoDeLei(
      String titulo,
      String descricao,
      byte[] anexoPdf,
      Tema tema,
      LocalDateTime dataValidade,
      Delegado delegadoProponente) {
    this.tema = tema;
    this.titulo = titulo;
    this.descricao = descricao;
    this.anexoPdf = anexoPdf;
    this.dataValidade = dataValidade;
    this.delegadoProponente = delegadoProponente;
    this.nApoios = 1;
    this.estado = EstadoValidade.ABERTO;
    this.apoiantes = new HashSet<>();
  }

  public ProjetoDeLei(
      String titulo,
      String descricao,
      byte[] anexoPdf,
      Tema tema,
      LocalDateTime dataValidade,
      Delegado delegadoProponente,
      EstadoValidade estado) {
    this.tema = tema;
    this.titulo = titulo;
    this.descricao = descricao;
    this.anexoPdf = anexoPdf;
    this.dataValidade = dataValidade;
    this.delegadoProponente = delegadoProponente;
    this.nApoios = 1;
    this.estado = estado;
    this.apoiantes = new HashSet<>();
  }

  public ProjetoDeLei(
      String titulo,
      String descricao,
      byte[] anexoPdf,
      Tema tema,
      LocalDateTime dataValidade,
      Delegado delegadoProponente,
      int nApoios) {
    this.tema = tema;
    this.titulo = titulo;
    this.descricao = descricao;
    this.anexoPdf = anexoPdf;
    this.dataValidade = dataValidade;
    this.delegadoProponente = delegadoProponente;
    this.nApoios = nApoios;
    this.estado = EstadoValidade.ABERTO;
    this.apoiantes = new HashSet<>();
  }

  public EstadoValidade getEstado() {
    return estado;
  }

  public void setEstado(EstadoValidade estado) {
    this.estado = estado;
  }

  public Delegado getDelegadoProponente() {
    return delegadoProponente;
  }

  public void setDelegadoProponente(Delegado delegadoProponente) {
    this.delegadoProponente = delegadoProponente;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @NonNull
  public byte[] getAnexoPdf() {
    return anexoPdf;
  }

  public void setAnexoPdf(@NonNull byte[] anexoPdf) {
    this.anexoPdf = anexoPdf;
  }

  public Tema getTema() {
    return tema;
  }

  public void setTema(Tema tema) {
    this.tema = tema;
  }

  public LocalDateTime getDataValidade() {
    return dataValidade;
  }

  public void setDataValidade(LocalDateTime dataValidade) {
    this.dataValidade = dataValidade;
  }

  public int getnApoios() {
    return nApoios;
  }

  public Set<Eleitor> getApoiantes() {
    return apoiantes;
  }

  public void setApoiantes(Set<Eleitor> apoiantes) {
    this.apoiantes = apoiantes;
  }

  public void setnApoios(int nApoios) {
    this.nApoios = nApoios;
  }

  public Votacao getVotacao() {
    return votacao;
  }

  public void setVotacao(Votacao votacao) {
    this.votacao = votacao;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjetoDeLei that = (ProjetoDeLei) o;
    return id == that.id
        && Objects.equals(delegadoProponente, that.delegadoProponente)
        && Objects.equals(titulo, that.titulo)
        && Objects.equals(descricao, that.descricao)
        && Arrays.equals(anexoPdf, that.anexoPdf)
        && Objects.equals(tema, that.tema);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, delegadoProponente, titulo, descricao, tema);
    result = 31 * result + Arrays.hashCode(anexoPdf);
    return result;
  }
}
