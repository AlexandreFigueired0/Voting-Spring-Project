package pt.ul.fc.css.example.demo.facade.dtos;

public class EleitorApoiaProjetoDTO {

  private long eleitorId;
  private long projetoId;

  private String eleitorCC;

  public EleitorApoiaProjetoDTO() {}

  public String getEleitorCC() {
    return eleitorCC;
  }

  public void setEleitorCC(String eleitorCC) {
    this.eleitorCC = eleitorCC;
  }

  public long getEleitorId() {
    return eleitorId;
  }

  public void setEleitorId(long eleitorId) {
    this.eleitorId = eleitorId;
  }

  public long getProjetoId() {
    return projetoId;
  }

  public void setProjetoId(long projetoId) {
    this.projetoId = projetoId;
  }
}
