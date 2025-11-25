package syscecilia.vet.SysCecilia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;
import syscecilia.vet.SysCecilia.dto.VeterinarianAvailabilityResponse;
import syscecilia.vet.SysCecilia.dto.VeterinarianResponse;
import syscecilia.vet.SysCecilia.service.VeterinarianService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/veterinarians")
@Tag(name = "Veterinarians", description = "API for managing veterinarians")
@Validated
public class VeterinarianController {

    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    @Operation(
            summary = "List all veterinarians with optional filters",
            description = "Retrieves a list of veterinarians with optional filters by name and specialty. " +
                    "Results are sorted alphabetically by name. " +
                    "All filter parameters are optional."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of veterinarians",
                    content = @Content(schema = @Schema(implementation = VeterinarianResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<List<VeterinarianResponse>> findAll(
            @Parameter(
                    description = "Filter by veterinarian name (partial match, case-insensitive)",
                    required = false,
                    example = "Amelia"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Filter by specialty code (reference to ConsultationReasonType)",
                    required = false,
                    example = "2"
            )
            @RequestParam(required = false) Integer specialtyCode
    ) {
        List<VeterinarianResponse> veterinarians = veterinarianService.findAll(name, specialtyCode);
        return ResponseEntity.ok(veterinarians);
    }

    @GetMapping("/{veterinarianId}/availability")
    @Operation(
            summary = "List upcoming availability for a veterinarian",
            description = "Returns up to the next 10 availability intervals (hour granularity) "
                    + "in business hours (08:00-12:00 and 13:00-18:00, Monday to Friday) based on Brasília timezone."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved availability",
                    content = @Content(schema = @Schema(implementation = VeterinarianAvailabilityResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters provided",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veterinarian not found",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    public ResponseEntity<List<VeterinarianAvailabilityResponse>> getAvailability(
            @Parameter(description = "Veterinarian identifier", required = true, example = "1")
            @PathVariable @Positive(message = "Veterinarian id must be greater than zero") Long veterinarianId,
            @Parameter(description = "Optional reference date (YYYY-MM-DD)", example = "2025-11-25")
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate
    ) {
        List<VeterinarianAvailabilityResponse> availability =
                veterinarianService.findAvailability(veterinarianId, referenceDate);
        return ResponseEntity.ok(availability);
    }
}

