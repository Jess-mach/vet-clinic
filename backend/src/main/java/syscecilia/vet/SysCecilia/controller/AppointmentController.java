package syscecilia.vet.SysCecilia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import syscecilia.vet.SysCecilia.dto.AppointmentRequest;
import syscecilia.vet.SysCecilia.dto.AppointmentResponse;
import syscecilia.vet.SysCecilia.service.AppointmentService;

import java.net.URI;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "API for managing appointment scheduling in the veterinary clinic")
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment for an animal. " +
                    "All required fields must be provided. " +
                    "The appointment date must be in the future. " +
                    "The animal must exist and be active."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment successfully created",
                    content = @Content(schema = @Schema(implementation = AppointmentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error - one or more fields are invalid",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Animal not found or inactive",
                    content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody(
                    description = "Appointment data to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AppointmentRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody AppointmentRequest request) {
        AppointmentResponse createdAppointment = appointmentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/api/appointments/" + createdAppointment.getId()))
                .body(createdAppointment);
    }
}

