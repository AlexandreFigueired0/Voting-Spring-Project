package pt.ul.fc.css.example.demo.facade.handlers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacaoId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.repositories.EleitorDelegadoAssociacaoRepository;

@Component
public class EscolherDelegadoHandler {

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  public EscolherDelegadoHandler(
      EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository) {
    this.eleitorDelegadoAssociacaoRepository = eleitorDelegadoAssociacaoRepository;
  }

  public void eleitorEscolheDelegado(Eleitor eleitor, Delegado delegado, Tema tema) {
    EleitorDelegadoAssociacaoId id = new EleitorDelegadoAssociacaoId(tema.getId(), eleitor.getId());
    Optional<EleitorDelegadoAssociacao> eda = eleitorDelegadoAssociacaoRepository.findById(id);
    if (eda.isPresent()) {
      this.eleitorDelegadoAssociacaoRepository.updateDelegado(
          eleitor.getId(), delegado, tema.getId());
    } else {
      EleitorDelegadoAssociacao eleitorDelegadoAssociacao =
          new EleitorDelegadoAssociacao(delegado, eleitor, tema);
      this.eleitorDelegadoAssociacaoRepository.save(eleitorDelegadoAssociacao);
    }
  }
}
