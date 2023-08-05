package pt.ul.fc.css.example.demo.facade.handlers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.repositories.ProjetoDeLeiRepository;

@Configuration
@EnableScheduling
public class FecharProjetosDeLeiHandler {

  @Autowired private ProjetoDeLeiRepository projetoDeLeiRepository;

  public FecharProjetosDeLeiHandler(ProjetoDeLeiRepository projetoDeLeiRepository) {
    this.projetoDeLeiRepository = projetoDeLeiRepository;
  }

  public FecharProjetosDeLeiHandler() {}

  @Scheduled(fixedRate = 15000)
  public void fecharProjetosDeLei() {
    projetoDeLeiRepository.fechaExpirados(LocalDateTime.now(), EstadoValidade.FECHADO);
  }
}
