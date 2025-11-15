package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.ConsultationRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should find all consultations by animal ID")
    public void testFindAllByAnimalId() {
        when(animalRepository.existsById(1L)).thenReturn(true);
        when(consultationRepository.findByAnimalIdOrderByConsultationDateDesc(1L))
                .thenReturn(Arrays.asList(testConsultation));

        List<ConsultationResponse> consultations = consultationService.findAllByAnimalId(1L);
        
        assertNotNull(consultations);
        assertEquals(1, consultations.size());
        assertEquals("Dr. Santos", consultations.get(0).getVeterinarianName());
        assertEquals("Luna", consultations.get(0).getAnimal().getName());
        
        verify(animalRepository, times(1)).existsById(1L);
        verify(consultationRepository, times(1)).findByAnimalIdOrderByConsultationDateDesc(1L);
    }

    @Test
    @DisplayName("Should throw exception when animal not found")
    public void testFindAllByAnimalIdNotFound() {
        when(animalRepository.existsById(9999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.findAllByAnimalId(9999L);
        });
        
        verify(animalRepository, times(1)).existsById(9999L);
    }

    @Test
    @DisplayName("Should find all consultations")
    public void testFindAll() {
        when(consultationRepository.findAll()).thenReturn(Arrays.asList(testConsultation));

        List<ConsultationResponse> consultations = consultationService.findAll();
        
        assertNotNull(consultations);
        assertEquals(1, consultations.size());
        assertEquals("Vaccination", consultations.get(0).getReason());
        
        verify(consultationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no consultations exist")
    public void testFindAllEmpty() {
        when(consultationRepository.findAll()).thenReturn(Collections.emptyList());

        List<ConsultationResponse> consultations = consultationService.findAll();
        
        assertNotNull(consultations);
        assertEquals(0, consultations.size());
        
        verify(consultationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return consultations ordered by date DESC")
    public void testFindAllByAnimalIdOrderByDate() {
        Consultation consultation2 = new Consultation();
        consultation2.setId(2L);
        consultation2.setAnimal(testAnimal);
        consultation2.setConsultationDate(LocalDateTime.of(2025, 11, 15, 14, 30));
        consultation2.setVeterinarianName("Dr. Silva");
        consultation2.setReason("Follow-up");
        consultation2.setStatus("COMPLETED");

        when(animalRepository.existsById(1L)).thenReturn(true);
        when(consultationRepository.findByAnimalIdOrderByConsultationDateDesc(1L))
                .thenReturn(Arrays.asList(consultation2, testConsultation));

        List<ConsultationResponse> consultations = consultationService.findAllByAnimalId(1L);
        
        assertEquals(2, consultations.size());
        // Should be ordered by date DESC (newest first)
        assertEquals("Follow-up", consultations.get(0).getReason());
        assertEquals("Vaccination", consultations.get(1).getReason());
        
        verify(consultationRepository, times(1)).findByAnimalIdOrderByConsultationDateDesc(1L);
    }
}

