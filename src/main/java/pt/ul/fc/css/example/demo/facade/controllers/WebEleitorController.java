package pt.ul.fc.css.example.demo.facade.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.exceptions.NoSuchDelegadoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchTemaException;
import pt.ul.fc.css.example.demo.facade.dtos.EleitorDelegadoAssociacaoDTO;
import pt.ul.fc.css.example.demo.facade.services.EleitorService;

@Controller
public class WebEleitorController {

  @Autowired private EleitorService eleitorService;

  @GetMapping("/escolhe-delegado")
  public String escolheDelegado(final Model model) {
    model.addAttribute("eleitorDelegadoAssociacaoDTO", new EleitorDelegadoAssociacaoDTO());
    model.addAttribute("currentPage", "escolheDelegado");
    return "escolhe-delegado";
  }

  @PostMapping("/escolhe-delegado")
  public String escolheDelegadoAction(
      final Model model, @ModelAttribute EleitorDelegadoAssociacaoDTO eadDTO) {
    try {
      this.eleitorService.escolherDelegado(
          eadDTO.getTema(), eadDTO.getEleitorCC(), eadDTO.getDelegadoCC());
    } catch (NoSuchDelegadoException e) {
      model.addAttribute("errorMsg", "Delegado nao existe");
      return "error/404";
    } catch (NoSuchTemaException e) {
      model.addAttribute("errorMsg", "Tema nao existe");
      return "error/404";
    } catch (NoSuchEleitorException e) {
      model.addAttribute("errorMsg", "Eleitor nao existe");
      return "error/404";
    }
    return "escolher_delegado_sucesso";
  }
}
