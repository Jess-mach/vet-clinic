package syscecilia.vet.SysCecilia.repository;

import org.springframework.data.jpa.domain.Specification;
import syscecilia.vet.SysCecilia.model.Consultation;
import syscecilia.vet.SysCecilia.model.ConsultationReasonType;

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
     * Filter by animal ID (exact match)
     */
    public static Specification<Consultation> hasAnimalId(Long animalId) {
        return (root, query, criteriaBuilder) -> {
            if (animalId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("animal").get("id"), animalId);
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
                    criteriaBuilder.lower(root.join("veterinarian").get("name")),
                    "%" + veterinarianName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filter by veterinarian ID (exact match)
     */
    public static Specification<Consultation> hasVeterinarianId(Long veterinarianId) {
        return (root, query, criteriaBuilder) -> {
            if (veterinarianId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("veterinarian").get("id"), veterinarianId);
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
     * Filter by reason using enum code derived from description.
     * Expects the full description used by ConsultationReasonType.
     */
    public static Specification<Consultation> hasReason(String reason) {
        return (root, query, criteriaBuilder) -> {
            if (reason == null || reason.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            try {
                ConsultationReasonType reasonType = ConsultationReasonType.fromDescription(reason);
                return criteriaBuilder.equal(root.get("reasonCode"), reasonType.getId());
            } catch (IllegalArgumentException ex) {
                // If the description does not match any enum, return no results
                return criteriaBuilder.disjunction();
            }
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
            Long animalId,
            String ownerName,
            String veterinarianName,
            Long veterinarianId,
            String status,
            String reason,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd) {
        
        Specification<Consultation> spec = Specification
                .where(hasAnimalName(animalName))
                .and(hasOwnerName(ownerName))
                .and(hasStatus(status))
                .and(hasReason(reason))
                .and(hasDescription(description))
                .and(createdAtStart(createdAtStart))
                .and(createdAtEnd(createdAtEnd));

        // Apply animal ID filter
        if (animalId != null) {
            spec = spec.and(hasAnimalId(animalId));
        }

        // Apply veterinarian filters (name OR id)
        if (veterinarianName != null && !veterinarianName.trim().isEmpty()) {
            spec = spec.and(hasVeterinarianName(veterinarianName));
        }
        if (veterinarianId != null) {
            spec = spec.and(hasVeterinarianId(veterinarianId));
        }

        return spec;
    }
}

