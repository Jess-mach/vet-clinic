package syscecilia.vet.SysCecilia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.service.ConsultationService;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/consultations")
@Tag(name = "Consultations", description = "API for managing veterinary consultations")
@Validated
public class ConsultationController {

    private final ConsultationService consultationService;

    @Autowired
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get consultation by ID",
            description = "Retrieves a specific consultation by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved consultation",
                    content = @Content(schema = @Schema(implementation = ConsultationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Consultation not found",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<ConsultationResponse> findById(
            @Parameter(description = "Consultation ID", required = true, example = "1")
            @PathVariable Long id) {
        ConsultationResponse consultation = consultationService.findById(id);
        return ResponseEntity.ok(consultation);
    }

    @GetMapping
    @Operation(
            summary = "List consultations with multiple filters and pagination",
            description = "Retrieves a paginated list of consultations with optional filters. " +
                    "All filters are optional and can be combined. Filters include: animal name, owner name, " +
                    "veterinarian name, status, reason, description, and creation date range."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated list of consultations",
                    content = @Content(schema = @Schema(implementation = ConsultationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination or filter parameters",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<Page<ConsultationResponse>> findAll(
            @Parameter(description = "Filter by animal name (partial match, case-insensitive)", required = false, example = "Rex")
            @RequestParam(required = false) String animalName,
            @Parameter(description = "Filter by owner name (partial match, case-insensitive)", required = false, example = "João")
            @RequestParam(required = false) String ownerName,
            @Parameter(description = "Filter by veterinarian name (partial match, case-insensitive)", required = false, example = "Dr. Silva")
            @RequestParam(required = false) String veterinarianName,
            @Parameter(description = "Filter by consultation status", required = false, example = "COMPLETED")
            @RequestParam(required = false) String status,
            @Parameter(description = "Filter by consultation reason (partial match, case-insensitive)", required = false, example = "Checkup")
            @RequestParam(required = false) String reason,
            @Parameter(description = "Filter by consultation description (partial match, case-insensitive)", required = false, example = "General")
            @RequestParam(required = false) String description,
            @Parameter(description = "Filter by creation date start (format: yyyy-MM-dd'T'HH:mm:ss)", required = false, example = "2025-01-01T00:00:00")
            @RequestParam(required = false) String createdAtStart,
            @Parameter(description = "Filter by creation date end (format: yyyy-MM-dd'T'HH:mm:ss)", required = false, example = "2025-12-31T23:59:59")
            @RequestParam(required = false) String createdAtEnd,
            @Parameter(description = "Page number (0-indexed)", required = false, example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false, example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field and direction (format: field,asc or field,desc)", required = false, example = "consultationDate,desc")
            @RequestParam(defaultValue = "consultationDate,desc") String sort) {
        
        try {
            LocalDateTime createdAtStartDateTime = null;
            LocalDateTime createdAtEndDateTime = null;
            
            if (createdAtStart != null && !createdAtStart.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                createdAtStartDateTime = LocalDateTime.parse(createdAtStart, formatter);
            }
            
            if (createdAtEnd != null && !createdAtEnd.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                createdAtEndDateTime = LocalDateTime.parse(createdAtEnd, formatter);
            }
            
            // Converter strings vazias em null para melhor compatibilidade com query
            animalName = (animalName != null && animalName.trim().isEmpty()) ? null : animalName;
            ownerName = (ownerName != null && ownerName.trim().isEmpty()) ? null : ownerName;
            veterinarianName = (veterinarianName != null && veterinarianName.trim().isEmpty()) ? null : veterinarianName;
            status = (status != null && status.trim().isEmpty()) ? null : status;
            reason = (reason != null && reason.trim().isEmpty()) ? null : reason;
            description = (description != null && description.trim().isEmpty()) ? null : description;
            
            String[] sortParts = sort.split(",");
            String sortField = sortParts[0];
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
            
            Page<ConsultationResponse> consultations = consultationService.findByFilters(
                    animalName,
                    ownerName,
                    veterinarianName,
                    status,
                    reason,
                    description,
                    createdAtStartDateTime,
                    createdAtEndDateTime,
                    pageable
            );
            
            return ResponseEntity.ok(consultations);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid filter or pagination parameters: " + e.getMessage(), e);
        }
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel consultation",
            description = "Cancels a consultation by setting its status to CANCELLED. " +
                    "Cannot cancel consultations that are already cancelled or completed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consultation successfully cancelled",
                    content = @Content(schema = @Schema(implementation = ConsultationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Consultation not found",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Business rule violation - consultation cannot be cancelled",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<ConsultationResponse> cancelConsultation(
            @Parameter(description = "Consultation ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id) {
        ConsultationResponse consultation = consultationService.cancelConsultation(id);
        return ResponseEntity.ok(consultation);
    }
}

