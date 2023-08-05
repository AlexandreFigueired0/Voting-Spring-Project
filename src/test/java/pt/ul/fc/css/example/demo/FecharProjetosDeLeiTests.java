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
import pt.ul.fc.css.example.demo.facade.handlers.FecharProjetosDeLeiHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@SpringBootTest
public class FecharProjetosDeLeiTests {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  private FecharProjetosDeLeiHandler handler;

  private Delegado delegado;
  private Tema tema;

  @BeforeEach
  public void setUpTest() {
    handler = new FecharProjetosDeLeiHandler(projetoDeLeiRepository);
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
  public void nenhumProjetoParaFecharTest() {
    this.projetoDeLeiRepository.save(
        new ProjetoDeLei(
            "t", "desc", new byte[1], tema, LocalDateTime.now().plusDays(1), delegado));
    this.projetoDeLeiRepository.save(
        new ProjetoDeLei(
            "p", "desc", new byte[1], tema, LocalDateTime.now().plusDays(1), delegado));
    handler.fecharProjetosDeLei();
    List<ProjetoDeLei> projetos = this.projetoDeLeiRepository.findByEstado(EstadoValidade.ABERTO);
    Assertions.assertEquals(2, projetos.size());
  }

  @Test
  public void fecharProjetoDeLeiExpirado() {
    this.projetoDeLeiRepository.save(
        new ProjetoDeLei(
            "t", "desc", new byte[1], tema, LocalDateTime.now().minusDays(2), delegado));
    handler.fecharProjetosDeLei();
    List<ProjetoDeLei> projetos = this.projetoDeLeiRepository.findByEstado(EstadoValidade.ABERTO);
    Assertions.assertEquals(0, projetos.size());
  }
}
