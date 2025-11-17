package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Animal;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByIsActiveTrueOrderByNameAsc();

    Page<Animal> findAllByIsActiveTrueOrderByNameAsc(Pageable pageable);

    Optional<Animal> findByIdAndIsActiveTrue(Long id);

    Optional<Animal> findByMicrochipNumberAndIsActiveTrue(String microchipNumber);

    List<Animal> findBySpeciesAndIsActiveTrue(String species);

    List<Animal> findByOwnerNameContainingIgnoreCaseAndIsActiveTrue(String ownerName);

    List<Animal> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    Page<Animal> findByNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(String name, Pageable pageable);

    Page<Animal> findBySpeciesAndIsActiveTrueOrderByNameAsc(String species, Pageable pageable);

    Page<Animal> findByOwnerNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(String ownerName, Pageable pageable);
}


