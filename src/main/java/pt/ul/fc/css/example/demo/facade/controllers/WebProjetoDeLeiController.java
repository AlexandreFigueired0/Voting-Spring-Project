package pt.ul.fc.css.example.demo.facade.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.facade.dtos.EleitorApoiaProjetoDTO;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.facade.services.ProjetoDeLeiService;
import pt.ul.fc.css.example.demo.repositories.EleitorRepository;

@Controller
@RequestMapping("projetos-de-lei")
public class WebProjetoDeLeiController {

  @Autowired private ProjetoDeLeiService projetoDeLeiService;

  @Autowired private EleitorRepository eleitorRepository;

  @GetMapping("")
  public String listarProjetosDeLei(final Model model) {
    model.addAttribute("projetosDeLei", this.projetoDeLeiService.getProjetosDeLeiNaoExpirados());
    model.addAttribute("currentPage", "listaProjetos");
    return "projetos_de_lei_list";
  }

  @GetMapping("/apoia")
  public String apoiaProjetoDeLeiView(final Model model) {
    model.addAttribute("eap", new EleitorApoiaProjetoDTO());
    model.addAttribute("currentPage", "apoiaProjetos");
    return "projeto_de_lei_apoia";
  }

  @PostMapping("/apoia")
  public String apoiaProjetoDeLei(
      final Model model, @ModelAttribute EleitorApoiaProjetoDTO eapDTO) {
    try {
      projetoDeLeiService.apoiaProjetoDeLei(eapDTO.getEleitorCC(), eapDTO.getProjetoId());
    } catch (NoSuchProjetoDeLeiException e) {
      model.addAttribute("errorMsg", "Projeto de Lei nao existe");
      return "error/404";
    } catch (NoSuchEleitorException e) {
      model.addAttribute("errorMsg", "Eleitor nao existe");
      return "error/404";
    } catch (AlreadySupportedProjetoDeLeiException e) {
      model.addAttribute("errorMsg", "Ja apoiou este projeto de lei");
      return "error/409";
    } catch (AlreadyExpiredProjetoDeLeiException e) {
      model.addAttribute("errorMsg", "Projeto de lei expirado");
      return "error/409";
    }
    return "projeto_apoiado_sucesso";
  }

  @GetMapping("/{projetoId}")
  public String projetoDeLeiDetails(final Model model, @PathVariable Long projetoId) {
    ProjetoDeLeiDTO projetoDeLeiDTO;
    try {
      projetoDeLeiDTO = this.projetoDeLeiService.getProjetoDeLeiDTOById(projetoId);
    } catch (NoSuchProjetoDeLeiException e) {
      model.addAttribute("errorMsg", "Projeto de Lei nao existe");
      return "error/404";
    }
    model.addAttribute("projeto", projetoDeLeiDTO);
    return "projeto_de_lei_details";
  }

  @GetMapping("/apresenta")
  public String apresentaProjetoDeLeiView(final Model model) {
    model.addAttribute("projeto", new ProjetoDeLeiDTO());
    model.addAttribute("currentPage", "apresentaProjetos");
    return "projeto_de_lei_apresenta";
  }

  @PostMapping("")
  public String apresentaProjetoDeLei(
      final Model model,
      @ModelAttribute ProjetoDeLeiDTO proj,
      @RequestParam("pdfFile") MultipartFile pdfFile) {
    ProjetoDeLeiDTO projeto = null;
    System.out.println(proj.getIdDelegadoProponente());
    try {
      if (!pdfFile.isEmpty()) {
        byte[] pdfContent = pdfFile.getBytes();
        projeto =
            projetoDeLeiService.apresentaProjetoDeLei(
                proj.getTitulo(),
                proj.getTema(),
                proj.getCcDelegadoProponente(),
                proj.getDataValidadeString(),
                proj.getDescricao(),
                pdfContent);
      } else {
        projeto =
            projetoDeLeiService.apresentaProjetoDeLei(
                proj.getTitulo(),
                proj.getTema(),
                proj.getCcDelegadoProponente(),
                proj.getDataValidadeString(),
                proj.getDescricao(),
                new byte[1]);
      }
    } catch (NoSuchTemaException e) {
      model.addAttribute("errorMsg", "Tema nao existe");
      return "error/404";
    } catch (NoSuchDelegadoException e) {
      model.addAttribute("errorMsg", "Delegado nao existe");
      return "error/404";
    } catch (DataSuperiorAUmAnoException e) {
      model.addAttribute("errorMsg", "Data de validade superior a um ano");
      return "error/406";
    } catch (IOException e) {
      model.addAttribute("errorMsg", "Erro durante a transferencia do anexo do Projeto de Lei");
      return "error/500";
    }
    return "redirect:/projetos-de-lei/" + projeto.getId();
  }
}
