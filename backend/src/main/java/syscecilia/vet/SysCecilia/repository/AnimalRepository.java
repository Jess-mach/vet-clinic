package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Animal;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByOrderByNameAsc();

    Optional<Animal> findByMicrochipNumber(String microchipNumber);

    List<Animal> findBySpecies(String species);

    List<Animal> findByOwnerNameContainingIgnoreCase(String ownerName);

    List<Animal> findByNameContainingIgnoreCase(String name);
}


