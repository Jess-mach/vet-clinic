package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import syscecilia.vet.SysCecilia.model.Veterinarian;

import java.util.List;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long>, JpaSpecificationExecutor<Veterinarian> {
    
    List<Veterinarian> findByNameContainingIgnoreCase(String name);
    
    List<Veterinarian> findBySpecialtyCode(Integer specialtyCode);
    
    List<Veterinarian> findByNameContainingIgnoreCaseAndSpecialtyCode(String name, Integer specialtyCode);
}

