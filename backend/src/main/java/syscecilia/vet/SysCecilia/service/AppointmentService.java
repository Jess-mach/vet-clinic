package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.AppointmentRequest;
import syscecilia.vet.SysCecilia.dto.AppointmentResponse;
import syscecilia.vet.SysCecilia.dto.AnimalBasicInfo;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.model.Appointment;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;
import syscecilia.vet.SysCecilia.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AnimalRepository animalRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, AnimalRepository animalRepository) {
        this.appointmentRepository = appointmentRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        verifyAnimalExists(request.getAnimalId());
        
        Appointment appointment = convertToEntity(request);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        return convertToResponse(savedAppointment);
    }

    private void verifyAnimalExists(Long animalId) {
        animalRepository.findByIdAndIsActiveTrue(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + animalId));
    }

    private Appointment convertToEntity(AppointmentRequest request) {
        Animal animal = animalRepository.findByIdAndIsActiveTrue(request.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + request.getAnimalId()));
        
        Appointment appointment = new Appointment();
        appointment.setAnimal(animal);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setVeterinarianName(request.getVeterinarianName());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setStatus("SCHEDULED");
        return appointment;
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {
        Animal animal = appointment.getAnimal();
        AnimalBasicInfo animalBasicInfo = new AnimalBasicInfo(
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getOwnerName()
        );

        return new AppointmentResponse(
                appointment.getId(),
                animalBasicInfo,
                appointment.getAppointmentDate(),
                appointment.getVeterinarianName(),
                appointment.getReason(),
                appointment.getNotes(),
                appointment.getStatus(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}

