package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Consultation;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByAnimalId(Long animalId);

    List<Consultation> findByAnimalIdOrderByConsultationDateDesc(Long animalId);
}

