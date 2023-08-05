package pt.ul.fc.css.example.demo;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.exceptions.AlreadyExpiredProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.AlreadySupportedProjetoDeLeiException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchProjetoDeLeiException;
import pt.ul.fc.css.example.demo.facade.controllers.RestProjetoDeLeiController;
import pt.ul.fc.css.example.demo.facade.dtos.ProjetoDeLeiDTO;
import pt.ul.fc.css.example.demo.facade.services.ProjetoDeLeiService;

@WebMvcTest(RestProjetoDeLeiController.class)
public class RestProjetoDeLeiTests {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProjetoDeLeiService projetoDeLeiService;

  private ProjetoDeLei proj1;
  private ProjetoDeLei proj2;

  @BeforeEach
  public void setUpTest() {
    Delegado delegado = new Delegado("delegado1", "cc", "token");
    Tema tema = new Tema("t");
    this.proj1 = new ProjetoDeLei("p1", "desc1", new byte[1], tema, LocalDateTime.now(), delegado);
    this.proj2 = new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
  }

  @Test
  public void testGetProjetosDeLeiNaoExpirados() throws Exception {
    List<ProjetoDeLeiDTO> expected =
        Arrays.asList(new ProjetoDeLeiDTO(proj1), new ProjetoDeLeiDTO(proj2));

    when(this.projetoDeLeiService.getProjetosDeLeiNaoExpirados()).thenReturn(expected);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/api/projetos-de-lei/projetos-de-lei-nao-expirados"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expected.size()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].titulo").value(expected.get(0).getTitulo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].titulo").value(expected.get(1).getTitulo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].tema").value(expected.get(0).getTema()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].tema").value(expected.get(1).getTema()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].descricao").value(expected.get(0).getDescricao()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[1].descricao").value(expected.get(1).getDescricao()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].nApoios").value(expected.get(0).getnApoios()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[1].nApoios").value(expected.get(1).getnApoios()));
  }

  @Test
  public void testApoiaProjetoDeLei() throws Exception {
    doNothing().when(this.projetoDeLeiService).apoiaProjetoDeLei("cc1", 1L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.patch("/api/projetos-de-lei/apoia/1/1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testApoiaProjetoDeLeiProjetoDeLeiJaApoiado() throws Exception {
    doThrow(AlreadySupportedProjetoDeLeiException.class)
        .when(this.projetoDeLeiService)
        .apoiaProjetoDeLei("cc1", 1L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.patch("/api/projetos-de-lei/apoia/cc1/1"))
        .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  public void testApoiaProjetoDeLeiProjetoDeLeiForaValidade() throws Exception {
    doThrow(AlreadyExpiredProjetoDeLeiException.class)
        .when(this.projetoDeLeiService)
        .apoiaProjetoDeLei("cc1", 1L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.patch("/api/projetos-de-lei/apoia/cc1/1"))
        .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  public void testApoiaProjetoDeLeiNoSuchEleitor() throws Exception {
    doThrow(NoSuchEleitorException.class)
        .when(this.projetoDeLeiService)
        .apoiaProjetoDeLei("cc1", 1L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.patch("/api/projetos-de-lei/apoia/cc1/1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testApoiaProjetoDeLeiNoSuchProjetoDeLei() throws Exception {
    doThrow(NoSuchProjetoDeLeiException.class)
        .when(this.projetoDeLeiService)
        .apoiaProjetoDeLei("cc1", 1L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.patch("/api/projetos-de-lei/apoia/cc1/1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetProjetoDeLeiDTO() throws Exception {
    ProjetoDeLeiDTO expected = new ProjetoDeLeiDTO(proj1);
    when(this.projetoDeLeiService.getProjetoDeLeiDTOById(1L)).thenReturn(expected);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/api/projetos-de-lei/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.titulo").value(expected.getTitulo()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tema").value(expected.getTema()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value(expected.getDescricao()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nApoios").value(expected.getnApoios()));
  }

  @Test
  public void testGetProjetoDeLeiDTONoSuchProjetoDeLei() throws Exception {
    ProjetoDeLeiDTO expected = new ProjetoDeLeiDTO(proj1);
    doThrow(NoSuchProjetoDeLeiException.class)
        .when(this.projetoDeLeiService)
        .getProjetoDeLeiDTOById(3L);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/api/projetos-de-lei/3"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
