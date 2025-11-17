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
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.exception.BusinessException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationSpecification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
}

