package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.associations.VotoId;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.enums.ResultadoVotacao;
import pt.ul.fc.css.example.demo.facade.handlers.FecharVotacaoHandler;
import pt.ul.fc.css.example.demo.repositories.*;

@SpringBootTest
public class FecharVotacaoTests {

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;
  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  @Autowired private VotoRepository votoRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  private FecharVotacaoHandler handler;
  private Delegado delegado;
  private Eleitor eleitor;
  private ProjetoDeLei projetoDeLei;
  private Tema tema;
  private Votacao votacao;

  @BeforeEach
  public void setUpTest() {
    handler =
        new FecharVotacaoHandler(
            votoRepository, eleitorDelegadoAssociacaoRepository, votacaoRepository);
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
  public void eleitoresSemDelegadosNaoVotamAoFecharVotacaoTest() {
    handler.fecharVotacao(votacao);
    Assertions.assertTrue(votacao.getVotos().isEmpty());
    Assertions.assertEquals(votacao.getVotosNegativos(), 0);
    Assertions.assertEquals(votacao.getVotosPositivos(), 0);
  }

  @Test
  public void eleitorComDelegadoQueVotouVotaAoFecharVotacaoTest() {
    Voto votoDelegado = new Voto(true, delegado, votacao);
    votoRepository.save(votoDelegado);
    votacao.setVotosPositivos(votacao.getVotosPositivos() + 1);
    votacaoRepository.save(votacao);
    EleitorDelegadoAssociacao ead = new EleitorDelegadoAssociacao(delegado, eleitor, tema);
    eleitorDelegadoAssociacaoRepository.save(ead);
    handler.fecharVotacao(votacao);
    Votacao vot = votacaoRepository.findById(votacao.getId()).get();
    Assertions.assertEquals(2, vot.getVotosPositivos());
    Assertions.assertEquals(0, vot.getVotosNegativos());
    Optional<Voto> votoEleitor =
        votoRepository.findById(new VotoId(eleitor.getId(), votacao.getId()));
    Assertions.assertTrue(votoEleitor.isPresent());
  }

  @Test
  public void votoDeDelegadoEscondidoQuandoNaoVotouTest() {
    Delegado delegadoDelegado = new Delegado("delegadoDelegado", "cc2", "tok2");
    this.eleitorRepository.save(delegadoDelegado);
    EleitorDelegadoAssociacao ead = new EleitorDelegadoAssociacao(delegadoDelegado, delegado, tema);
    eleitorDelegadoAssociacaoRepository.save(ead);
    Voto votoDelegadoDelegado = new Voto(true, delegadoDelegado, votacao);
    votoRepository.save(votoDelegadoDelegado);
    handler.fecharVotacao(votacao);
    Voto votoDelegado =
        votoRepository.findById(new VotoId(delegado.getId(), votacao.getId())).get();
    Assertions.assertNull(votoDelegado.isValorVoto());
  }

  @Test
  public void eleitorComDelegadoParaTemaPaiVotaAFecharVotacaoTest() {
    Tema temaFilho = new Tema("temaFilho", tema);
    this.temaRepository.save(temaFilho);
    projetoDeLei.setTema(temaFilho);
    this.projetoDeLeiRepository.save(projetoDeLei);
    EleitorDelegadoAssociacao ead = new EleitorDelegadoAssociacao(delegado, eleitor, tema);
    this.eleitorDelegadoAssociacaoRepository.save(ead);

    votacao =
        new Votacao(
            EstadoValidade.ABERTO, null, 1, 0, new HashSet<>(), LocalDateTime.now(), projetoDeLei);

    votacaoRepository.save(votacao);

    Voto votoDelegado = new Voto(true, delegado, votacao);
    votoRepository.save(votoDelegado);

    handler.fecharVotacao(votacao);
    Votacao vot = votacaoRepository.findById(votacao.getId()).get();
    Assertions.assertEquals(2, vot.getVotosPositivos());
    Assertions.assertEquals(0, vot.getVotosNegativos());
    Optional<Voto> votoEleitor =
        votoRepository.findById(new VotoId(eleitor.getId(), votacao.getId()));
    Assertions.assertTrue(votoEleitor.isPresent());
  }

  @Test
  public void votacaoFechadaComoAceiteTest() {
    votacao.setVotosPositivos(5);
    this.votacaoRepository.save(votacao);
    handler.fecharVotacao(votacao);
    Votacao vot = this.votacaoRepository.findById(votacao.getId()).get();
    Assertions.assertEquals(ResultadoVotacao.APROVADO, vot.getResultado());
  }

  @Test
  public void votacaoFechadaComoRejeitadaTest() {
    votacao.setVotosNegativos(5);
    this.votacaoRepository.save(votacao);
    handler.fecharVotacao(votacao);
    Votacao vot = this.votacaoRepository.findById(votacao.getId()).get();
    Assertions.assertEquals(ResultadoVotacao.REJEITADO, vot.getResultado());
  }
}
