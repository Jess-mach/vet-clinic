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
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;

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

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @InjectMocks
    private ConsultationService consultationService;

    private Animal testAnimal;
    private Veterinarian testVeterinarian;
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

        testVeterinarian = new Veterinarian();
        testVeterinarian.setId(1L);
        testVeterinarian.setName("Dr. Santos");
        testVeterinarian.setSpecialtyCode(ConsultationReasonType.VACCINATION.getId());

        testConsultation = new Consultation();
        testConsultation.setId(1L);
        testConsultation.setAnimal(testAnimal);
        testConsultation.setVeterinarian(testVeterinarian);
        testConsultation.setConsultationDate(LocalDateTime.of(2025, 10, 20, 10, 0));
        testConsultation.setReasonCode(ConsultationReasonType.VACCINATION.getId());
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
        assertEquals(1L, consultation.getVeterinarianId());
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
        assertEquals(ConsultationReasonType.VACCINATION.getDescription(), consultations.getContent().get(0).getReason());
        assertEquals(ConsultationReasonType.VACCINATION.getId(), consultations.getContent().get(0).getReasonCode());
        
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
                "Luna", null, "Dr. Santos", null, "COMPLETED", null, null, null, null,
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
        assertEquals(ConsultationReasonType.VACCINATION.getDescription(), consultations.getContent().get(0).getReason());
        assertEquals(ConsultationReasonType.VACCINATION.getId(), consultations.getContent().get(0).getReasonCode());
        
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
        Veterinarian vet2 = new Veterinarian();
        vet2.setId(2L);
        vet2.setName("Dr. Silva");
        vet2.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Consultation scheduledConsultation = new Consultation();
        scheduledConsultation.setId(2L);
        scheduledConsultation.setAnimal(testAnimal);
        scheduledConsultation.setVeterinarian(vet2);
        scheduledConsultation.setConsultationDate(LocalDateTime.of(2025, 11, 20, 10, 0));
        scheduledConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
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
        cancelledConsultation.setVeterinarian(testVeterinarian);
        cancelledConsultation.setConsultationDate(LocalDateTime.of(2025, 11, 20, 10, 0));
        cancelledConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
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
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        request.setDescription("General health examination");
        request.setStatus("SCHEDULED");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setVeterinarian(vet3);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        savedConsultation.setDescription(request.getDescription());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3L, result.getVeterinarianId());
        assertEquals("Dr. Silva", result.getVeterinarianName());
        assertEquals(ConsultationReasonType.GENERAL_CHECKUP.getDescription(), result.getReason());
        assertEquals("SCHEDULED", result.getStatus());
        assertEquals("Luna", result.getAnimal().getName());
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(veterinarianRepository, times(1)).findById(3L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should create consultation with default SCHEDULED status when status not provided")
    public void testCreateConsultationWithDefaultStatus() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        request.setStatus(null);

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setVeterinarian(vet3);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
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
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        when(animalRepository.findByIdAndIsActiveTrue(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(999L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should update consultation clinical information and next appointment when COMPLETED without changing date")
    public void testUpdateCompletedConsultationClinicalInfo() {
        ConsultationRequest request = new ConsultationRequest();
        request.setDescription("Updated description");
        request.setDiagnosis("Updated diagnosis");
        request.setTreatmentPrescribed("Updated treatment");
        request.setObservations("Updated observations");
        request.setNextAppointmentDate(LocalDateTime.of(2026, 1, 10, 10, 0));

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultationResponse result = consultationService.update(1L, request);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());
        assertEquals("Updated diagnosis", result.getDiagnosis());
        assertEquals("Updated treatment", result.getTreatmentPrescribed());
        assertEquals("Updated observations", result.getObservations());
        assertEquals(LocalDateTime.of(2026, 1, 10, 10, 0), result.getNextAppointmentDate());

        // Data da consulta permanece inalterada
        assertEquals(testConsultation.getConsultationDate(), result.getConsultationDate());

        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to change date of COMPLETED consultation")
    public void testUpdateCompletedConsultationChangeDateNotAllowed() {
        ConsultationRequest request = new ConsultationRequest();
        request.setConsultationDate(LocalDateTime.of(2025, 12, 1, 10, 0)); // different date

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));

        assertThrows(BusinessException.class, () -> consultationService.update(1L, request));

        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should update scheduled consultation date and veterinarian when available")
    public void testUpdateScheduledConsultationChangeDateAndVeterinarian() {
        Veterinarian vet4 = new Veterinarian();
        vet4.setId(4L);
        vet4.setName("Dr. Silva");
        vet4.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Veterinarian vet5 = new Veterinarian();
        vet5.setId(5L);
        vet5.setName("Dr. Costa");
        vet5.setSpecialtyCode(ConsultationReasonType.VACCINATION.getId());

        Consultation scheduledConsultation = new Consultation();
        scheduledConsultation.setId(5L);
        scheduledConsultation.setAnimal(testAnimal);
        scheduledConsultation.setVeterinarian(vet4);
        scheduledConsultation.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        scheduledConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        scheduledConsultation.setStatus("SCHEDULED");

        ConsultationRequest request = new ConsultationRequest();
        request.setConsultationDate(LocalDateTime.of(2025, 12, 16, 10, 0));
        request.setVeterinarianId(5L);
        request.setReasonCode(ConsultationReasonType.VACCINATION.getId());

        when(consultationRepository.findById(5L)).thenReturn(Optional.of(scheduledConsultation));
        when(veterinarianRepository.findById(5L)).thenReturn(Optional.of(vet5));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultationResponse result = consultationService.update(5L, request);

        assertNotNull(result);
        assertEquals(LocalDateTime.of(2025, 12, 16, 10, 0), result.getConsultationDate());
        assertEquals(5L, result.getVeterinarianId());
        assertEquals("Dr. Costa", result.getVeterinarianName());
        assertEquals(ConsultationReasonType.VACCINATION.getId(), result.getReasonCode());

        verify(consultationRepository, times(1)).findById(5L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should create consultation with all optional fields")
    public void testCreateConsultationWithAllFields() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        request.setDescription("General health examination performed");
        request.setDiagnosis("Healthy");
        request.setTreatmentPrescribed("Continue with regular diet");
        request.setObservations("Schedule next checkup in 6 months");
        request.setNextAppointmentDate(LocalDateTime.of(2026, 6, 15, 14, 30));
        request.setStatus("COMPLETED");

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setVeterinarian(vet3);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        savedConsultation.setDescription(request.getDescription());
        savedConsultation.setDiagnosis(request.getDiagnosis());
        savedConsultation.setTreatmentPrescribed(request.getTreatmentPrescribed());
        savedConsultation.setObservations(request.getObservations());
        savedConsultation.setNextAppointmentDate(request.getNextAppointmentDate());
        savedConsultation.setStatus("COMPLETED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals(3L, result.getVeterinarianId());
        assertEquals("Dr. Silva", result.getVeterinarianName());
        assertEquals(ConsultationReasonType.GENERAL_CHECKUP.getDescription(), result.getReason());
        assertEquals("General health examination performed", result.getDescription());
        assertEquals("Healthy", result.getDiagnosis());
        assertEquals("Continue with regular diet", result.getTreatmentPrescribed());
        assertEquals("Schedule next checkup in 6 months", result.getObservations());
        assertEquals(LocalDateTime.of(2026, 6, 15, 14, 30), result.getNextAppointmentDate());
        assertEquals("COMPLETED", result.getStatus());
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(veterinarianRepository, times(1)).findById(3L);
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when veterinarian already has consultation at same time")
    public void testCreateConsultationVeterinarianConflict() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Consultation existingConsultation = new Consultation();
        existingConsultation.setId(2L);
        existingConsultation.setVeterinarian(vet3);
        existingConsultation.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        existingConsultation.setStatus("SCHEDULED");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                eq(LocalDateTime.of(2025, 12, 15, 14, 30)), eq(3L), eq("CANCELLED")))
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
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Consultation existingConsultation = new Consultation();
        existingConsultation.setId(2L);
        existingConsultation.setAnimal(testAnimal);
        existingConsultation.setVeterinarian(vet3);
        existingConsultation.setConsultationDate(LocalDateTime.of(2025, 12, 15, 14, 30));
        existingConsultation.setStatus("SCHEDULED");

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
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
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 6, 30)); // 6:30 AM
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when consultation time is after last appointment time on weekday")
    public void testCreateConsultationAfterLastAppointmentTimeWeekday() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 18, 30)); // Monday 18:30
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when consultation time is after last appointment time on Saturday")
    public void testCreateConsultationAfterLastAppointmentTimeSaturday() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        // Saturday, December 20, 2025
        request.setConsultationDate(LocalDateTime.of(2025, 12, 20, 13, 30)); // Saturday 13:30
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));

        assertThrows(BusinessException.class, () -> {
            consultationService.create(request);
        });

        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should allow consultation at last appointment time on weekday")
    public void testCreateConsultationAtLastAppointmentTimeWeekday() {
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        request.setConsultationDate(LocalDateTime.of(2025, 12, 15, 18, 0)); // Monday 18:00
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setVeterinarian(vet3);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
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
        Veterinarian vet3 = new Veterinarian();
        vet3.setId(3L);
        vet3.setName("Dr. Silva");
        vet3.setSpecialtyCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        ConsultationRequest request = new ConsultationRequest();
        request.setAnimalId(1L);
        // Saturday, December 20, 2025
        request.setConsultationDate(LocalDateTime.of(2025, 12, 20, 13, 0)); // Saturday 13:00
        request.setVeterinarianId(3L);
        request.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId(1L);
        savedConsultation.setAnimal(testAnimal);
        savedConsultation.setVeterinarian(vet3);
        savedConsultation.setConsultationDate(request.getConsultationDate());
        savedConsultation.setReasonCode(ConsultationReasonType.GENERAL_CHECKUP.getId());
        savedConsultation.setStatus("SCHEDULED");
        savedConsultation.setCreatedAt(LocalDateTime.now());
        savedConsultation.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(testAnimal));
        when(veterinarianRepository.findById(3L)).thenReturn(Optional.of(vet3));
        when(consultationRepository.findByConsultationDateAndVeterinarianIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.findByConsultationDateAndAnimalIdAndStatusNot(
                any(LocalDateTime.class), anyLong(), eq("CANCELLED"))).thenReturn(Collections.emptyList());
        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationResponse result = consultationService.create(request);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }
}

