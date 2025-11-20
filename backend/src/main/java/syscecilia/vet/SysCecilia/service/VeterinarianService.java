package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.VeterinarianResponse;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;
import syscecilia.vet.SysCecilia.model.Veterinarian;
import syscecilia.vet.SysCecilia.repository.VeterinarianRepository;
import syscecilia.vet.SysCecilia.specification.VeterinarianSpecification;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;

    @Autowired
    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    @Transactional(readOnly = true)
    public List<VeterinarianResponse> findAll(String name, Integer specialtyCode) {
        List<Veterinarian> veterinarians;

        if ((name == null || name.trim().isEmpty()) && specialtyCode == null) {
            // No filters, return all veterinarians sorted by name
            veterinarians = veterinarianRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } else {
            // Apply filters using specification
            veterinarians = veterinarianRepository.findAll(
                    VeterinarianSpecification.withFilters(name, specialtyCode),
                    Sort.by(Sort.Direction.ASC, "name")
            );
        }

        return veterinarians.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private VeterinarianResponse convertToResponse(Veterinarian veterinarian) {
        String specialtyDescription = "";
        try {
            ConsultationReasonType reasonType = ConsultationReasonType.fromId(veterinarian.getSpecialtyCode());
            specialtyDescription = reasonType.getDescription();
        } catch (IllegalArgumentException e) {
            specialtyDescription = "Unknown specialty";
        }

        return new VeterinarianResponse(
                veterinarian.getId(),
                veterinarian.getName(),
                veterinarian.getSpecialtyCode(),
                specialtyDescription,
                veterinarian.getCreatedAt(),
                veterinarian.getUpdatedAt()
        );
    }
}

