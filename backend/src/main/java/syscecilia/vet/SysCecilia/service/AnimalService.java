package syscecilia.vet.SysCecilia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import syscecilia.vet.SysCecilia.dto.AnimalResponse;
import syscecilia.vet.SysCecilia.exception.ResourceNotFoundException;
import syscecilia.vet.SysCecilia.model.Animal;
import syscecilia.vet.SysCecilia.repository.AnimalRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalResponse> search(String name, String species, String ownerName) {
        Stream<Animal> animalStream;

        if (name != null && !name.isBlank() && species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCase(name).stream()
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species))
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank() && species != null && !species.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCase(name).stream()
                    .filter(animal -> animal.getSpecies().equalsIgnoreCase(species));
        } else if (name != null && !name.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCase(name).stream()
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (species != null && !species.isBlank() && ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findBySpecies(species).stream()
                    .filter(animal -> animal.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()));
        } else if (name != null && !name.isBlank()) {
            animalStream = animalRepository.findByNameContainingIgnoreCase(name).stream();
        } else if (species != null && !species.isBlank()) {
            animalStream = animalRepository.findBySpecies(species).stream();
        } else if (ownerName != null && !ownerName.isBlank()) {
            animalStream = animalRepository.findByOwnerNameContainingIgnoreCase(ownerName).stream();
        } else {
            animalStream = animalRepository.findAllByOrderByNameAsc().stream();
        }

        return animalStream
                .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AnimalResponse findById(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
        return convertToResponse(animal);
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

