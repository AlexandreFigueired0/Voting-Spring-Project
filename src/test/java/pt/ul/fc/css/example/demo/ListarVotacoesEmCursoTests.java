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
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.facade.handlers.ListarVotacoesEmCursoHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;

@SpringBootTest
public class ListarVotacoesEmCursoTests {

  private ListarVotacoesEmCursoHandler handler;

  @Autowired private VotacaoRepository votacaoRepository;

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  private Delegado delegado;
  private Tema tema;

  private ProjetoDeLei proj1;
  private ProjetoDeLei proj2;

  @BeforeEach
  public void setUpTest() {
    handler = new ListarVotacoesEmCursoHandler(votacaoRepository);
    delegado = new Delegado("delegado1", "cc", "token");
    this.eleitorRepository.save(delegado);
    tema = new Tema("t");
    this.temaRepository.save(tema);
    this.proj1 = new ProjetoDeLei("p1", "desc1", new byte[1], tema, LocalDateTime.now(), delegado);
    this.proj2 = new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
    projetoDeLeiRepository.save(this.proj1);
    projetoDeLeiRepository.save(this.proj2);
  }

  @AfterEach
  public void endTest() {
    this.votacaoRepository.deleteAll();
    this.projetoDeLeiRepository.deleteAll();
    this.temaRepository.deleteAll();
    this.eleitorRepository.deleteAll();
    this.votacaoRepository.deleteAll();
  }

  @Test
  public void noVotacoesTest() {
    List<VotacaoDTO> votacoes = handler.listarVotacoesEmCurso();
    Assertions.assertTrue(votacoes.isEmpty());
  }

  @Test
  public void noVotacoesEmCursoTest() {
    Votacao votacao =
        new Votacao(EstadoValidade.FECHADO, null, 0, 0, null, LocalDateTime.now(), this.proj1);
    votacaoRepository.save(votacao);
    List<VotacaoDTO> votacoes = handler.listarVotacoesEmCurso();
    Assertions.assertTrue(votacoes.isEmpty());
  }

  @Test
  public void votacoesEmCursoTest() {
    Votacao votacao1 =
        new Votacao(EstadoValidade.ABERTO, null, 0, 0, null, LocalDateTime.now(), this.proj1);
    votacaoRepository.save(votacao1);
    Votacao votacao2 =
        new Votacao(EstadoValidade.ABERTO, null, 0, 0, null, LocalDateTime.now(), this.proj1);
    votacaoRepository.save(votacao2);
    List<VotacaoDTO> votacoes = handler.listarVotacoesEmCurso();
    Assertions.assertTrue(votacoes.size() == 2);
  }

  @Test
  public void votacoesEmCursoEFechadas() {
    Votacao votacao1 =
        new Votacao(EstadoValidade.ABERTO, null, 0, 0, null, LocalDateTime.now(), this.proj1);
    votacaoRepository.save(votacao1);
    Votacao votacao2 =
        new Votacao(EstadoValidade.FECHADO, null, 0, 0, null, LocalDateTime.now(), this.proj1);
    votacaoRepository.save(votacao2);
    List<VotacaoDTO> votacoes = handler.listarVotacoesEmCurso();
    Assertions.assertTrue(votacoes.size() == 1);
    Assertions.assertTrue(votacoes.contains(new VotacaoDTO(votacao1)));
  }
}
