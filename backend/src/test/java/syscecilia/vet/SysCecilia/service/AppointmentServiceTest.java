package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import syscecilia.vet.SysCecilia.dto.AppointmentRequest;
import syscecilia.vet.SysCecilia.dto.AppointmentResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Appointment;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.AppointmentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentService Unit Tests")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Animal animal;
    private AppointmentRequest appointmentRequest;

    @BeforeEach
    void setUp() {
        animal = new Animal();
        animal.setId(1L);
        animal.setName("Rex");
        animal.setSpecies("Dog");
        animal.setBreed("Golden Retriever");
        animal.setGender("Male");
        animal.setBirthDate(LocalDate.of(2020, 5, 15));
        animal.setColor("Golden");
        animal.setWeight(new BigDecimal("25.5"));
        animal.setMicrochipNumber("CHIP001");
        animal.setOwnerName("John Doe");
        animal.setOwnerPhone("1234567890");
        animal.setOwnerEmail("john@example.com");
        animal.setIsActive(true);
        animal.setCreatedAt(LocalDateTime.now());
        animal.setUpdatedAt(LocalDateTime.now());

        appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAnimalId(1L);
        appointmentRequest.setAppointmentDate(LocalDateTime.now().plusDays(7));
        appointmentRequest.setVeterinarianName("Dr. Silva");
        appointmentRequest.setReason("Routine checkup");
        appointmentRequest.setNotes("First visit for this animal");
    }

    @Test
    @DisplayName("Should create appointment successfully when all data is valid")
    void shouldCreateAppointmentSuccessfullyWhenAllDataIsValid() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setAnimal(animal);
        savedAppointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        savedAppointment.setVeterinarianName(appointmentRequest.getVeterinarianName());
        savedAppointment.setReason(appointmentRequest.getReason());
        savedAppointment.setNotes(appointmentRequest.getNotes());
        savedAppointment.setStatus("SCHEDULED");
        savedAppointment.setCreatedAt(LocalDateTime.now());
        savedAppointment.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(animal));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // When
        AppointmentResponse result = appointmentService.create(appointmentRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getVeterinarianName()).isEqualTo("Dr. Silva");
        assertThat(result.getReason()).isEqualTo("Routine checkup");
        assertThat(result.getNotes()).isEqualTo("First visit for this animal");
        assertThat(result.getStatus()).isEqualTo("SCHEDULED");
        assertThat(result.getAnimal().getName()).isEqualTo("Rex");
        assertThat(result.getAnimal().getSpecies()).isEqualTo("Dog");
        verify(animalRepository, times(2)).findByIdAndIsActiveTrue(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should create appointment successfully without notes")
    void shouldCreateAppointmentSuccessfullyWithoutNotes() {
        // Given
        appointmentRequest.setNotes(null);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setAnimal(animal);
        savedAppointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        savedAppointment.setVeterinarianName(appointmentRequest.getVeterinarianName());
        savedAppointment.setReason(appointmentRequest.getReason());
        savedAppointment.setNotes(null);
        savedAppointment.setStatus("SCHEDULED");
        savedAppointment.setCreatedAt(LocalDateTime.now());
        savedAppointment.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(animal));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // When
        AppointmentResponse result = appointmentService.create(appointmentRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNotes()).isNull();
        verify(animalRepository, times(2)).findByIdAndIsActiveTrue(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when animal not found")
    void shouldThrowResourceNotFoundExceptionWhenAnimalNotFound() {
        // Given
        when(animalRepository.findByIdAndIsActiveTrue(999L)).thenReturn(Optional.empty());

        appointmentRequest.setAnimalId(999L);

        // When/Then
        assertThatThrownBy(() -> appointmentService.create(appointmentRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Animal not found with id: 999");
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(999L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when animal is inactive")
    void shouldThrowResourceNotFoundExceptionWhenAnimalIsInactive() {
        // Given
        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> appointmentService.create(appointmentRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Animal not found with id: 1");
        verify(animalRepository, times(1)).findByIdAndIsActiveTrue(1L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should convert request to entity correctly")
    void shouldConvertRequestToEntityCorrectly() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setAnimal(animal);
        savedAppointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        savedAppointment.setVeterinarianName(appointmentRequest.getVeterinarianName());
        savedAppointment.setReason(appointmentRequest.getReason());
        savedAppointment.setNotes(appointmentRequest.getNotes());
        savedAppointment.setStatus("SCHEDULED");
        savedAppointment.setCreatedAt(LocalDateTime.now());
        savedAppointment.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(animal));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment appointment = invocation.getArgument(0);
            appointment.setId(1L);
            appointment.setCreatedAt(LocalDateTime.now());
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointment;
        });

        // When
        AppointmentResponse result = appointmentService.create(appointmentRequest);

        // Then
        assertThat(result.getAppointmentDate()).isEqualTo(appointmentRequest.getAppointmentDate());
        assertThat(result.getVeterinarianName()).isEqualTo(appointmentRequest.getVeterinarianName());
        assertThat(result.getReason()).isEqualTo(appointmentRequest.getReason());
        assertThat(result.getNotes()).isEqualTo(appointmentRequest.getNotes());
        assertThat(result.getStatus()).isEqualTo("SCHEDULED");
    }

    @Test
    @DisplayName("Should convert entity to response DTO correctly")
    void shouldConvertEntityToResponseDTOCorrectly() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setAnimal(animal);
        savedAppointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        savedAppointment.setVeterinarianName(appointmentRequest.getVeterinarianName());
        savedAppointment.setReason(appointmentRequest.getReason());
        savedAppointment.setNotes(appointmentRequest.getNotes());
        savedAppointment.setStatus("SCHEDULED");
        savedAppointment.setCreatedAt(LocalDateTime.now());
        savedAppointment.setUpdatedAt(LocalDateTime.now());

        when(animalRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(animal));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // When
        AppointmentResponse result = appointmentService.create(appointmentRequest);

        // Then
        assertThat(result.getId()).isEqualTo(savedAppointment.getId());
        assertThat(result.getAppointmentDate()).isEqualTo(savedAppointment.getAppointmentDate());
        assertThat(result.getVeterinarianName()).isEqualTo(savedAppointment.getVeterinarianName());
        assertThat(result.getReason()).isEqualTo(savedAppointment.getReason());
        assertThat(result.getNotes()).isEqualTo(savedAppointment.getNotes());
        assertThat(result.getStatus()).isEqualTo(savedAppointment.getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(savedAppointment.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(savedAppointment.getUpdatedAt());
        assertThat(result.getAnimal().getId()).isEqualTo(animal.getId());
        assertThat(result.getAnimal().getName()).isEqualTo(animal.getName());
        assertThat(result.getAnimal().getSpecies()).isEqualTo(animal.getSpecies());
        assertThat(result.getAnimal().getBreed()).isEqualTo(animal.getBreed());
        assertThat(result.getAnimal().getOwnerName()).isEqualTo(animal.getOwnerName());
    }
}

