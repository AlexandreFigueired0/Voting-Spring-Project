package pt.ul.fc.css.example.demo.repositories;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.Votacao;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;
import pt.ul.fc.css.example.demo.enums.ResultadoVotacao;

public interface VotacaoRepository extends JpaRepository<Votacao, Long> {

  @Transactional
  List<Votacao> findByEstado(EstadoValidade estado);

  @Transactional
  @Query("SELECT v FROM Votacao v WHERE v.dataValidade < :curr_date")
  List<Votacao> findVotacoesForaValidade(@Param("curr_date") LocalDateTime currDate);

  @Modifying
  @Transactional
  @Query("UPDATE Votacao v SET v.votosPositivos = v.votosPositivos+1 WHERE v.id = :vid")
  void updateVotosPositivos(@Param("vid") long vid);

  @Modifying
  @Transactional
  @Query("UPDATE Votacao v SET v.votosNegativos = v.votosNegativos+1 WHERE v.id = :vid")
  void updateVotosNegativos(@Param("vid") long vid);

  @Modifying
  @Transactional
  @Query("UPDATE Votacao v SET v.estado = :estado, v.resultado = :res WHERE v.id = :vid")
  void updateVotacaoComoTerminada(
      @Param("vid") long vid,
      @Param("res") ResultadoVotacao res,
      @Param("estado") EstadoValidade estado);
}
