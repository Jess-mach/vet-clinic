package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.ConsultationRequest;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.exception.BusinessException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationSpecification;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    @Transactional
    public ConsultationResponse create(ConsultationRequest request) {
        Animal animal = animalRepository.findByIdAndIsActiveTrue(request.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + request.getAnimalId()));

        LocalDateTime consultationDate = request.getConsultationDate();
        String veterinarianName = request.getVeterinarianName();

        // Validar regras de negócio
        validateBusinessRules(consultationDate, veterinarianName, request.getAnimalId());

        Consultation consultation = convertToEntity(request, animal);
        Consultation savedConsultation = consultationRepository.save(consultation);

        return convertToResponse(savedConsultation);
    }

    @Transactional
    public ConsultationResponse cancelConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));

        if ("CANCELLED".equals(consultation.getStatus())) {
            throw new BusinessException("Consultation is already cancelled");
        }

        if ("COMPLETED".equals(consultation.getStatus())) {
            throw new BusinessException("Cannot cancel a completed consultation");
        }

        consultation.setStatus("CANCELLED");
        consultation = consultationRepository.save(consultation);

        return convertToResponse(consultation);
    }

    private void verifyAnimalExists(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new ResourceNotFoundException("Animal not found with id: " + animalId);
        }
    }

    private void validateBusinessRules(LocalDateTime consultationDate, String veterinarianName, Long animalId) {
        // Validar horário de funcionamento
        validateClinicHours(consultationDate);

        // Validar se já existe consulta no mesmo horário para o mesmo veterinário
        validateVeterinarianAvailability(consultationDate, veterinarianName);

        // Validar se já existe consulta no mesmo horário para o mesmo animal
        validateAnimalAvailability(consultationDate, animalId);
    }

    private void validateClinicHours(LocalDateTime consultationDate) {
        DayOfWeek dayOfWeek = consultationDate.getDayOfWeek();
        LocalTime time = consultationDate.toLocalTime();
        LocalTime openingTime = LocalTime.of(7, 0);
        LocalTime closingTime;
        LocalTime lastAppointmentTime;

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            // Sábado: 7:00 às 14:00, último agendamento às 13:00
            closingTime = LocalTime.of(14, 0);
            lastAppointmentTime = LocalTime.of(13, 0);
        } else {
            // Segunda a Sexta: 7:00 às 19:00, último agendamento às 18:00
            closingTime = LocalTime.of(19, 0);
            lastAppointmentTime = LocalTime.of(18, 0);
        }

        if (time.isBefore(openingTime)) {
            throw new BusinessException(
                    "Consultation cannot be scheduled before clinic opening time (07:00)",
                    "The clinic opens at 07:00. Please schedule the consultation after this time."
            );
        }

        if (time.isAfter(lastAppointmentTime)) {
            String dayName = dayOfWeek == DayOfWeek.SATURDAY ? "Saturday" : "weekday";
            String lastTime = dayOfWeek == DayOfWeek.SATURDAY ? "13:00" : "18:00";
            throw new BusinessException(
                    String.format("Consultation cannot be scheduled after last appointment time (%s) on %s", lastTime, dayName),
                    String.format("The last appointment time is %s. Please schedule the consultation before this time.", lastTime)
            );
        }

        if (time.isAfter(closingTime) || time.equals(closingTime)) {
            String dayName = dayOfWeek == DayOfWeek.SATURDAY ? "Saturday" : "weekday";
            String closingTimeStr = dayOfWeek == DayOfWeek.SATURDAY ? "14:00" : "19:00";
            throw new BusinessException(
                    String.format("Consultation cannot be scheduled at or after clinic closing time (%s) on %s", closingTimeStr, dayName),
                    String.format("The clinic closes at %s. Please schedule the consultation before this time.", closingTimeStr)
            );
        }
    }

    private void validateVeterinarianAvailability(LocalDateTime consultationDate, String veterinarianName) {
        // Buscar consultas no mesmo horário para o mesmo veterinário que não estejam canceladas
        List<Consultation> existingConsultations = consultationRepository
                .findByConsultationDateAndVeterinarianNameAndStatusNot(
                        consultationDate, veterinarianName, "CANCELLED");

        if (!existingConsultations.isEmpty()) {
            throw new BusinessException(
                    String.format("Veterinarian %s already has a consultation scheduled at %s", 
                            veterinarianName, consultationDate),
                    String.format("The veterinarian %s already has a consultation scheduled at this date and time. " +
                            "Please choose a different time or veterinarian.", veterinarianName)
            );
        }
    }

    private void validateAnimalAvailability(LocalDateTime consultationDate, Long animalId) {
        // Buscar consultas no mesmo horário para o mesmo animal que não estejam canceladas
        List<Consultation> existingConsultations = consultationRepository
                .findByConsultationDateAndAnimalIdAndStatusNot(
                        consultationDate, animalId, "CANCELLED");

        if (!existingConsultations.isEmpty()) {
            throw new BusinessException(
                    String.format("Animal already has a consultation scheduled at %s", consultationDate),
                    "This animal already has a consultation scheduled at this date and time. " +
                            "Please choose a different time."
            );
        }
    }

    private Consultation convertToEntity(ConsultationRequest request, Animal animal) {
        Consultation consultation = new Consultation();
        consultation.setAnimal(animal);
        consultation.setConsultationDate(request.getConsultationDate());
        consultation.setVeterinarianName(request.getVeterinarianName());

        // Map numeric reason code from request to enum and store its id in database
        ConsultationReasonType reasonType = ConsultationReasonType.fromId(request.getReasonCode());
        consultation.setReasonCode(reasonType.getId());
        consultation.setDescription(request.getDescription());
        consultation.setDiagnosis(request.getDiagnosis());
        consultation.setTreatmentPrescribed(request.getTreatmentPrescribed());
        consultation.setObservations(request.getObservations());
        consultation.setNextAppointmentDate(request.getNextAppointmentDate());
        
        // Se o status não for fornecido, usar SCHEDULED para agendamentos
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            consultation.setStatus(request.getStatus());
        } else {
            consultation.setStatus("SCHEDULED");
        }
        
        return consultation;
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

        ConsultationReasonType reasonType = ConsultationReasonType.fromId(consultation.getReasonCode());

        return new ConsultationResponse(
                consultation.getId(),
                animalBasicInfo,
                consultation.getConsultationDate(),
                consultation.getVeterinarianName(),
                reasonType.getId(),
                reasonType.getDescription(),
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

