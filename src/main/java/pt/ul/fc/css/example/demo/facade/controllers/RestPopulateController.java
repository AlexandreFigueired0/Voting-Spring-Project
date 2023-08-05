package pt.ul.fc.css.example.demo.facade.controllers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.repositories.*;

@RestController()
@RequestMapping("api/populate")
public class RestPopulateController {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;
  @Autowired private EleitorRepository eleitorRepository;
  @Autowired private TemaRepository temaRepository;

  @Autowired private VotacaoRepository votacaoRepository;

  @Autowired private VotoRepository votoRepository;

  @Autowired private EleitorDelegadoAssociacaoRepository eleitorDelegadoAssociacaoRepository;

  @GetMapping("/test")
  ResponseEntity<?> populate(final Model model) {
    Eleitor eleitor = new Eleitor("eleitor", "cc1", "tok1");
    eleitorRepository.save(eleitor);
    Delegado delegado = new Delegado("delegado1", "cc", "token");
    eleitorRepository.save(delegado);
    Tema tema = new Tema("t");
    temaRepository.save(tema);
    ProjetoDeLei proj1 =
        new ProjetoDeLei(
            "p1", "desc1", new byte[1], tema, LocalDateTime.now().plusMonths(1), delegado);
    projetoDeLeiRepository.save(proj1);
    Votacao votacao =
        new Votacao(
            EstadoValidade.ABERTO, null, 1, 0, null, LocalDateTime.now().plusMonths(1), proj1);
    votacaoRepository.save(votacao);

    Voto v = new Voto(true, delegado, votacao);
    votoRepository.save(v);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
