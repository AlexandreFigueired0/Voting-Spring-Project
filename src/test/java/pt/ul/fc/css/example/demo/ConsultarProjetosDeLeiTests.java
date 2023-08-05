package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.facade.handlers.ConsultarProjetosDeLeiHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@SpringBootTest
public class ConsultarProjetosDeLeiTests {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  private ConsultarProjetosDeLeiHandler handler;
  private Delegado delegado;
  private Tema tema;

  @BeforeEach
  public void setUpTest() {
    handler = new ConsultarProjetosDeLeiHandler(projetoDeLeiRepository);
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
  public void noProjetosDeLeiTest() {
    List<ProjetoDeLeiDTO> projetos = handler.listarProjetosDeLei();
    Assertions.assertTrue(projetos.isEmpty());
  }

  @Test
  public void noProjetosDeLeiAbertosTest() {
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei(
            "titulo",
            "desc",
            new byte[1],
            tema,
            LocalDateTime.now(),
            delegado,
            EstadoValidade.FECHADO);
    this.projetoDeLeiRepository.save(projetoDeLei);
    List<ProjetoDeLeiDTO> projetos = handler.listarProjetosDeLei();
    Assertions.assertTrue(projetos.isEmpty());
  }

  @Test
  public void projetosDeLeiAbertosTest() {
    ProjetoDeLei projetoDeLei1 =
        new ProjetoDeLei("p1", "desc1", new byte[1], tema, LocalDateTime.now(), delegado);
    ProjetoDeLei projetoDeLei2 =
        new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
    ProjetoDeLei projetoDeLei3 =
        new ProjetoDeLei("p3", "desc3", new byte[1], tema, LocalDateTime.now(), delegado);
    this.projetoDeLeiRepository.save(projetoDeLei1);
    this.projetoDeLeiRepository.save(projetoDeLei2);
    this.projetoDeLeiRepository.save(projetoDeLei3);
    List<ProjetoDeLeiDTO> projetos = handler.listarProjetosDeLei();
    Assertions.assertTrue(projetos.size() == 3);
  }

  @Test
  public void projetosDeLeiAbertosEFechadosTest() {
    ProjetoDeLei projetoDeLei1 =
        new ProjetoDeLei(
            "p1",
            "desc1",
            new byte[1],
            tema,
            LocalDateTime.now(),
            delegado,
            EstadoValidade.FECHADO);
    ProjetoDeLei projetoDeLei2 =
        new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
    this.projetoDeLeiRepository.save(projetoDeLei1);
    this.projetoDeLeiRepository.save(projetoDeLei2);
    List<ProjetoDeLeiDTO> projetos = handler.listarProjetosDeLei();
    Assertions.assertTrue(projetos.size() == 1);
    Assertions.assertFalse(projetos.contains(new ProjetoDeLeiDTO(projetoDeLei1)));
  }

  @Test
  public void getProjetoInexistenteTest() {
    Assertions.assertThrows(NoSuchProjetoDeLeiException.class, () -> handler.getProjetoDeLei(-1));
  }

  @Test
  public void getProjetoExistenteTest() {
    ProjetoDeLei projetoDeLei =
        new ProjetoDeLei("p", "desc", new byte[1], tema, LocalDateTime.now(), delegado);
    this.projetoDeLeiRepository.save(projetoDeLei);
    Assertions.assertDoesNotThrow(
        () -> {
          ProjetoDeLeiDTO projetoRecebido = handler.getProjetoDeLei(projetoDeLei.getId());
          Assertions.assertTrue(projetoRecebido.equals(new ProjetoDeLeiDTO(projetoDeLei)));
        });
  }
}
