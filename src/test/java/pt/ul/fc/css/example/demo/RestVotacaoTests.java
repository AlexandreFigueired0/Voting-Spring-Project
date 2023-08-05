package pt.ul.fc.css.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.entities.Tema;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.exceptions.AlreadyVotedVotacaoException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchEleitorException;
import pt.ul.fc.css.example.demo.exceptions.NoSuchVotacaoException;
import pt.ul.fc.css.example.demo.facade.controllers.RestVotacaoController;
import pt.ul.fc.css.example.demo.facade.dtos.VotacaoDTO;
import pt.ul.fc.css.example.demo.facade.dtos.VotoDTO;
import pt.ul.fc.css.example.demo.facade.services.VotacaoService;

@WebMvcTest(RestVotacaoController.class)
public class RestVotacaoTests {

  private static final String BASE_URL = "/api/votacoes";

  @Autowired ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private VotacaoService votacaoService;

  private Votacao vot1;
  private Votacao vot2;

  @BeforeEach
  public void setUpTest() {
    Delegado delegado = new Delegado("delegado1", "cc", "token");
    delegado.setId(1);
    Tema tema = new Tema("t");
    tema.setId(1);
    ProjetoDeLei proj1 =
        new ProjetoDeLei("p1", "desc1", new byte[1], tema, LocalDateTime.now(), delegado);
    ProjetoDeLei proj2 =
        new ProjetoDeLei("p2", "desc2", new byte[1], tema, LocalDateTime.now(), delegado);
    proj1.setId(1);
    proj1.setId(2);
    this.vot1 =
        new Votacao(
            EstadoValidade.ABERTO, null, 0, 0, null, LocalDateTime.now().plusMonths(1), proj1);
    this.vot2 =
        new Votacao(
            EstadoValidade.ABERTO, null, 0, 0, null, LocalDateTime.now().plusMonths(1), proj2);
    vot1.setId(1);
    vot1.setId(2);
  }

  @Test
  void getVotacoesListTest() throws Exception {
    List<VotacaoDTO> expected = Arrays.asList(new VotacaoDTO(vot1), new VotacaoDTO(vot2));

    when(this.votacaoService.getVotacoesEmCurso()).thenReturn(expected);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(this.BASE_URL + "/votacoes-em-curso"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(expected.size())))
        .andExpect(jsonPath("$[0].id").value(expected.get(0).getId()))
        .andExpect(jsonPath("$[1].id").value(expected.get(1).getId()));
  }

  @Test
  void getVotoOmissaoSucessoTest() throws Exception {
    VotoDTO expected = new VotoDTO();
    expected.setValorVoto(true);
    expected.setVotacaoId(1);
    expected.setEleitorCC("cc1");

    when(this.votacaoService.getVotoPorOmissao("cc1", 1L)).thenReturn(expected);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(this.BASE_URL + "/voto-omissao/cc1/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eleitorCC").value(expected.getEleitorCC()))
        .andExpect(jsonPath("$.votacaoId").value(expected.getVotacaoId()))
        .andExpect(jsonPath("$.valorVoto").value(expected.getValorVoto()));
  }

  @Test
  void getVotoOmissaoEleitorNaoExisteTest() throws Exception {
    when(this.votacaoService.getVotoPorOmissao("cc10", 1L))
        .thenThrow(new NoSuchEleitorException("Eleitor nao exsite"));

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(this.BASE_URL + "/voto-omissao/cc10/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getVotoOmissaoVotacaoNaoExisteTest() throws Exception {
    when(this.votacaoService.getVotoPorOmissao("cc1", 10L))
        .thenThrow(new NoSuchVotacaoException("Votacao nao exsite"));

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(this.BASE_URL + "/voto-omissao/cc1/10"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getVotoOmissaoVotoPorOmissaoNaoExisteTest() throws Exception {
    when(this.votacaoService.getVotoPorOmissao("cc1", 1L)).thenReturn(null);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(this.BASE_URL + "/voto-omissao/cc1/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void postVotoSucessoTest() throws Exception {
    VotoDTO expected = new VotoDTO();
    expected.setValorVoto(true);
    expected.setVotacaoId(1);
    expected.setEleitorCC("cc1");

    when(this.votacaoService.createVoto("cc1", 1L, true)).thenReturn(expected);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(this.BASE_URL + "/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(expected));

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eleitorCC").value(expected.getEleitorCC()))
        .andExpect(jsonPath("$.valorVoto").value(expected.getValorVoto()))
        .andExpect(jsonPath("$.votacaoId").value(expected.getVotacaoId()));
  }

  @Test
  void postVotoEleitorNaoExisteTest() throws Exception {
    VotoDTO expected = new VotoDTO();
    expected.setValorVoto(true);
    expected.setVotacaoId(1);
    expected.setEleitorCC("cc10");

    when(this.votacaoService.createVoto("cc10", 1L, true))
        .thenThrow(new NoSuchEleitorException("Eleitor nao existe"));

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(this.BASE_URL + "/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(expected));

    this.mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }

  @Test
  void postVotoVotacaoNaoExisteTest() throws Exception {
    VotoDTO expected = new VotoDTO();
    expected.setValorVoto(true);
    expected.setVotacaoId(10);
    expected.setEleitorCC("cc1");

    when(this.votacaoService.createVoto("cc1", 10L, true))
        .thenThrow(new NoSuchVotacaoException("Votacao nao existe"));

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(this.BASE_URL + "/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(expected));

    this.mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }

  @Test
  void postVotoJaVotouTest() throws Exception {
    VotoDTO expected = new VotoDTO();
    expected.setValorVoto(true);
    expected.setVotacaoId(1);
    expected.setEleitorCC("cc1");

    when(this.votacaoService.createVoto("cc1", 1L, true))
        .thenThrow(new AlreadyVotedVotacaoException("Ja votou nesta votacao"));

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post(this.BASE_URL + "/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(expected));

    this.mockMvc.perform(mockRequest).andExpect(status().isConflict());
  }
}
