package pt.ul.fc.css.example.demo.facade.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.demo.exceptions.AlreadyExpiredProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.AlreadySupportedProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.facade.services.ProjetoDeLeiService;

@RestController()
@RequestMapping("api/projetos-de-lei")
public class RestProjetoDeLeiController {

  @Autowired private ProjetoDeLeiService projetoDeLeiService;

  @GetMapping("/projetos-de-lei-nao-expirados")
  List<ProjetoDeLeiDTO> getProjetosDeLeiNaoExpirados() {
    return this.projetoDeLeiService.getProjetosDeLeiNaoExpirados();
  }

  @GetMapping("/{projetoId}")
  ResponseEntity<?> getProjetoDeLeiDTO(@PathVariable Long projetoId) {
    try {
      return ResponseEntity.ok().body(this.projetoDeLeiService.getProjetoDeLeiDTOById(projetoId));
    } catch (NoSuchProjetoDeLeiException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PatchMapping("/apoia/{eleitorCC}/{projetoDeLeiId}")
  ResponseEntity<?> apoiaProjetoDeLei(
      @PathVariable String eleitorCC, @PathVariable Long projetoDeLeiId) {
    try {
      projetoDeLeiService.apoiaProjetoDeLei(eleitorCC, projetoDeLeiId);
    } catch (NoSuchProjetoDeLeiException | NoSuchEleitorException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (AlreadySupportedProjetoDeLeiException | AlreadyExpiredProjetoDeLeiException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/download-pdf/{id}")
  public ResponseEntity<byte[]> downloadPDF(@PathVariable("id") long id) {
    ProjetoDeLeiDTO projeto = null;
    try {
      projeto = projetoDeLeiService.getProjetoDeLeiDTOById(id);
    } catch (NoSuchProjetoDeLeiException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    byte[] pdfBytes = projeto.getAnexoPdf();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.attachment().filename("anexo-proj" + id + ".pdf").build());

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
  }
}
