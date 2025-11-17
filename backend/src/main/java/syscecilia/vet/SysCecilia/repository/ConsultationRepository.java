package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Consultation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>, JpaSpecificationExecutor<Consultation> {

    List<Consultation> findByAnimalId(Long animalId);

    List<Consultation> findByAnimalIdOrderByConsultationDateDesc(Long animalId);

    Optional<Consultation> findById(Long id);
}

