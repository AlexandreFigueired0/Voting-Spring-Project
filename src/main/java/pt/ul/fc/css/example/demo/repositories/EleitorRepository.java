package pt.ul.fc.css.example.demo.repositories;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;

public interface EleitorRepository extends JpaRepository<Eleitor, Long> {

  @Query("SELECT e FROM Eleitor e WHERE e.id = :id AND TYPE(e) = Delegado")
  @Transactional
  Optional<Delegado> findDelegadoById(@Param("id") long id);

  @Transactional
  Optional<Eleitor> findByCc(String cc);

  @Query("SELECT e From Eleitor e Where e.cc = :cc AND TYPE(e) = Delegado")
  @Transactional
  Optional<Delegado> findDelegadoByCC(@Param("cc") String cc);
}
