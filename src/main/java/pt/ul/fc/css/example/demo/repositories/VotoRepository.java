package pt.ul.fc.css.example.demo.repositories;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.associations.Voto;
import pt.ul.fc.css.example.demo.associations.VotoId;
import pt.ul.fc.css.example.demo.entities.Delegado;

public interface VotoRepository extends JpaRepository<Voto, VotoId> {

  @Transactional
  @Query("SELECT v FROM Voto v WHERE v.eleitor.id = :eid AND v.votacao.id = :vid")
  Optional<Voto> findByEleitorVotacao(@Param("eid") long eid, @Param("vid") long vid);

  @Transactional
  @Query("SELECT v.eleitor FROM Voto v WHERE v.votacao.id = :vid AND TYPE(v.eleitor) = Delegado")
  List<Delegado> findDelegadosQueVotaram(@Param("vid") long vid);
}
