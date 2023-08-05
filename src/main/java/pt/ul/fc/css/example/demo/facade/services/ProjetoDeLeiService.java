package pt.ul.fc.css.example.demo.facade.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.facade.handlers.ApoiaProjetoDeLeiHandler;
import pt.ul.fc.css.example.demo.facade.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.facade.handlers.ConsultarProjetosDeLeiHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class ProjetoDeLeiService {

  @Autowired private ConsultarProjetosDeLeiHandler consultarProjetosDeLeiHandler;
  @Autowired private ApoiaProjetoDeLeiHandler apoiaProjetoDeLeiHandler;
  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;
  @Autowired private EleitorRepository eleitorRepository;
  @Autowired private TemaRepository temaRepository;

  @Autowired private ApresentarProjetoLeiHandler apresentarProjetoLeiHandler;

  public List<ProjetoDeLeiDTO> getProjetosDeLeiNaoExpirados() {
    return this.consultarProjetosDeLeiHandler.listarProjetosDeLei();
  }

  public void apoiaProjetoDeLei(String eleitorCC, Long projetoDeLeiId)
      throws NoSuchProjetoDeLeiException, NoSuchEleitorException,
          AlreadySupportedProjetoDeLeiException, AlreadyExpiredProjetoDeLeiException {
    Optional<Eleitor> eleitorOptional = this.eleitorRepository.findByCc(eleitorCC);
    Eleitor eleitor =
        eleitorOptional.orElseThrow(() -> new NoSuchEleitorException("Eleitor nao existe"));
    this.apoiaProjetoDeLeiHandler.apoiaProjetoDeLei(projetoDeLeiId, eleitor);
  }

  public ProjetoDeLeiDTO apresentaProjetoDeLei(
      String titulo,
      String temaNome,
      String delegadoCC,
      String dataValidadeString,
      String descricao,
      byte[] anexoPdf)
      throws NoSuchTemaException, NoSuchDelegadoException, DataSuperiorAUmAnoException {
    Optional<Delegado> delegadoOptional = this.eleitorRepository.findDelegadoByCC(delegadoCC);
    Delegado delegado =
        delegadoOptional.orElseThrow(() -> new NoSuchDelegadoException("Delegado nao existe"));

    Optional<Tema> temaOptional = this.temaRepository.findByNome(temaNome);
    Tema tema = temaOptional.orElseThrow(() -> new NoSuchTemaException("Tema nao existe"));
    DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime dataValidade = LocalDateTime.parse(dataValidadeString, dataFormatter);
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei(titulo, descricao, anexoPdf, tema, dataValidade, delegado);
    apresentarProjetoLeiHandler.apresentarProjetoLei(projetoDeLei);
    return new ProjetoDeLeiDTO(projetoDeLei);
  }

  public ProjetoDeLeiDTO getProjetoDeLeiDTOById(Long id) throws NoSuchProjetoDeLeiException {
    Optional<ProjetoDeLei> projetoOpt = this.projetoDeLeiRepository.findById(id);
    ProjetoDeLei projeto =
        projetoOpt.orElseThrow(() -> new NoSuchProjetoDeLeiException("Projeto de Lei nao existe"));
    return new ProjetoDeLeiDTO(projeto);
  }
}
