package syscecilia.vet.SysCecilia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import syscecilia.vet.SysCecilia.dto.ConsultationResponse;
import syscecilia.vet.SysCecilia.service.ConsultationService;

import java.util.List;

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

    @GetMapping
    @Operation(
            summary = "List all consultations",
            description = "Retrieves a list of all consultations in the system. " +
                    "Optionally filter by animal ID to get consultations for a specific animal."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of consultations",
                    content = @Content(schema = @Schema(implementation = ConsultationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Animal not found when filtering by animal ID",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<List<ConsultationResponse>> findAll(
            @Parameter(description = "Filter consultations by animal ID (optional)", required = false, example = "1")
            @RequestParam(required = false) Long animalId) {
        
        List<ConsultationResponse> consultations;
        
        if (animalId != null) {
            consultations = consultationService.findAllByAnimalId(animalId);
        } else {
            consultations = consultationService.findAll();
        }
        
        return ResponseEntity.ok(consultations);
    }
}

