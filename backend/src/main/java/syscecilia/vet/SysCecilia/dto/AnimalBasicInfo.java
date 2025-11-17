package syscecilia.vet.SysCecilia.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Basic information about an animal")
public class AnimalBasicInfo {

    @Schema(description = "Animal ID")
    private Long id;

    @Schema(description = "Animal name")
    private String name;

    @Schema(description = "Animal species")
    private String species;

    @Schema(description = "Animal breed")
    private String breed;

    @Schema(description = "Owner name")
    private String ownerName;

    public AnimalBasicInfo() {
    }

    public AnimalBasicInfo(Long id, String name, String species, String breed, String ownerName) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.ownerName = ownerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}

