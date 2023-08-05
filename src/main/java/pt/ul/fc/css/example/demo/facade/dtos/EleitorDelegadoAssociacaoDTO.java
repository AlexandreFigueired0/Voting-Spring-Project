package pt.ul.fc.css.example.demo.facade.dtos;

public class EleitorDelegadoAssociacaoDTO {

  private Long eleitorId;

  private Long delegadoId;

  private String eleitorCC;

  private String delegadoCC;

  private String tema;

  public EleitorDelegadoAssociacaoDTO() {}

  public EleitorDelegadoAssociacaoDTO(Long eleitorId, Long delegadoId, String tema) {
    this.eleitorId = eleitorId;
    this.delegadoId = delegadoId;
    this.tema = tema;
  }

  public String getEleitorCC() {
    return eleitorCC;
  }

  public void setEleitorCC(String eleitorCC) {
    this.eleitorCC = eleitorCC;
  }

  public String getDelegadoCC() {
    return delegadoCC;
  }

  public void setDelegadoCC(String delegadoCC) {
    this.delegadoCC = delegadoCC;
  }

  public Long getEleitorId() {
    return eleitorId;
  }

  public void setEleitorId(Long eleitorId) {
    this.eleitorId = eleitorId;
  }

  public Long getDelegadoId() {
    return delegadoId;
  }

  public void setDelegadoId(Long delegadoId) {
    this.delegadoId = delegadoId;
  }

  public String getTema() {
    return tema;
  }

  public void setTema(String tema) {
    this.tema = tema;
  }
}
