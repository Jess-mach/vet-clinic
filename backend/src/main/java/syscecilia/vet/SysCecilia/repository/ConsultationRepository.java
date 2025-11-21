package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Consultation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>, JpaSpecificationExecutor<Consultation> {

    List<Consultation> findByAnimalId(Long animalId);

    List<Consultation> findByAnimalIdOrderByConsultationDateDesc(Long animalId);

    Optional<Consultation> findById(Long id);

    List<Consultation> findByConsultationDateAndVeterinarianIdAndStatusNot(
            LocalDateTime consultationDate, Long veterinarianId, String status);

    List<Consultation> findByConsultationDateAndAnimalIdAndStatusNot(
            LocalDateTime consultationDate, Long animalId, String status);

    List<Consultation> findByVeterinarianIdAndConsultationDateBetweenAndStatusNotOrderByConsultationDateAsc(
            Long veterinarianId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String status
    );
}

