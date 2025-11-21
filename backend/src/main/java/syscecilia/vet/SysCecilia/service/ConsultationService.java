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
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationSpecification;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public ConsultationService(ConsultationRepository consultationRepository, 
                               AnimalRepository animalRepository,
                               VeterinarianRepository veterinarianRepository) {
        this.consultationRepository = consultationRepository;
        this.animalRepository = animalRepository;
        this.veterinarianRepository = veterinarianRepository;
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
            Long veterinarianId,
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
                        veterinarianId,
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
        return findByFilters(null, null, null, null, null, null, null, null, null, pageable);
    }

    @Transactional
    public ConsultationResponse create(ConsultationRequest request) {
        Animal animal = animalRepository.findByIdAndIsActiveTrue(request.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + request.getAnimalId()));

        Veterinarian veterinarian = veterinarianRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with id: " + request.getVeterinarianId()));

        LocalDateTime consultationDate = request.getConsultationDate();

        // Validar regras de negócio para nova consulta
        validateBusinessRules(consultationDate, veterinarian.getId(), request.getAnimalId(), null);

        Consultation consultation = convertToEntity(request, animal, veterinarian);
        Consultation savedConsultation = consultationRepository.save(consultation);

        return convertToResponse(savedConsultation);
    }

    @Transactional
    public ConsultationResponse update(Long id, ConsultationRequest request) {
        Consultation existingConsultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));

        boolean isCompleted = "COMPLETED".equalsIgnoreCase(existingConsultation.getStatus());

        // Regra: se a consulta estiver COMPLETED, não pode alterar a data da consulta
        if (isCompleted
                && request.getConsultationDate() != null
                && !request.getConsultationDate().isEqual(existingConsultation.getConsultationDate())) {
            throw new BusinessException(
                    "Cannot change consultation date of a COMPLETED consultation",
                    "This consultation is already completed and its date cannot be changed. " +
                            "You can only update clinical information and schedule a next appointment.");
        }

        // Se a consulta não estiver COMPLETED e a data/veterinário forem alterados, validar regras de negócio
        if (!isCompleted) {
            LocalDateTime newDate = request.getConsultationDate() != null
                    ? request.getConsultationDate()
                    : existingConsultation.getConsultationDate();
            
            Long newVeterinarianId = request.getVeterinarianId() != null
                    ? request.getVeterinarianId()
                    : existingConsultation.getVeterinarian().getId();

            if (!newDate.isEqual(existingConsultation.getConsultationDate())
                    || !newVeterinarianId.equals(existingConsultation.getVeterinarian().getId())) {
                Long animalId = existingConsultation.getAnimal() != null
                        ? existingConsultation.getAnimal().getId()
                        : request.getAnimalId();
                validateBusinessRules(newDate, newVeterinarianId, animalId, existingConsultation.getId());
                existingConsultation.setConsultationDate(newDate);
                
                Veterinarian newVeterinarian = veterinarianRepository.findById(newVeterinarianId)
                        .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with id: " + newVeterinarianId));
                existingConsultation.setVeterinarian(newVeterinarian);
            }
        }

        // Atualizar motivo, descrição e campos clínicos
        if (request.getReasonCode() != null) {
            ConsultationReasonType reasonType = ConsultationReasonType.fromId(request.getReasonCode());
            existingConsultation.setReasonCode(reasonType.getId());
        }

        existingConsultation.setDescription(request.getDescription());
        existingConsultation.setDiagnosis(request.getDiagnosis());
        existingConsultation.setTreatmentPrescribed(request.getTreatmentPrescribed());
        existingConsultation.setObservations(request.getObservations());
        existingConsultation.setNextAppointmentDate(request.getNextAppointmentDate());

        // Atualizar status se fornecido
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            existingConsultation.setStatus(request.getStatus());
        }

        Consultation savedConsultation = consultationRepository.save(existingConsultation);
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

    private void validateBusinessRules(LocalDateTime consultationDate,
                                       Long veterinarianId,
                                       Long animalId,
                                       Long currentConsultationId) {
        // Validar horário de funcionamento
        validateClinicHours(consultationDate);

        // Validar se já existe consulta no mesmo horário para o mesmo veterinário
        validateVeterinarianAvailability(consultationDate, veterinarianId, currentConsultationId);

        // Validar se já existe consulta no mesmo horário para o mesmo animal
        validateAnimalAvailability(consultationDate, animalId, currentConsultationId);
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

    private void validateVeterinarianAvailability(LocalDateTime consultationDate,
                                                  Long veterinarianId,
                                                  Long currentConsultationId) {
        // Buscar consultas no mesmo horário para o mesmo veterinário que não estejam canceladas
        List<Consultation> existingConsultations = consultationRepository
                .findByConsultationDateAndVeterinarianIdAndStatusNot(
                        consultationDate, veterinarianId, "CANCELLED");

        if (!existingConsultations.isEmpty()) {
            boolean hasConflict = existingConsultations.stream()
                    .anyMatch(c -> currentConsultationId == null || !c.getId().equals(currentConsultationId));

            if (!hasConflict) {
                return;
            }

            Veterinarian veterinarian = veterinarianRepository.findById(veterinarianId)
                    .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with id: " + veterinarianId));
            
            throw new BusinessException(
                    String.format("Veterinarian %s already has a consultation scheduled at %s", 
                            veterinarian.getName(), consultationDate),
                    String.format("The veterinarian %s already has a consultation scheduled at this date and time. " +
                            "Please choose a different time or veterinarian.", veterinarian.getName())
            );
        }
    }

    private void validateAnimalAvailability(LocalDateTime consultationDate,
                                            Long animalId,
                                            Long currentConsultationId) {
        // Buscar consultas no mesmo horário para o mesmo animal que não estejam canceladas
        List<Consultation> existingConsultations = consultationRepository
                .findByConsultationDateAndAnimalIdAndStatusNot(
                        consultationDate, animalId, "CANCELLED");

        if (!existingConsultations.isEmpty()) {
            boolean hasConflict = existingConsultations.stream()
                    .anyMatch(c -> currentConsultationId == null || !c.getId().equals(currentConsultationId));

            if (!hasConflict) {
                return;
            }

            throw new BusinessException(
                    String.format("Animal already has a consultation scheduled at %s", consultationDate),
                    "This animal already has a consultation scheduled at this date and time. " +
                            "Please choose a different time."
            );
        }
    }

    private Consultation convertToEntity(ConsultationRequest request, Animal animal, Veterinarian veterinarian) {
        Consultation consultation = new Consultation();
        consultation.setAnimal(animal);
        consultation.setVeterinarian(veterinarian);
        consultation.setConsultationDate(request.getConsultationDate());

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

        Veterinarian veterinarian = consultation.getVeterinarian();
        ConsultationReasonType reasonType = ConsultationReasonType.fromId(consultation.getReasonCode());

        return new ConsultationResponse(
                consultation.getId(),
                animalBasicInfo,
                consultation.getConsultationDate(),
                veterinarian.getId(),
                veterinarian.getName(),
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

