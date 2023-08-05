package pt.ul.fc.css.example.demo.facade.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;

@Component
public class ConsultarProjetosDeLeiHandler {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  public ConsultarProjetosDeLeiHandler() {}

  public ConsultarProjetosDeLeiHandler(ProjetoDeLeiRepository projetoDeLeiRepository) {
    this.projetoDeLeiRepository = projetoDeLeiRepository;
  }

  public List<ProjetoDeLeiDTO> listarProjetosDeLei() {
    return this.projetoDeLeiRepository
        .findByEstado(EstadoValidade.ABERTO)
        .stream()
        .map(p -> new ProjetoDeLeiDTO(p))
        .collect(Collectors.toList());
  }

  public ProjetoDeLeiDTO getProjetoDeLei(long projetoDeLeiId) throws NoSuchProjetoDeLeiException {
    Optional<ProjetoDeLei> p = this.projetoDeLeiRepository.findById(projetoDeLeiId);
    ProjetoDeLei projetoDeLei =
        p.orElseThrow(() -> new NoSuchProjetoDeLeiException("No such Projeto de Lei"));
    return new ProjetoDeLeiDTO(projetoDeLei);
  }
}
