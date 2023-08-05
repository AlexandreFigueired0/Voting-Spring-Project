package pt.ul.fc.css.example.demo.facade.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchVotacaoException;
import pt.ul.fc.css.example.demo.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.facade.dtos.VotoDTO;
import pt.ul.fc.css.example.demo.facade.handlers.ListarVotacoesEmCursoHandler;
import pt.ul.fc.css.example.demo.facade.handlers.VotarNumaPropostaHandler;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;

@Component
public class VotacaoService {

  @Autowired private ListarVotacoesEmCursoHandler listarVotacoesEmCursoHandler;
  @Autowired private VotarNumaPropostaHandler votarNumaPropostaHandler;
  @Autowired private EleitorRepository eleitorRepository;
  @Autowired private VotacaoRepository votacaoRepository;

  public List<VotacaoDTO> getVotacoesEmCurso() {
    return this.listarVotacoesEmCursoHandler.listarVotacoesEmCurso();
  }

  public VotoDTO getVotoPorOmissao(String eleitorCC, Long votacaoId)
      throws NoSuchEleitorException, NoSuchVotacaoException {
    Optional<Eleitor> eleitorOptional = this.eleitorRepository.findByCc(eleitorCC);
    Eleitor eleitor =
        eleitorOptional.orElseThrow(() -> new NoSuchEleitorException("Eleitor nao existe"));

    Optional<Votacao> votacaoOptional = this.votacaoRepository.findById(votacaoId);
    Votacao votacao =
        votacaoOptional.orElseThrow(() -> new NoSuchVotacaoException("Votacao nao existe"));

    Voto voto = this.votarNumaPropostaHandler.indicaVotacao(votacao, eleitor);

    return voto == null ? null : new VotoDTO(voto);
  }

  public VotoDTO createVoto(String eleitorCC, Long votacaoId, boolean valorVoto)
      throws NoSuchEleitorException, NoSuchVotacaoException, AlreadyVotedVotacaoException {
    Optional<Eleitor> eleitorOptional = this.eleitorRepository.findByCc(eleitorCC);
    Eleitor eleitor =
        eleitorOptional.orElseThrow(() -> new NoSuchEleitorException("Eleitor nao existe"));

    Optional<Votacao> votacaoOptional = this.votacaoRepository.findById(votacaoId);
    Votacao votacao =
        votacaoOptional.orElseThrow(() -> new NoSuchVotacaoException("Votacao nao existe"));

    Voto voto = new Voto(valorVoto, eleitor, votacao);
    this.votarNumaPropostaHandler.indicaVoto(voto);
    return new VotoDTO(voto);
  }
}
