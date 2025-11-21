package syscecilia.vet.SysCecilia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import syscecilia.vet.SysCecilia.dto.VeterinarianResponse;
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VeterinarianService Unit Tests")
class VeterinarianServiceTest {

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @InjectMocks
    private VeterinarianService veterinarianService;

    private Veterinarian veterinarian1;
    private Veterinarian veterinarian2;
    private Veterinarian veterinarian3;

    @BeforeEach
    void setUp() {
        veterinarian1 = new Veterinarian();
        veterinarian1.setId(1L);
        veterinarian1.setName("Dr. Amelia Rivers");
        veterinarian1.setSpecialtyCode(2); // Ophthalmology
        veterinarian1.setCreatedAt(LocalDateTime.now());
        veterinarian1.setUpdatedAt(LocalDateTime.now());

        veterinarian2 = new Veterinarian();
        veterinarian2.setId(2L);
        veterinarian2.setName("Dr. Noah Bennett");
        veterinarian2.setSpecialtyCode(1); // General Checkup
        veterinarian2.setCreatedAt(LocalDateTime.now());
        veterinarian2.setUpdatedAt(LocalDateTime.now());

        veterinarian3 = new Veterinarian();
        veterinarian3.setId(3L);
        veterinarian3.setName("Dr. Olivia Carter");
        veterinarian3.setSpecialtyCode(3); // Cardiology
        veterinarian3.setCreatedAt(LocalDateTime.now());
        veterinarian3.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return all veterinarians when no filters are provided")
    void testFindAll_NoFilters_ReturnsAllVeterinarians() {
        // Arrange
        List<Veterinarian> veterinarians = Arrays.asList(veterinarian1, veterinarian2, veterinarian3);
        when(veterinarianRepository.findAll(any(Sort.class))).thenReturn(veterinarians);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(veterinarianRepository, times(1)).findAll(any(Sort.class));
        verify(veterinarianRepository, never()).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("Should return filtered veterinarians when name filter is provided")
    void testFindAll_WithNameFilter_ReturnsFilteredVeterinarians() {
        // Arrange
        List<Veterinarian> filteredVets = Collections.singletonList(veterinarian1);
        when(veterinarianRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(filteredVets);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll("Amelia", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Amelia Rivers", result.get(0).getName());
        assertEquals(2, result.get(0).getSpecialtyCode());
        assertEquals("Consulta com oftalmologista", result.get(0).getSpecialty());
        verify(veterinarianRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("Should return filtered veterinarians when specialty filter is provided")
    void testFindAll_WithSpecialtyFilter_ReturnsFilteredVeterinarians() {
        // Arrange
        List<Veterinarian> filteredVets = Collections.singletonList(veterinarian2);
        when(veterinarianRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(filteredVets);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll(null, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Noah Bennett", result.get(0).getName());
        assertEquals(1, result.get(0).getSpecialtyCode());
        assertEquals("Consulta com clinico geral", result.get(0).getSpecialty());
        verify(veterinarianRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("Should return filtered veterinarians when both filters are provided")
    void testFindAll_WithBothFilters_ReturnsFilteredVeterinarians() {
        // Arrange
        List<Veterinarian> filteredVets = Collections.singletonList(veterinarian3);
        when(veterinarianRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(filteredVets);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll("Olivia", 3);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dr. Olivia Carter", result.get(0).getName());
        assertEquals(3, result.get(0).getSpecialtyCode());
        assertEquals("Consulta com cardiologista", result.get(0).getSpecialty());
        verify(veterinarianRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("Should return empty list when no veterinarians match filters")
    void testFindAll_NoMatches_ReturnsEmptyList() {
        // Arrange
        when(veterinarianRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Collections.emptyList());

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll("NonExistent", null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(veterinarianRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    @DisplayName("Should treat empty string name as no filter")
    void testFindAll_EmptyNameString_TreatedAsNoFilter() {
        // Arrange
        List<Veterinarian> veterinarians = Arrays.asList(veterinarian1, veterinarian2, veterinarian3);
        when(veterinarianRepository.findAll(any(Sort.class))).thenReturn(veterinarians);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll("   ", null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(veterinarianRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("Should convert entity to response with proper specialty description")
    void testConvertToResponse_ProperConversion() {
        // Arrange
        List<Veterinarian> veterinarians = Collections.singletonList(veterinarian1);
        when(veterinarianRepository.findAll(any(Sort.class))).thenReturn(veterinarians);

        // Act
        List<VeterinarianResponse> result = veterinarianService.findAll(null, null);

        // Assert
        VeterinarianResponse response = result.get(0);
        assertEquals(veterinarian1.getId(), response.getId());
        assertEquals(veterinarian1.getName(), response.getName());
        assertEquals(veterinarian1.getSpecialtyCode(), response.getSpecialtyCode());
        assertEquals("Consulta com oftalmologista", response.getSpecialty());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }
}

