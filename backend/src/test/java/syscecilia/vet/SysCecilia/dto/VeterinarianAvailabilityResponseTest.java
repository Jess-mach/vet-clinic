package syscecilia.vet.SysCecilia.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VeterinarianAvailabilityResponse DTO Tests")
class VeterinarianAvailabilityResponseTest {

    private VeterinarianAvailabilityResponse availabilityResponse;

    @BeforeEach
    void setUp() {
        availabilityResponse = new VeterinarianAvailabilityResponse();
    }

    @Test
    @DisplayName("Should create VeterinarianAvailabilityResponse with default constructor")
    void shouldCreateWithDefaultConstructor() {
        assertNotNull(availabilityResponse);
        assertNull(availabilityResponse.getDate());
        assertNull(availabilityResponse.getStartTime());
        assertNull(availabilityResponse.getEndTime());
        assertNull(availabilityResponse.getTimezone());
    }

    @Test
    @DisplayName("Should create VeterinarianAvailabilityResponse with full constructor")
    void shouldCreateWithFullConstructor() {
        LocalDate date = LocalDate.of(2025, 11, 25);
        LocalTime startTime = LocalTime.of(8, 0, 0);
        LocalTime endTime = LocalTime.of(10, 0, 0);
        String timezone = "America/Sao_Paulo";

        VeterinarianAvailabilityResponse response =
                new VeterinarianAvailabilityResponse(date, startTime, endTime, timezone);

        assertEquals(date, response.getDate());
        assertEquals(startTime, response.getStartTime());
        assertEquals(endTime, response.getEndTime());
        assertEquals(timezone, response.getTimezone());
    }

    @Test
    @DisplayName("Should set and get date")
    void shouldSetAndGetDate() {
        LocalDate date = LocalDate.of(2030, 1, 1);
        availabilityResponse.setDate(date);
        assertEquals(date, availabilityResponse.getDate());
    }

    @Test
    @DisplayName("Should set and get start time")
    void shouldSetAndGetStartTime() {
        LocalTime startTime = LocalTime.of(9, 30);
        availabilityResponse.setStartTime(startTime);
        assertEquals(startTime, availabilityResponse.getStartTime());
    }

    @Test
    @DisplayName("Should set and get end time")
    void shouldSetAndGetEndTime() {
        LocalTime endTime = LocalTime.of(11, 45);
        availabilityResponse.setEndTime(endTime);
        assertEquals(endTime, availabilityResponse.getEndTime());
    }

    @Test
    @DisplayName("Should set and get timezone")
    void shouldSetAndGetTimezone() {
        String timezone = "America/Sao_Paulo";
        availabilityResponse.setTimezone(timezone);
        assertEquals(timezone, availabilityResponse.getTimezone());
    }

    @Test
    @DisplayName("Should handle null values for all fields")
    void shouldHandleNullValues() {
        availabilityResponse.setDate(null);
        availabilityResponse.setStartTime(null);
        availabilityResponse.setEndTime(null);
        availabilityResponse.setTimezone(null);

        assertNull(availabilityResponse.getDate());
        assertNull(availabilityResponse.getStartTime());
        assertNull(availabilityResponse.getEndTime());
        assertNull(availabilityResponse.getTimezone());
    }

    @Test
    @DisplayName("Should handle different time intervals correctly")
    void shouldHandleDifferentTimeIntervals() {
        LocalDate date = LocalDate.of(2025, 12, 31);
        LocalTime earlyMorning = LocalTime.of(6, 0);
        LocalTime lateMorning = LocalTime.of(11, 59);

        availabilityResponse.setDate(date);
        availabilityResponse.setStartTime(earlyMorning);
        availabilityResponse.setEndTime(lateMorning);

        assertEquals(date, availabilityResponse.getDate());
        assertEquals(earlyMorning, availabilityResponse.getStartTime());
        assertEquals(lateMorning, availabilityResponse.getEndTime());
    }
}


