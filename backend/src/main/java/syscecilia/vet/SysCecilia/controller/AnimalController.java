package syscecilia.vet.SysCecilia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.service.AnimalService;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@Tag(name = "Animals", description = "API for managing animals in the veterinary clinic")
@Validated
public class AnimalController {

    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    @Operation(
            summary = "Search animals",
            description = "Retrieves a list of animals with optional filters. " +
                    "You can filter by name, species, and/or ownerName. " +
                    "All parameters are optional. If no parameters are provided, returns all animals ordered by name."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of animals",
                    content = @Content(schema = @Schema(implementation = AnimalResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<List<AnimalResponse>> searchAnimals(
            @Parameter(description = "Animal name (partial match, case-insensitive)", required = false, example = "Rex")
            @RequestParam(required = false) String name,
            @Parameter(description = "Animal species (e.g., Dog, Cat, Bird)", required = false, example = "Dog")
            @RequestParam(required = false) String species,
            @Parameter(description = "Owner name (partial match, case-insensitive)", required = false, example = "John")
            @RequestParam(required = false) String ownerName) {
        List<AnimalResponse> animals = animalService.search(name, species, ownerName);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get animal by ID",
            description = "Retrieves a specific animal by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved animal",
                    content = @Content(schema = @Schema(implementation = AnimalResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Animal not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID parameter"
            )
    })
    public ResponseEntity<AnimalResponse> getAnimalById(
            @Parameter(description = "Animal ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID must be greater than 0") Long id) {
        AnimalResponse animal = animalService.findById(id);
        return ResponseEntity.ok(animal);
    }
}