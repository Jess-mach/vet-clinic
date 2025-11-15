package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Consultation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByAnimalId(Long animalId);

    List<Consultation> findByAnimalIdOrderByConsultationDateDesc(Long animalId);

    @Query(name = "Consultation.findByFilters")
    Page<Consultation> findByFilters(
            @Param("animalName") String animalName,
            @Param("ownerName") String ownerName,
            @Param("veterinarianName") String veterinarianName,
            @Param("status") String status,
            @Param("reason") String reason,
            @Param("description") String description,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            Pageable pageable
    );

    Optional<Consultation> findById(Long id);
}

