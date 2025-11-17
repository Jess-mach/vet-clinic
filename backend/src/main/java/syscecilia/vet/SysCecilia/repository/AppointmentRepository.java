package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAnimalIdOrderByAppointmentDateDesc(Long animalId);

    List<Appointment> findByStatusOrderByAppointmentDateAsc(String status);
}

