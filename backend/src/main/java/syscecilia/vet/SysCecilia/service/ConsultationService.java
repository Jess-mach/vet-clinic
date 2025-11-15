package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ConsultationResponse> findAllByAnimalId(Long animalId) {
        verifyAnimalExists(animalId);
        List<Consultation> consultations = consultationRepository.findByAnimalIdOrderByConsultationDateDesc(animalId);
        return consultations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultationResponse> findAll() {
        List<Consultation> consultations = consultationRepository.findAll();
        return consultations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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

