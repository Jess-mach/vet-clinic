package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.domain.Specification;
import syscecilia.vet.SysCecilia.model.Consultation;

import java.time.LocalDateTime;

public class ConsultationSpecification {

    private ConsultationSpecification() {
        // Prevent instantiation
    }

    /**
     * Filter by animal name (partial match, case-insensitive)
     */
    public static Specification<Consultation> hasAnimalName(String animalName) {
        return (root, query, criteriaBuilder) -> {
            if (animalName == null || animalName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("animal").get("name")),
                    "%" + animalName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by owner name (partial match, case-insensitive)
     */
    public static Specification<Consultation> hasOwnerName(String ownerName) {
        return (root, query, criteriaBuilder) -> {
            if (ownerName == null || ownerName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("animal").get("ownerName")),
                    "%" + ownerName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by veterinarian name (partial match, case-insensitive)
     */
    public static Specification<Consultation> hasVeterinarianName(String veterinarianName) {
        return (root, query, criteriaBuilder) -> {
            if (veterinarianName == null || veterinarianName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("veterinarianName")),
                    "%" + veterinarianName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by status (exact match)
     */
    public static Specification<Consultation> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Filter by reason (partial match, case-insensitive)
     */
    public static Specification<Consultation> hasReason(String reason) {
        return (root, query, criteriaBuilder) -> {
            if (reason == null || reason.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("reason")),
                    "%" + reason.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by description (partial match, case-insensitive)
     */
    public static Specification<Consultation> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by creation date start (greater than or equal)
     */
    public static Specification<Consultation> createdAtStart(LocalDateTime startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
        };
    }

    /**
     * Filter by creation date end (less than or equal)
     */
    public static Specification<Consultation> createdAtEnd(LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }

    /**
     * Combine multiple specifications using AND logic
     */
    public static Specification<Consultation> withFilters(
            String animalName,
            String ownerName,
            String veterinarianName,
            String status,
            String reason,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd) {
        
        return Specification
                .where(hasAnimalName(animalName))
                .and(hasOwnerName(ownerName))
                .and(hasVeterinarianName(veterinarianName))
                .and(hasStatus(status))
                .and(hasReason(reason))
                .and(hasDescription(description))
                .and(createdAtStart(createdAtStart))
                .and(createdAtEnd(createdAtEnd));
    }
}

