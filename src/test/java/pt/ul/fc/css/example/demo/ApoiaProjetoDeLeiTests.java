package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.AlreadyExpiredProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.AlreadySupportedProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.facade.handlers.ApoiaProjetoDeLeiHandler;
import pt.ul.fc.css.example.demo.repositories.*;

@SpringBootTest
public class ApoiaProjetoDeLeiTests {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  @Autowired private VotoRepository votoRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  private ApoiaProjetoDeLeiHandler handler;

  private Delegado delegado;
  private Eleitor eleitor;
  private ProjetoDeLei projetoDeLei;
  private Tema tema;

  @BeforeEach
  public void setUpTest() {
    handler =
        new ApoiaProjetoDeLeiHandler(
            projetoDeLeiRepository, eleitorRepository, votacaoRepository, votoRepository);
    delegado = new Delegado("delegado1", "cc", "token");
    eleitor = new Eleitor("eleitor", "cc1", "tok1");
    this.eleitorRepository.save(delegado);
    this.eleitorRepository.save(eleitor);
    tema = new Tema("t");
    this.temaRepository.save(tema);
    projetoDeLei = new ProjetoDeLei("p", "desc", new byte[1], tema, LocalDateTime.now(), delegado);
    projetoDeLeiRepository.save(projetoDeLei);
  }

  @AfterEach
  public void endTest() {
    this.votoRepository.deleteAll();
    this.votacaoRepository.deleteAll();
    this.projetoDeLeiRepository.deleteAll();
    this.temaRepository.deleteAll();
    this.eleitorRepository.deleteAll();
  }

  @Test
  public void naoExisteProjetoDeLeiTest() {
    ProjetoDeLei projetoDeLei2 =
        new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
    Assertions.assertThrows(
        NoSuchProjetoDeLeiException.class,
        () -> handler.apoiaProjetoDeLei(projetoDeLei2.getId(), eleitor));
  }

  @Test
  public void projetoDeLeiJaApoiadoTest()
      throws AlreadySupportedProjetoDeLeiException, NoSuchProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor);
    Eleitor novaVersao = this.eleitorRepository.findById(eleitor.getId()).get();
    Assertions.assertThrows(
        AlreadySupportedProjetoDeLeiException.class,
        () -> handler.apoiaProjetoDeLei(projetoDeLei.getId(), novaVersao));
  }

  @Test
  public void apoiaProjetoDeLeiNormalSemAbrirVotacaoTest()
      throws AlreadySupportedProjetoDeLeiException, NoSuchProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    int nApoiosAntes = projetoDeLei.getnApoios();
    handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor);
    Optional<ProjetoDeLei> projApoiadoOpt = projetoDeLeiRepository.findById(projetoDeLei.getId());
    ProjetoDeLei projApoiado = projApoiadoOpt.get();
    int nApoiosDepois = projApoiado.getnApoios();
    Assertions.assertEquals(nApoiosDepois, nApoiosAntes + 1);
    Assertions.assertTrue(projApoiado.getApoiantes().contains(eleitor));
    Eleitor eleitorNovo = eleitorRepository.findById(eleitor.getId()).get();
    Assertions.assertTrue(eleitorNovo.getProjetosDeLeiApoiados().contains(projApoiado));
  }

  @Test
  public void apoiaProjetoDeLeiExpiradoTest() {
    projetoDeLei.setEstado(EstadoValidade.FECHADO);
    this.projetoDeLeiRepository.save(projetoDeLei);
    Assertions.assertThrows(
        AlreadyExpiredProjetoDeLeiException.class,
        () -> handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor));
  }

  @Test
  public void abreVotacaoTest()
      throws NoSuchProjetoDeLeiException, AlreadySupportedProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    projetoDeLei.setnApoios(9999);
    this.projetoDeLeiRepository.save(projetoDeLei);
    this.handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor);
    List<Votacao> votacoes = this.votacaoRepository.findAll();
    ProjetoDeLei projApoiado = projetoDeLeiRepository.findById(projetoDeLei.getId()).get();
    Assertions.assertEquals(1, votacoes.size());
    Assertions.assertNotNull(projApoiado.getVotacao());
  }

  @Test
  public void abreVotacaoDeProjetoComValidadeMenorQueQuinzeDiasTest()
      throws NoSuchProjetoDeLeiException, AlreadySupportedProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    LocalDateTime dataValidade = LocalDateTime.now();
    projetoDeLei = new ProjetoDeLei("p2", "desc2", new byte[1], tema, dataValidade, delegado);
    projetoDeLei.setnApoios(9999);
    projetoDeLeiRepository.save(projetoDeLei);
    this.handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor);
    ProjetoDeLei projeto = projetoDeLeiRepository.findById(projetoDeLei.getId()).get();
    Votacao votacao = projeto.getVotacao();
    Assertions.assertTrue(votacao.getDataValidade().isAfter(LocalDateTime.now().plusDays(14)));
  }

  @Test
  public void abreVotacaoDeProjetoComValidadeMaiorQueDoisMesesTest()
      throws NoSuchProjetoDeLeiException, AlreadySupportedProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    projetoDeLei.setDataValidade(LocalDateTime.now().plusMonths(3));
    projetoDeLei.setnApoios(9999);
    projetoDeLeiRepository.save(projetoDeLei);
    this.handler.apoiaProjetoDeLei(projetoDeLei.getId(), eleitor);
    ProjetoDeLei projeto = projetoDeLeiRepository.findById(projetoDeLei.getId()).get();
    Votacao votacao = projeto.getVotacao();
    Assertions.assertTrue(votacao.getDataValidade().isBefore(LocalDateTime.now().plusMonths(2)));
  }
}
