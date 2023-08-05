package pt.ul.fc.css.example.demo.repositories;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.ProjetoDeLei;
import pt.ul.fc.css.example.demo.enums.EstadoValidade;

public interface ProjetoDeLeiRepository extends JpaRepository<ProjetoDeLei, Long> {

  @Transactional
  List<ProjetoDeLei> findByEstado(EstadoValidade estado);

  @Modifying
  @Transactional
  @Query("UPDATE ProjetoDeLei proj SET proj.estado = :est WHERE proj.dataValidade < :curr_date")
  void fechaExpirados(@Param("curr_date") LocalDateTime currDate, @Param("est") EstadoValidade est);
}
