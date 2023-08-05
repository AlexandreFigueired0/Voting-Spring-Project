package pt.ul.fc.css.example.demo.facade.controllers;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchVotacaoException;
import pt.ul.fc.css.example.demo.facade.dtos.VotoDTO;
import pt.ul.fc.css.example.demo.facade.services.EleitorService;
import pt.ul.fc.css.example.demo.facade.services.VotacaoService;

@Controller
public class WebVotacaoController {

  @Autowired private VotacaoService votacaoService;
  @Autowired private EleitorService eleitorService;

  @GetMapping("/votacoes-em-curso")
  public String getVotacoesEmCurso(final Model model) {
    model.addAttribute("votacoesEmCurso", this.votacaoService.getVotacoesEmCurso());
    model.addAttribute("currentPage", "listaVotacoes");
    return "votacoes_em_curso_list";
  }

  @GetMapping("/voto-omissao-forms")
  public String getFormularioVotoOmissao(final Model model) {
    model.addAttribute("voto", new VotoDTO());
    return "voto_omissao_forms";
  }

  @GetMapping("/voto-omissao")
  public String getVotoPorOmissao(final Model model, @ModelAttribute VotoDTO voto) {
    try {
      model.addAttribute(
          "votoPorOmissao",
          this.votacaoService.getVotoPorOmissao(voto.getEleitorCC(), voto.getVotacaoId()));
    } catch (NoSuchEleitorException e) {
      model.addAttribute("errorMsg", "Eleitor nao existe");
      return "error/404";
    } catch (NoSuchVotacaoException e) {
      model.addAttribute("errorMsg", "Votacao nao existe");
      return "error/404";
    }
    return "voto_details";
  }

  @GetMapping("/votos")
  public String getFormularioVoto(final Model model) {
    model.addAttribute("novoVoto", new VotoDTO());
    List<Boolean> listaValoresVotos = Arrays.asList(true, false);
    model.addAttribute("valoresVoto", listaValoresVotos);
    return "voto_forms";
  }

  @PostMapping("/votos")
  public String createVoto(final Model model, @ModelAttribute VotoDTO voto) {
    VotoDTO voto2;
    try {
      voto2 =
          votacaoService.createVoto(voto.getEleitorCC(), voto.getVotacaoId(), voto.getValorVoto());
    } catch (NoSuchEleitorException e) {
      model.addAttribute("errorMsg", "Eleitor nao existe");
      return "error/404";
    } catch (NoSuchVotacaoException e) {
      model.addAttribute("errorMsg", "Votacao nao existe");
      return "error/404";
    } catch (AlreadyVotedVotacaoException e) {
      model.addAttribute("errorMsg", "Ja votou nesta votacao");
      return "error/409";
    }
    return "voto_sucesso";
  }
}
