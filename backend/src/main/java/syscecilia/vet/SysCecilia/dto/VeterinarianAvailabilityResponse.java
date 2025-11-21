package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Represents an availability interval for a veterinarian")
public class VeterinarianAvailabilityResponse {

    @Schema(description = "Date respecting America/Sao_Paulo timezone", example = "2025-11-25")
    private LocalDate date;

    @Schema(description = "Inclusive start time for the interval", example = "08:00:00")
    private LocalTime startTime;

    @Schema(description = "Exclusive end time for the interval", example = "10:00:00")
    private LocalTime endTime;

    @Schema(description = "IANA timezone identifier", example = "America/Sao_Paulo")
    private String timezone;

    public VeterinarianAvailabilityResponse() {
    }

    public VeterinarianAvailabilityResponse(LocalDate date, LocalTime startTime, LocalTime endTime, String timezone) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timezone = timezone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}

