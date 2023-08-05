package pt.ul.fc.css.example.demo.facade.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.exceptions.NoSuchDelegadoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchTemaException;
import pt.ul.fc.css.example.demo.facade.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@Component
public class EleitorService {

  @Autowired EscolherDelegadoHandler escolherDelegadoHandler;
  @Autowired EleitorRepository eleitorRepository;
  @Autowired TemaRepository temaRepository;

  public void escolherDelegado(String temaNome, String eleitorCC, String delegadoCC)
      throws NoSuchDelegadoException, NoSuchTemaException, NoSuchEleitorException {
    Optional<Delegado> delegadoOptional = this.eleitorRepository.findDelegadoByCC(delegadoCC);
    Delegado delegado =
        delegadoOptional.orElseThrow(() -> new NoSuchDelegadoException("Delegado nao existe"));

    Optional<Tema> temaOptional = this.temaRepository.findByNome(temaNome);
    Tema tema = temaOptional.orElseThrow(() -> new NoSuchTemaException("Tema nao existe"));

    Optional<Eleitor> eleitorOptional = this.eleitorRepository.findByCc(eleitorCC);
    Eleitor eleitor =
        eleitorOptional.orElseThrow(() -> new NoSuchEleitorException("Eleitor nao existe"));

    this.escolherDelegadoHandler.eleitorEscolheDelegado(eleitor, delegado, tema);
  }
}
