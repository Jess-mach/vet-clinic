package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import syscecilia.vet.SysCecilia.dto.ConsultationRequest;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.exception.BusinessException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConsultationService Tests")
public class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private ConsultationService consultationService;

    private Animal testAnimal;
    private Consultation testConsultation;

    @BeforeEach
    public void setUp() {
        testAnimal = new Animal();
        testAnimal.setId(1L);
        testAnimal.setName("Luna");
        testAnimal.setSpecies("Cat");
        testAnimal.setBreed("Siamese");
        testAnimal.setGender("Female");
        testAnimal.setOwnerName("Jane Smith");

        testConsultation = new Consultation();
        testConsultation.setId(1L);
        testConsultation.setAnimal(testAnimal);
        testConsultation.setConsultationDate(LocalDateTime.of(2025, 10, 20, 10, 0));
        testConsultation.setVeterinarianName("Dr. Santos");
        testConsultation.setReason("Vaccination");
        testConsultation.setDescription("Annual vaccination applied");
        testConsultation.setDiagnosis("Vaccinated successfully");
        testConsultation.setStatus("COMPLETED");
    }

    @Test
    @DisplayName("Should find consultation by ID")
    public void testFindById() {
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));

        ConsultationResponse consultation = consultationService.findById(1L);
        
        assertNotNull(consultation);
        assertEquals(1L, consultation.getId());
        assertEquals("Dr. Santos", consultation.getVeterinarianName());
        assertEquals("Luna", consultation.getAnimal().getName());
        
        verify(consultationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when consultation not found by ID")
    public void testFindByIdNotFound() {
        when(consultationRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.findById(9999L);
        });
        
        verify(consultationRepository, times(1)).findById(9999L);
    }

    @Test
    @DisplayName("Should find all consultations with pagination")
    public void testFindAll() {
        Page<Consultation> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "consultationDate")),
                1
        );
        
        when(consultationRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(page);

        Page<ConsultationResponse> consultations = consultationService.findAll(PageRequest.of(0, 10));
        
        assertNotNull(consultations);
        assertEquals(1, consultations.getTotalElements());
        assertEquals("Vaccination", consultations.getContent().get(0).getReason());
        
        verify(consultationRepository, times(1)).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    @DisplayName("Should return empty page when no consultations exist")
    public void testFindAllEmpty() {
        Page<Consultation> emptyPage = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(0, 10),
                0
        );
        
        when(consultationRepository.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(emptyPage);

        Page<ConsultationResponse> consultations = consultationService.findAll(PageRequest.of(0, 10));
        
        assertNotNull(consultations);
        assertEquals(0, consultations.getTotalElements());
        
        verify(consultationRepository, times(1)).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    @DisplayName("Should find consultations by filters")
    public void testFindByFilters() {
        Page<Consultation> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );
        
        when(consultationRepository.findAll(
                any(Specification.class), any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        Page<ConsultationResponse> consultations = consultationService.findByFilters(
                "Luna", null, "Dr. Santos", "COMPLETED", null, null, null, null,
                PageRequest.of(0, 10)
        );
        
        assertNotNull(consultations);
        assertEquals(1, consultations.getTotalElements());
        assertEquals("Luna", consultations.getContent().get(0).getAnimal().getName());
        
        verify(consultationRepository, times(1)).findAll(
                any(Specification.class), any(org.springframework.data.domain.Pageable.class)
        );
    }

    @Test
    @DisplayName("Should find consultations by animal ID with pagination")
    public void testFindAllByAnimalIdWithPagination() {
        when(animalRepository.existsById(1L)).thenReturn(true);
        
        Page<Consultation> page = new PageImpl<>(
                Arrays.asList(testConsultation),
                PageRequest.of(0, 10),
                1
        );
        
        when(consultationRepository.findAll(
                any(Specification.class), any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        Page<ConsultationResponse> consultations = consultationService.findAllByAnimalId(1L, PageRequest.of(0, 10));
        
        assertNotNull(consultations);
        assertEquals(1, consultations.getTotalElements());
        assertEquals("Vaccination", consultations.getContent().get(0).getReason());
        
        verify(animalRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should throw exception when animal not found")
    public void testFindAllByAnimalIdNotFound() {
        when(animalRepository.existsById(9999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.findAllByAnimalId(9999L, PageRequest.of(0, 10));
        });
        
        verify(animalRepository, times(1)).existsById(9999L);
    }

    @Test
    @DisplayName("Should cancel consultation successfully")
    public void testCancelConsultation() {
        Consultation scheduledConsultation = new Consultation();
        scheduledConsultation.setId(2L);
        scheduledConsultation.setAnimal(testAnimal);
        scheduledConsultation.setConsultationDate(LocalDateTime.of(2025, 11, 20, 10, 0));
        scheduledConsultation.setVeterinarianName("Dr. Silva");
        scheduledConsultation.setReason("Checkup");
        scheduledConsultation.setStatus("SCHEDULED");
        scheduledConsultation.setCreatedAt(LocalDateTime.now());
        scheduledConsultation.setUpdatedAt(LocalDateTime.now());

        when(consultationRepository.findById(2L)).thenReturn(Optional.of(scheduledConsultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> {
            Consultation saved = invocation.getArgument(0);
            saved.setStatus("CANCELLED");
            return saved;
        });

        ConsultationResponse result = consultationService.cancelConsultation(2L);

        assertNotNull(result);
        assertEquals("CANCELLED", result.getStatus());
        verify(consultationRepository, times(1)).findById(2L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw exception when consultation not found for cancellation")
    public void testCancelConsultationNotFound() {
        when(consultationRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.cancelConsultation(9999L);
        });

        verify(consultationRepository, times(1)).findById(9999L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to cancel already cancelled consultation")
    public void testCancelConsultationAlreadyCancelled() {
        Consultation cancelledConsultation = new Consultation();
        cancelledConsultation.setId(3L);
        cancelledConsultation.setAnimal(testAnimal);
        cancelledConsultation.setConsultationDate(LocalDateTime.of(2025, 11, 20, 10, 0));
        cancelledConsultation.setVeterinarianName("Dr. Silva");
        cancelledConsultation.setReason("Checkup");
        cancelledConsultation.setStatus("CANCELLED");

        when(consultationRepository.findById(3L)).thenReturn(Optional.of(cancelledConsultation));

        assertThrows(BusinessException.class, () -> {
            consultationService.cancelConsultation(3L);
        });

        verify(consultationRepository, times(1)).findById(3L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to cancel completed consultation")
    public void testCancelConsultationCompleted() {
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));

        assertThrows(BusinessException.class, () -> {
            consultationService.cancelConsultation(1L);
        });

        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should create consultation successfully when all data is valid")
    public void testCreateConsultationSuccessfully() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");
        request.setDescription("General health examination");
        request.setStatus("SCHEDULED");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setVeterinarianName(request.getVeterinarianName());
        savedConsultation.setReason(request.getReason());
        savedConsultation.setDescription(request.getDescription());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dr. Silva", result.getVeterinarianName());
        assertEquals("Routine checkup", result.getReason());
        assertEquals("SCHEDULED", result.getStatus());
        assertEquals("Luna", result.getAnimal().getName());
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should create consultation with default SCHEDULED status when status not provided")
    public void testCreateConsultationWithDefaultStatus() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");
        request.setStatus(null);

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setVeterinarianName(request.getVeterinarianName());
        savedConsultation.setReason(request.getReason());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> {
            Consultation consultation = invocation.getArgument(0);
            consultation.setId(1L);
            consultation.setCreatedAt(LocalDateTime.now());
            consultation.setUpdatedAt(LocalDateTime.now());
            return consultation;
        });

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when animal not found")
    public void testCreateConsultationAnimalNotFound() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(999L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        when(animalRepository.findByIdAndIsActiveTrue(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(999L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should create consultation with all optional fields")
    public void testCreateConsultationWithAllFields() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");
        request.setDescription("General health examination performed");
        request.setDiagnosis("Healthy");
        request.setTreatmentPrescribed("Continue with regular diet");
        request.setObservations("Schedule next checkup in 6 months");
        request.setNextAppointmentDate(LocalDateTime.of(2026, 6, 15, 14, 30));
        request.setStatus("COMPLETED");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setVeterinarianName(request.getVeterinarianName());
        savedConsultation.setReason(request.getReason());
        savedConsultation.setDescription(request.getDescription());
        savedConsultation.setDiagnosis(request.getDiagnosis());
        savedConsultation.setTreatmentPrescribed(request.getTreatmentPrescribed());
        savedConsultation.setObservations(request.getObservations());
        savedConsultation.setNextAppointmentDate(request.getNextAppointmentDate());
        savedConsultation.setStatus("COMPLETED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals("Dr. Silva", result.getVeterinarianName());
        assertEquals("Routine checkup", result.getReason());
        assertEquals("General health examination performed", result.getDescription());
        assertEquals("Healthy", result.getDiagnosis());
        assertEquals("Continue with regular diet", result.getTreatmentPrescribed());
        assertEquals("Schedule next checkup in 6 months", result.getObservations());
        assertEquals(LocalDateTime.of(2026, 6, 15, 14, 30), result.getNextAppointmentDate());
        assertEquals("COMPLETED", result.getStatus());
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when veterinarian already has consultation at same time")
    public void testCreateConsultationVeterinarianConflict() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        Consultation existingConsultation = new Consultation();
        existingConsultation.setId(2L);
        existingConsultation.setVeterinarianName("Dr. Silva");
        existingConsultation.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        existingConsultation.setStatus("SCHEDULED");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                eq(LocalDateTime.of(2025, 12, 15, 14, 30)), eq("Dr. Silva"), eq("CANCELLED")))
                .thenReturn(Arrays.asList(existingConsultation));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when animal already has consultation at same time")
    public void testCreateConsultationAnimalConflict() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        Consultation existingConsultation = new Consultation();
        existingConsultation.setId(2L);
        existingConsultation.setAnimal(testAnimal);
        existingConsultation.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        existingConsultation.setStatus("SCHEDULED");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                eq(LocalDateTime.of(2025, 12, 15, 14, 30)), eq(1L), eq("CANCELLED")))
                .thenReturn(Arrays.asList(existingConsultation));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when consultation time is before clinic opening")
    public void testCreateConsultationBeforeOpeningTime() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 6, 30)); // 6:30 AM
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when consultation time is after last appointment time on weekday")
    public void testCreateConsultationAfterLastAppointmentTimeWeekday() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 18, 30)); // Monday 18:30
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when consultation time is after last appointment time on Saturday")
    public void testCreateConsultationAfterLastAppointmentTimeSaturday() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        // Saturday, December 20, 2025
        request.setConsultationDate(LocalDateTime.of(2025, 12, 20, 13, 30)); // Saturday 13:30
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should allow consultation at last appointment time on weekday")
    public void testCreateConsultationAtLastAppointmentTimeWeekday() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 18, 0)); // Monday 18:00
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setVeterinarianName(request.getVeterinarianName());
        savedConsultation.setReason(request.getReason());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should allow consultation at last appointment time on Saturday")
    public void testCreateConsultationAtLastAppointmentTimeSaturday() {
        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        // Saturday, December 20, 2025
        request.setConsultationDate(LocalDateTime.of(2025, 12, 20, 13, 0)); // Saturday 13:00
        request.setVeterinarianName("Dr. Silva");
        request.setReason("Routine checkup");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setVeterinarianName(request.getVeterinarianName());
        savedConsultation.setReason(request.getReason());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(consultationRepository.findByConsultationDateAndVeterinarianNameAndStatusNot(
                any(LocalDateTime.class), anyString(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }
}

