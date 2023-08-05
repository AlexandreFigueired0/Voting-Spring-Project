package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.DataSuperiorAUmAnoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchDelegadoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchTemaException;
import pt.ul.fc.css.example.demo.facade.handlers.ApresentarProjetoLeiHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@SpringBootTest
public class ApresentarProjetoLeiTests {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  private ApresentarProjetoLeiHandler handler;

  private Delegado delegado;
  private Tema tema;

  @BeforeEach
  public void setUpTest() {
    handler =
        new ApresentarProjetoLeiHandler(eleitorRepository, temaRepository, projetoDeLeiRepository);
    delegado = new Delegado("delegado1", "cc", "token");
    this.eleitorRepository.save(delegado);
    tema = new Tema("t");
    this.temaRepository.save(tema);
  }

  @AfterEach
  public void endTest() {
    this.projetoDeLeiRepository.deleteAll();
    this.temaRepository.deleteAll();
    this.eleitorRepository.deleteAll();
  }

  @Test
  public void delegadoInexistenteTest() {
    Delegado delegadoInexistente = new Delegado("a", "a", "a");
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei("p", "desc", new byte[1], tema, LocalDateTime.now(), delegadoInexistente);
    Assertions.assertThrows(
        NoSuchDelegadoException.class, () -> handler.apresentarProjetoLei(projetoDeLei));
  }

  @Test
  public void temaInexistenteTest() {
    Tema temaInexistente = new Tema("tema");
    ProjetoDeLei p =
        new ProjetoDeLei(
            "titulo", "desc", new byte[1], temaInexistente, LocalDateTime.now(), delegado);
    Assertions.assertThrows(NoSuchTemaException.class, () -> handler.apresentarProjetoLei(p));
  }

  @Test
  public void projetoComDataSuperiorAUmAnoTest() {
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei(
            "p",
            "desc",
            new byte[1],
            tema,
            LocalDateTime.now().plusYears(1).plusHours(1),
            delegado);
    Assertions.assertThrows(
        DataSuperiorAUmAnoException.class, () -> handler.apresentarProjetoLei(projetoDeLei));
  }

  @Test
  public void apresentaProjetoLei()
      throws NoSuchTemaException, NoSuchDelegadoException, DataSuperiorAUmAnoException {
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei("p", "desc", new byte[1], tema, LocalDateTime.now(), delegado);
    handler.apresentarProjetoLei(projetoDeLei);
    List<ProjetoDeLei> projetos = projetoDeLeiRepository.findByEstado(EstadoValidade.ABERTO);
    Assertions.assertTrue(projetos.contains(projetoDeLei));
    ProjetoDeLei projProposto = projetos.get(0);
    Assertions.assertEquals(delegado, projProposto.getDelegadoProponente());
    Delegado del =
        eleitorRepository.findDelegadoById(projProposto.getDelegadoProponente().getId()).get();
    Assertions.assertTrue(del.getProjetosDeLeiApoiados().contains(projProposto));
  }
}
