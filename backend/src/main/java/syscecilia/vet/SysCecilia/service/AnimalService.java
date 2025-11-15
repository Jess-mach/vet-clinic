package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.AnimalRequest;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.dto.PageResponse;
import syscecilia.vet.SysCecilia.exception.BusinessException;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Transactional(readOnly = true)
    public AnimalResponse findById(Long id) {
        Animal animal = animalRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        return convertToResponse(animal);
    }

    @Transactional
    public AnimalResponse create(AnimalRequest request) {
        validateBusinessRules(request);
        
        Animal animal = convertToEntity(request);
        Animal savedAnimal = animalRepository.save(animal);
        
        return convertToResponse(savedAnimal);
    }

    @Transactional
    public AnimalResponse update(Long id, AnimalRequest request) {
        Animal entity = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        
        validateBusinessRulesForUpdate(entity, request);
        
        updateEntityFromRequest(entity, request);
        Animal updatedAnimal = animalRepository.save(entity);
        
        return convertToResponse(updatedAnimal);
    }

    @Transactional
    public void delete(Long id) {
        Animal animal = animalRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        animal.setIsActive(false);
        animal.setInactivatedAt(LocalDateTime.now());
        animalRepository.save(animal);
    }

    private void validateBusinessRules(AnimalRequest request) {
        if (request.getMicrochipNumber() != null && !request.getMicrochipNumber().isBlank()) {
            Optional<Animal> existingAnimal = animalRepository.findByMicrochipNumberAndIsActiveTrue(request.getMicrochipNumber());
            if (existingAnimal.isPresent()) {
                throw new BusinessException(
                    "Microchip number already exists: " + request.getMicrochipNumber(),
                    "An animal with this microchip number is already registered"
                );
            }
        }
    }

    private void validateBusinessRulesForUpdate(Animal animal, AnimalRequest request) {
        if (request.getMicrochipNumber() != null && !request.getMicrochipNumber().isBlank()) {
            // Only validate if microchip is changing
            if (!request.getMicrochipNumber().equals(animal.getMicrochipNumber())) {
                Optional<Animal> existingAnimal = animalRepository.findByMicrochipNumberAndIsActiveTrue(request.getMicrochipNumber());
                if (existingAnimal.isPresent()) {
                    throw new BusinessException(
                        "Microchip number already exists: " + request.getMicrochipNumber(),
                        "An animal with this microchip number is already registered"
                    );
                }
            }
        }
    }

    private Animal convertToEntity(AnimalRequest request) {
        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setSpecies(request.getSpecies());
        animal.setBreed(request.getBreed());
        animal.setGender(request.getGender());
        animal.setBirthDate(request.getBirthDate());
        animal.setColor(request.getColor());
        animal.setWeight(request.getWeight());
        animal.setMicrochipNumber(request.getMicrochipNumber());
        animal.setOwnerName(request.getOwnerName());
        animal.setOwnerPhone(request.getOwnerPhone());
        animal.setOwnerEmail(request.getOwnerEmail());
        return animal;
    }

    private void updateEntityFromRequest(Animal entity, AnimalRequest request) {
        entity.setName(request.getName());
        entity.setSpecies(request.getSpecies());
        entity.setBreed(request.getBreed());
        entity.setGender(request.getGender());
        entity.setBirthDate(request.getBirthDate());
        entity.setColor(request.getColor());
        entity.setWeight(request.getWeight());
        entity.setMicrochipNumber(request.getMicrochipNumber());
        entity.setOwnerName(request.getOwnerName());
        entity.setOwnerPhone(request.getOwnerPhone());
        entity.setOwnerEmail(request.getOwnerEmail());
    }

    @Transactional(readOnly = true)
    public List<AnimalResponse> search(String name, String species, String ownerName) {
        Stream<Animal> animalStream;

        if (name != null && !name.isBlank() && species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species))
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank() && species != null && !species.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species));
        } else if (name != null && !name.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findBySpeciesAndIsActiveTrue(species).stream()
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream();
        } else if (species != null && !species.isBlank()) {
            animalStream = animalRepository.findBySpeciesAndIsActiveTrue(species).stream();
        } else if (ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByOwnerNameContainingIgnoreCaseAndIsActiveTrue(ownerName).stream();
        } else {
            animalStream = animalRepository.findAllByIsActiveTrueOrderByNameAsc().stream();
        }

        return animalStream
                .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<AnimalResponse> searchPaginated(Integer page, Integer pageSize, String name, String species, String ownerName) {
        // Use default values if not provided
        int pageNumber = page != null ? page : 0;
        int size = pageSize != null ? pageSize : 20;

        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Animal> animalPage;

        if (name != null && !name.isBlank() && species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            // Fetch by name and apply other filters in memory
            animalPage = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(name, pageable)
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species)
                            && animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank() && species != null && !species.isBlank()) {
            // Fetch by name and apply species filter in memory
            animalPage = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(name, pageable)
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species));
        } else if (name != null && !name.isBlank() && ownerName != null && !ownerName.isBlank()) {
            // Fetch by name and apply owner filter in memory
            animalPage = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(name, pageable)
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            // Fetch by species and apply owner filter in memory
            animalPage = animalRepository.findBySpeciesAndIsActiveTrueOrderByNameAsc(species, pageable)
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank()) {
            animalPage = animalRepository.findByNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(name, pageable);
        } else if (species != null && !species.isBlank()) {
            animalPage = animalRepository.findBySpeciesAndIsActiveTrueOrderByNameAsc(species, pageable);
        } else if (ownerName != null && !ownerName.isBlank()) {
            animalPage = animalRepository.findByOwnerNameContainingIgnoreCaseAndIsActiveTrueOrderByNameAsc(ownerName, pageable);
        } else {
            animalPage = animalRepository.findAllByIsActiveTrueOrderByNameAsc(pageable);
        }

        List<AnimalResponse> content = animalPage.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                animalPage.getNumber(),
                animalPage.getSize(),
                animalPage.getTotalElements(),
                animalPage.getTotalPages(),
                animalPage.isFirst(),
                animalPage.isLast(),
                animalPage.hasNext(),
                animalPage.hasPrevious()
        );
    }

    private AnimalResponse convertToResponse(Animal animal) {
        return new AnimalResponse(
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getGender(),
                animal.getBirthDate(),
                animal.getColor(),
                animal.getWeight(),
                animal.getMicrochipNumber(),
                animal.getOwnerName(),
                animal.getOwnerPhone(),
                animal.getOwnerEmail(),
                animal.getCreatedAt(),
                animal.getUpdatedAt()
        );
    }
}

