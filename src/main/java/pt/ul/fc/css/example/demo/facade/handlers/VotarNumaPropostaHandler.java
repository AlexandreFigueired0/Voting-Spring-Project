package pt.ul.fc.css.example.demo.facade.handlers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.associations.VotoId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.repositories.EleitorDelegadoAssociacaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotoRepository;

@Component
public class VotarNumaPropostaHandler {

  @Autowired EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  @Autowired private VotoRepository votoRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  public VotarNumaPropostaHandler() {}

  public VotarNumaPropostaHandler(
      EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository,
      VotoRepository votoRepository,
      VotacaoRepository votacaoRepository) {
    this.eleitorDelegadoAssociacaoRepository = eleitorDelegadoAssociacaoRepository;
    this.votoRepository = votoRepository;
    this.votacaoRepository = votacaoRepository;
  }

  public Voto indicaVotacao(Votacao votacao, Eleitor eleitor) {
    Voto voto = null;
    Tema tema = votacao.getProjetoDeLei().getTema();
    Optional<EleitorDelegadoAssociacao> ead;
    while (tema != null) {
      ead = eleitorDelegadoAssociacaoRepository.findByEleitorTema(eleitor.getId(), tema.getId());
      if (ead.isPresent()) {
        Delegado delegado = ead.get().getDelegado();
        VotoId votoId = new VotoId(delegado.getId(), votacao.getId());
        Optional<Voto> v = votoRepository.findById(votoId);
        if (v.isPresent()) {
          voto = v.get();
          Voto votoEleitor = new Voto(voto.isValorVoto(), eleitor, voto.getVotacao());
          return votoEleitor;
        }
      }
      tema = tema.getTemaPai();
    }
    return voto;
  }

  public void indicaVoto(Voto voto) throws AlreadyVotedVotacaoException {
    if (verificaVoto(voto)) {
      Votacao votacaoVoto = voto.getVotacao();
      if (voto.isValorVoto()) {
        votacaoRepository.updateVotosPositivos(votacaoVoto.getId());
      } else {
        votacaoRepository.updateVotosNegativos(votacaoVoto.getId());
      }
      Voto votoFormatado = voto.getEleitor().setupVoto(voto);
      votoRepository.save(votoFormatado);

      return;
    }
    throw new AlreadyVotedVotacaoException("Ja foi indicado voto para esta votacao.");
  }

  private boolean verificaVoto(Voto voto) {
    VotoId votoId = new VotoId(voto.getEleitor().getId(), voto.getVotacao().getId());
    Optional<Voto> v = votoRepository.findById(votoId);
    return v.isEmpty();
  }
}
