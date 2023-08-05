package pt.ul.fc.css.example.demo.facade.handlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.associations.VotoId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.enums.ResultadoVotacao;
import pt.ul.fc.css.example.demo.repositories.EleitorDelegadoAssociacaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;
import pt.ul.fc.css.example.demo.repositories.VotoRepository;

@Configuration
@EnableScheduling
public class FecharVotacaoHandler {

  @Autowired private VotoRepository votoRepository;

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  public FecharVotacaoHandler(
      VotoRepository votoRepository,
      EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository,
      VotacaoRepository votacaoRepository) {
    this.votoRepository = votoRepository;
    this.eleitorDelegadoAssociacaoRepository = eleitorDelegadoAssociacaoRepository;
    this.votacaoRepository = votacaoRepository;
  }

  public FecharVotacaoHandler() {}

  @Scheduled(fixedRate = 15000)
  public void fecharVotacoesForaValidade() {
    List<Votacao> votacoes = votacaoRepository.findVotacoesForaValidade(LocalDateTime.now());

    for (Votacao votacao : votacoes) {
      fecharVotacao(votacao);
    }
  }

  public void fecharVotacao(Votacao votacao) {
    // Obter tema
    Tema tema = votacao.getProjetoDeLei().getTema();

    List<Delegado> delegadosQueVotaram = votoRepository.findDelegadosQueVotaram(votacao.getId());

    // ATE CHEGAR AO TEMA QUE NAO TEM PAI
    while (tema != null) {
      // LOOP PARA CADA DELEGADO QUE VOTOU NESTA VOTACAO
      for (Delegado delegado : delegadosQueVotaram) {
        // GET ELEITORES QUE ELE REPRESENTA PARA O DADO TEMA
        List<Eleitor> eleitoresParaTema =
            eleitorDelegadoAssociacaoRepository.findEleitoresDelegadoParaTema(
                delegado.getId(), tema.getId());
        // LOOP PARA CADA ELEITOR
        for (Eleitor eleitor : eleitoresParaTema) {
          // SE AINDA NAO VOTOU
          VotoId votoId = new VotoId(eleitor.getId(), votacao.getId());
          Optional<Voto> v = votoRepository.findById(votoId);
          if (v.isEmpty()) {
            // METE O VOTO DO DELEGADO COMO VOTO DESTE ELEITOR
            votoId = new VotoId(delegado.getId(), votacao.getId());
            Voto votoDelegado = votoRepository.findById(votoId).get();
            Voto votoEleitor = new Voto(votoDelegado.isValorVoto(), eleitor, votacao);
            votoEleitor.setValorVoto(null);
            votoRepository.save(votoEleitor);
            if (votoDelegado.isValorVoto()) {
              votacaoRepository.updateVotosPositivos(votacao.getId());
            } else {
              votacaoRepository.updateVotosNegativos(votacao.getId());
            }
          }
        }
      }
      // ATUALIZA TEMA PARA O PAI DO TEMA ATUAL++
      tema = tema.getTemaPai();
    }

    if (votacao.getVotosPositivos() > votacao.getVotosNegativos()) {
      votacaoRepository.updateVotacaoComoTerminada(
          votacao.getId(), ResultadoVotacao.APROVADO, EstadoValidade.FECHADO);
      return;
    }

    votacaoRepository.updateVotacaoComoTerminada(
        votacao.getId(), ResultadoVotacao.REJEITADO, EstadoValidade.FECHADO);
  }
}
