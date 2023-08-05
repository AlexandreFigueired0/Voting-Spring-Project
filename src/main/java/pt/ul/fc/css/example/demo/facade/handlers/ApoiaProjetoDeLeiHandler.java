package pt.ul.fc.css.example.demo.facade.handlers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.AlreadyExpiredProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.AlreadySupportedProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotoRepository;

@Component
public class ApoiaProjetoDeLeiHandler {

  private static final int NUM_APOIOS_VOTACAO = 10000;
  private static final long MIN_DAYS_VOTACAO = 15;
  private static final long MAX_MONTHS_VOTACAO = 2;

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;
  @Autowired private EleitorRepository eleitorRepository;
  @Autowired private VotacaoRepository votacaoRepository;
  @Autowired private VotoRepository votoRepository;

  public ApoiaProjetoDeLeiHandler() {}

  public ApoiaProjetoDeLeiHandler(
      ProjetoDeLeiRepository projetoDeLeiRepository,
      EleitorRepository eleitorRepository,
      VotacaoRepository votacaoRepository,
      VotoRepository votoRepository) {
    this.projetoDeLeiRepository = projetoDeLeiRepository;
    this.eleitorRepository = eleitorRepository;
    this.votacaoRepository = votacaoRepository;
    this.votoRepository = votoRepository;
  }

  public void apoiaProjetoDeLei(long projetoDeLeiId, Eleitor eleitor)
      throws NoSuchProjetoDeLeiException, AlreadySupportedProjetoDeLeiException,
          AlreadyExpiredProjetoDeLeiException {
    Optional<ProjetoDeLei> p = projetoDeLeiRepository.findById(projetoDeLeiId);
    ProjetoDeLei projetoDeLei =
        p.orElseThrow(() -> new NoSuchProjetoDeLeiException("No Such ProjetoDeLei"));
    if (eleitor.getProjetosDeLeiApoiados().contains(projetoDeLei)) {
      throw new AlreadySupportedProjetoDeLeiException("Projeto De Lei ja apoiado pelo eleitor.");
    }
    if (projetoDeLei.getEstado().equals(EstadoValidade.FECHADO)) {
      throw new AlreadyExpiredProjetoDeLeiException("Projeto De Lei ja expirou.");
    }

    projetoDeLei.setnApoios(projetoDeLei.getnApoios() + 1);
    projetoDeLei.getApoiantes().add(eleitor);

    projetoDeLeiRepository.save(projetoDeLei);
    Votacao novaVotacao = null;
    if (projetoDeLei.getnApoios() == NUM_APOIOS_VOTACAO) {
      novaVotacao = abrirVotacao(projetoDeLei);
      votacaoRepository.save(novaVotacao);
      votoDelegadoProponente(projetoDeLei.getDelegadoProponente(), novaVotacao);
    }
  }

  private Votacao abrirVotacao(ProjetoDeLei projetoDeLei) {
    LocalDateTime dataAtual = LocalDateTime.now();
    LocalDateTime dataProjeto = projetoDeLei.getDataValidade();
    LocalDateTime dataVotacao = dataProjeto;
    // Data de expiracao com  menos de 15 dias
    if (dataAtual.plusDays(MIN_DAYS_VOTACAO).compareTo(dataProjeto) > 0) {
      dataVotacao = dataAtual.plusDays(MIN_DAYS_VOTACAO);
    }
    // Data de expiracao com  mais de 2 meses
    else if (dataAtual.plusMonths(MAX_MONTHS_VOTACAO).compareTo(dataProjeto) < 0) {
      dataVotacao = dataAtual.plusMonths(MAX_MONTHS_VOTACAO);
    }
    Votacao votacao =
        new Votacao(EstadoValidade.ABERTO, null, 1, 0, new HashSet<>(), dataVotacao, projetoDeLei);
    return votacao;
  }

  private void votoDelegadoProponente(Delegado delegadoProponente, Votacao votacao) {
    Voto votoDelegadoProponente = new Voto(true, delegadoProponente, votacao);
    this.votoRepository.save(votoDelegadoProponente);
  }
}
