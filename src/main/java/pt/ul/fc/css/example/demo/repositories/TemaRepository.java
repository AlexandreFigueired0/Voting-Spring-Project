package pt.ul.fc.css.example.demo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

  Optional<Tema> findByNome(String nome);
}
