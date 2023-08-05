package pt.ul.fc.css.example.demo.repositories;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacao;
import pt.ul.fc.css.example.demo.associations.EleitorDelegadoAssociacaoId;
import pt.ul.fc.css.example.demo.entities.Delegado;
import pt.ul.fc.css.example.demo.entities.Eleitor;

public interface EleitorDelegadoAssociacaoRepository
    extends JpaRepository<EleitorDelegadoAssociacao, EleitorDelegadoAssociacaoId> {

  @Transactional
  @Query(
      "SELECT ead FROM EleitorDelegadoAssociacao ead WHERE ead.eleitor.id = :eid AND ead.tema.id = :tid")
  Optional<EleitorDelegadoAssociacao> findByEleitorTema(
      @Param("eid") long eid, @Param("tid") long tid);

  @Transactional
  @Query(
      "SELECT ead.eleitor FROM EleitorDelegadoAssociacao ead WHERE ead.delegado.id = :did AND ead.tema.id = :tid")
  List<Eleitor> findEleitoresDelegadoParaTema(@Param("did") long did, @Param("tid") long tid);

  @Modifying
  @Transactional
  @Query(
      "UPDATE EleitorDelegadoAssociacao ead SET ead.delegado = :d WHERE ead.eleitor.id = :eid AND ead.tema.id = :tid")
  void updateDelegado(@Param("eid") long eid, @Param("d") Delegado d, @Param("tid") long tid);
}
