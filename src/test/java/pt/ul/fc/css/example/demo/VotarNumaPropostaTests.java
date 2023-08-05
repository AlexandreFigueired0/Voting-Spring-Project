package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import java.util.HashSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.facade.handlers.VotarNumaPropostaHandler;
import pt.ul.fc.css.example.demo.repositories.*;

@SpringBootTest
public class VotarNumaPropostaTests {

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  @Autowired private TemaRepository temaRepository;

  @Autowired private VotoRepository votoRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  private VotarNumaPropostaHandler handler;

  private Delegado delegado;
  private Eleitor eleitor;
  private ProjetoDeLei projetoDeLei;
  private Tema tema;
  private Votacao votacao;

  @BeforeEach
  public void setUpTest() {
    handler =
        new VotarNumaPropostaHandler(
            eleitorDelegadoAssociacaoRepository, votoRepository, votacaoRepository);
    delegado = new Delegado("delegado1", "cc", "token");
    eleitor = new Eleitor("eleitor", "cc1", "tok1");
    this.eleitorRepository.save(delegado);
    this.eleitorRepository.save(eleitor);
    tema = new Tema("t");
    this.temaRepository.save(tema);
    projetoDeLei = new ProjetoDeLei("p", "desc", new byte[1], tema, LocalDateTime.now(), delegado);
    projetoDeLeiRepository.save(projetoDeLei);
    votacao =
        new Votacao(
            EstadoValidade.ABERTO, null, 0, 0, new HashSet<>(), LocalDateTime.now(), projetoDeLei);
    this.votacaoRepository.save(votacao);
  }

  @AfterEach
  public void endTest() {
    this.votoRepository.deleteAll();
    this.votacaoRepository.deleteAll();
    this.eleitorDelegadoAssociacaoRepository.deleteAll();
    this.projetoDeLeiRepository.deleteAll();
    this.temaRepository.deleteAll();
    this.eleitorRepository.deleteAll();
  }

  @Test
  public void indicaVotacaoEleitorSemDelegadoTest() {
    Voto votoDelegado = handler.indicaVotacao(votacao, eleitor);
    Assertions.assertNull(votoDelegado);
  }

  @Test
  public void indicaVotacaoEleitorTemDelegadoParaTemaTest() {
    Voto v = new Voto(true, delegado, votacao);
    votoRepository.save(v);
    EleitorDelegadoAssociacao ead = new EleitorDelegadoAssociacao(delegado, eleitor, tema);
    eleitorDelegadoAssociacaoRepository.save(ead);
    Voto votoDelegado = handler.indicaVotacao(votacao, eleitor);
    Assertions.assertEquals(v.isValorVoto(), votoDelegado.isValorVoto());
  }

  @Test
  public void indicaVotacaoEleitorTemDelegadoParaTemaPai() {
    Tema temaFilho = new Tema("temaFilho", tema);
    this.temaRepository.save(temaFilho);
    projetoDeLei.setTema(temaFilho);
    this.projetoDeLeiRepository.save(projetoDeLei);
    EleitorDelegadoAssociacao ead = new EleitorDelegadoAssociacao(delegado, eleitor, tema);
    this.eleitorDelegadoAssociacaoRepository.save(ead);
    Voto v = new Voto(true, delegado, votacao);
    votoRepository.save(v);
    Voto votoDelegado = handler.indicaVotacao(votacao, eleitor);
    Assertions.assertTrue(votoDelegado.isValorVoto());
  }

  @Test
  public void eleitorJaVotou() {
    Voto voto = new Voto(true, eleitor, votacao);
    votoRepository.save(voto);
    Assertions.assertThrows(
        AlreadyVotedVotacaoException.class,
        () -> {
          handler.indicaVoto(voto);
        });
  }

  @Test
  public void eleitorVotaNegativo() throws AlreadyVotedVotacaoException {
    Voto voto = new Voto(false, eleitor, votacao);
    handler.indicaVoto(voto);
    Votacao votacaoVotada = votacaoRepository.findById(votacao.getId()).get();
    Assertions.assertEquals(votacaoVotada.getVotosPositivos(), 0);
    Assertions.assertEquals(votacaoVotada.getVotosNegativos(), 1);
  }

  @Test
  public void eleitorVotaPositivo() throws AlreadyVotedVotacaoException {
    int votosPositivosAntes = votacao.getVotosPositivos();
    Voto voto = new Voto(true, eleitor, votacao);
    handler.indicaVoto(voto);
    Votacao votacaoVotada = votacaoRepository.findById(votacao.getId()).get();
    int votosPositivosDepois = votacaoVotada.getVotosPositivos();
    Assertions.assertEquals(votosPositivosDepois, votosPositivosAntes + 1);
    Assertions.assertEquals(0, votacaoVotada.getVotosNegativos());
  }
}
