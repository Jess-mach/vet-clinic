package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Animal;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByIsActiveTrueOrderByNameAsc();

    Optional<Animal> findByIdAndIsActiveTrue(Long id);

    Optional<Animal> findByMicrochipNumberAndIsActiveTrue(String microchipNumber);

    List<Animal> findBySpeciesAndIsActiveTrue(String species);

    List<Animal> findByOwnerNameContainingIgnoreCaseAndIsActiveTrue(String ownerName);

    List<Animal> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
}


