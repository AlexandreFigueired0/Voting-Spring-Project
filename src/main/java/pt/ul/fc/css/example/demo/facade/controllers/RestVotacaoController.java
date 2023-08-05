package pt.ul.fc.css.example.demo.facade.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchVotacaoException;
import pt.ul.fc.css.example.demo.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.facade.dtos.VotoDTO;
import pt.ul.fc.css.example.demo.facade.services.VotacaoService;
import pt.ul.fc.css.example.demo.repositories.*;

@RestController()
@RequestMapping("api/votacoes")
public class RestVotacaoController {

  @Autowired private VotacaoService votacaoService;

  @GetMapping("/votacoes-em-curso")
  List<VotacaoDTO> votacoesEmCurso() {
    return votacaoService.getVotacoesEmCurso();
  }

  @GetMapping("/voto-omissao/{eleitorCC}/{votacaoId}")
  ResponseEntity<?> votoPorOmissao(@PathVariable String eleitorCC, @PathVariable Long votacaoId) {
    VotoDTO votoPorOmissaoDTO = null;
    try {
      votoPorOmissaoDTO = votacaoService.getVotoPorOmissao(eleitorCC, votacaoId);

    } catch (NoSuchEleitorException | NoSuchVotacaoException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (votoPorOmissaoDTO != null) {
      return ResponseEntity.ok().body(votoPorOmissaoDTO);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("/votos")
  ResponseEntity<?> createVoto(@RequestBody VotoDTO votoDto) {
    try {
      VotoDTO v =
          this.votacaoService.createVoto(
              votoDto.getEleitorCC(), votoDto.getVotacaoId(), votoDto.getValorVoto());
      return ResponseEntity.ok().body(v);
    } catch (NoSuchEleitorException | NoSuchVotacaoException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (AlreadyVotedVotacaoException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }
}
