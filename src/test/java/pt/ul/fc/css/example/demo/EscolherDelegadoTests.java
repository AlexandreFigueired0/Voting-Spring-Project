package pt.ul.fc.css.example.demo;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacaoId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.facade.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorDelegadoAssociacaoRepository;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.TemaRepository;

@SpringBootTest
public class EscolherDelegadoTests {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  @Autowired private EleitorRepository eleitorRepository;

  @Autowired private TemaRepository temaRepository;

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  private EscolherDelegadoHandler handler;

  private Delegado delegado;
  private Delegado delegado2;
  private Tema tema;

  private Eleitor eleitor;

  @BeforeEach
  public void setUpTest() {
    handler = new EscolherDelegadoHandler(eleitorDelegadoAssociacaoRepository);
    delegado = new Delegado("delegado1", "cc", "token");
    this.eleitorRepository.save(delegado);
    delegado2 = new Delegado("delegado2", "cc2", "token2");
    this.eleitorRepository.save(delegado2);
    tema = new Tema("t");
    this.temaRepository.save(tema);
    eleitor = new Eleitor("eleitor", "cce", "tokene");
    this.eleitorRepository.save(eleitor);
  }

  @AfterEach
  public void endTest() {
    this.eleitorDelegadoAssociacaoRepository.deleteAll();
    this.projetoDeLeiRepository.deleteAll();
    this.temaRepository.deleteAll();
    this.eleitorRepository.deleteAll();
  }

  @Test
  public void escolheDelegadoParaTemaQueNaoTinha() {
    handler.eleitorEscolheDelegado(eleitor, delegado, tema);
    EleitorDelegadoAssociacaoId eadId =
        new EleitorDelegadoAssociacaoId(tema.getId(), eleitor.getId());
    Optional<EleitorDelegadoAssociacao> ead = eleitorDelegadoAssociacaoRepository.findById(eadId);
    Assertions.assertTrue(ead.isPresent());
    Assertions.assertEquals(delegado, ead.get().getDelegado());
  }

  @Test
  public void escolheDelegadoParaTemaQueJaTinha() {
    EleitorDelegadoAssociacao oldEad = new EleitorDelegadoAssociacao(delegado, eleitor, tema);
    eleitorDelegadoAssociacaoRepository.save(oldEad);
    handler.eleitorEscolheDelegado(eleitor, delegado2, tema);
    EleitorDelegadoAssociacaoId eadId =
        new EleitorDelegadoAssociacaoId(tema.getId(), eleitor.getId());
    Optional<EleitorDelegadoAssociacao> ead = eleitorDelegadoAssociacaoRepository.findById(eadId);
    Assertions.assertTrue(ead.isPresent());
    Assertions.assertEquals(delegado2, ead.get().getDelegado());
  }
}
