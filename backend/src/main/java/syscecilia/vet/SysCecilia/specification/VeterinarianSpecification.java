package syscecilia.vet.SysCecilia.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import syscecilia.vet.SysCecilia.model.Veterinarian;

import java.util.ArrayList;
import java.util.List;

public class VeterinarianSpecification {

    public static Specification<Veterinarian> withFilters(String name, Integer specialtyCode) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase().trim() + "%"
                ));
            }

            if (specialtyCode != null) {
                predicates.add(criteriaBuilder.equal(root.get("specialtyCode"), specialtyCode));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

