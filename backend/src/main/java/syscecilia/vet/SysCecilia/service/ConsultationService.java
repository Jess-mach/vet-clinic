package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationSpecification;

import java.time.LocalDateTime;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AnimalRepository animalRepository;

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository, AnimalRepository animalRepository) {
        this.consultationRepository = consultationRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional(readOnly = true)
    public ConsultationResponse findById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));
        return convertToResponse(consultation);
    }

    @Transactional(readOnly = true)
    public Page<ConsultationResponse> findByFilters(
            String animalName,
            String ownerName,
            String veterinarianName,
            String status,
            String reason,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            Pageable pageable) {
        Page<Consultation> consultations = consultationRepository.findAll(
                ConsultationSpecification.withFilters(
                        animalName,
                        ownerName,
                        veterinarianName,
                        status,
                        reason,
                        description,
                        createdAtStart,
                        createdAtEnd
                ),
                pageable
        );
        return consultations.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ConsultationResponse> findAll(Pageable pageable) {
        Page<Consultation> consultations = consultationRepository.findAll(pageable);
        return consultations.map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable) {
        verifyAnimalExists(animalId);
        return findByFilters(null, null, null, null, null, null, null, null, pageable);
    }

    private void verifyAnimalExists(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new ResourceNotFoundException("Animal not found with id: " + animalId);
        }
    }

    private ConsultationResponse convertToResponse(Consultation consultation) {
        Animal animal = consultation.getAnimal();
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo(
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getOwnerName()
        );

        return new ConsultationResponse(
                consultation.getId(),
                animalBasicInfo,
                consultation.getConsultationDate(),
                consultation.getVeterinarianName(),
                consultation.getReason(),
                consultation.getDescription(),
                consultation.getDiagnosis(),
                consultation.getTreatmentPrescribed(),
                consultation.getObservations(),
                consultation.getNextAppointmentDate(),
                consultation.getStatus(),
                consultation.getCreatedAt(),
                consultation.getUpdatedAt()
        );
    }
}

