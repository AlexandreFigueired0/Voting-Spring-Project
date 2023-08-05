package pt.ul.fc.css.example.demo.facade.handlers;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.exceptions.DataSuperiorAUmAnoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchDelegadoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchTemaException;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class ApresentarProjetoLeiHandler {

  @Autowired private EleitorRepository eleitorRepository;
  @Autowired private TemaRepository temaRepository;
  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  public ApresentarProjetoLeiHandler(
      EleitorRepository eleitorRepository,
      TemaRepository temaRepository,
      ProjetoDeLeiRepository projetoDeLeiRepository) {
    this.eleitorRepository = eleitorRepository;
    this.temaRepository = temaRepository;
    this.projetoDeLeiRepository = projetoDeLeiRepository;
  }

  public void apresentarProjetoLei(ProjetoDeLei projetoDeLei)
      throws NoSuchDelegadoException, NoSuchTemaException, DataSuperiorAUmAnoException {
    Optional<Delegado> d =
        eleitorRepository.findDelegadoById(projetoDeLei.getDelegadoProponente().getId());
    if (d.isEmpty()) {
      throw new NoSuchDelegadoException("No Such Delegado");
    }
    Optional<Tema> t = temaRepository.findById(projetoDeLei.getTema().getId());
    if (t.isEmpty()) {
      throw new NoSuchTemaException("No Such Tema");
    }
    if (LocalDateTime.now().plusYears(1).compareTo(projetoDeLei.getDataValidade()) < 0) {
      throw new DataSuperiorAUmAnoException(
          "A data de validade do projeto de lei nao pode ser superior a 1 ano.");
    }
    projetoDeLei.getApoiantes().add(projetoDeLei.getDelegadoProponente());
    projetoDeLeiRepository.save(projetoDeLei);
  }
}
