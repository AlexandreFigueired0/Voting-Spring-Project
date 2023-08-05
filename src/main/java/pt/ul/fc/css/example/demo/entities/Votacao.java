package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.lang.NonNull;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.enums.ResultadoVotacao;

@Entity
public class Votacao {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NonNull
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoValidade estado;

  @Enumerated(EnumType.ORDINAL)
  private ResultadoVotacao resultado;

  private int votosPositivos;
  private int votosNegativos;

  @OneToMany(mappedBy = "votacao")
  private Set<Voto> votos;

  @NonNull
  @Column(name = "data_validade", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime dataValidade;

  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "projeto_de_lei_id", referencedColumnName = "id", nullable = false)
  private ProjetoDeLei projetoDeLei;

  public Votacao(
      @NonNull EstadoValidade estado,
      ResultadoVotacao resultado,
      int votosPositivos,
      int votosNegativos,
      Set<Voto> votos,
      @NonNull LocalDateTime dataValidade,
      ProjetoDeLei projetoDeLei) {
    this.estado = estado;
    this.resultado = resultado;
    this.votosPositivos = votosPositivos;
    this.votosNegativos = votosNegativos;
    this.votos = votos;
    this.dataValidade = dataValidade;
    this.projetoDeLei = projetoDeLei;
  }

  public Votacao() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public LocalDateTime getDataValidade() {
    return dataValidade;
  }

  public void setDataValidade(@NonNull LocalDateTime dataValidade) {
    this.dataValidade = dataValidade;
  }

  @NonNull
  public EstadoValidade getEstado() {
    return estado;
  }

  public void setEstado(@NonNull EstadoValidade estado) {
    this.estado = estado;
  }

  public ResultadoVotacao getResultado() {
    return resultado;
  }

  public void setResultado(ResultadoVotacao resultado) {
    this.resultado = resultado;
  }

  public int getVotosPositivos() {
    return votosPositivos;
  }

  public void setVotosPositivos(int votosPositivos) {
    this.votosPositivos = votosPositivos;
  }

  public int getVotosNegativos() {
    return votosNegativos;
  }

  public void setVotosNegativos(int votosNegativos) {
    this.votosNegativos = votosNegativos;
  }

  public Set<Voto> getVotos() {
    return votos;
  }

  public void setVotos(Set<Voto> votos) {
    this.votos = votos;
  }

  public ProjetoDeLei getProjetoDeLei() {
    return projetoDeLei;
  }

  public void setProjetoDeLei(ProjetoDeLei projetoDeLei) {
    this.projetoDeLei = projetoDeLei;
  }
}
