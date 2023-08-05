package pt.ul.fc.css.example.demo.facade.handlers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.repositories.VotacaoRepository;

@Component
public class ListarVotacoesEmCursoHandler {
  private static final int EM_CURSO = 1;

  @Autowired private VotacaoRepository votacaoRepository;

  public ListarVotacoesEmCursoHandler() {}

  public ListarVotacoesEmCursoHandler(VotacaoRepository votacaoRepository) {
    this.votacaoRepository = votacaoRepository;
  }

  public List<VotacaoDTO> listarVotacoesEmCurso() {
    return this.votacaoRepository
        .findByEstado(EstadoValidade.ABERTO)
        .stream()
        .map(v -> new VotacaoDTO(v))
        .collect(Collectors.toList());
  }
}
