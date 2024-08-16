package cz.itnetwork.utils;

import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for creating JPA Specifications to filter entities.
 */
public class FilterUtils {

    /**
     * Creates a Specification to filter Person entities based on provided parameters.
     *
     * @param params filter parameters (e.g., "name", "identificationNumber")
     * @return a Specification for filtering Person entities
     */
    public static Specification<PersonEntity> createPersonSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                if ("name".equals(key)) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"));
                } else if ("identificationNumber".equals(key)) {
                    predicates.add(criteriaBuilder.like(root.get("identificationNumber"), "%" + value + "%"));
                }
            });

            predicates.add(criteriaBuilder.equal(root.get("hidden"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Creates a Specification to filter Invoice entities based on provided parameters.
     *
     * @param params filter parameters (e.g., "buyerID", "sellerID", "product", "minPrice", "maxPrice")
     * @return a Specification for filtering Invoice entities
     */
    public static Specification<InvoiceEntity> createInvoiceSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                try {
                    if ("buyerID".equals(key) && !value.isEmpty()) {
                        predicates.add(criteriaBuilder.equal(root.get("buyer").get("id"), Long.parseLong(value)));
                    } else if ("sellerID".equals(key) && !value.isEmpty()) {
                        predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), Long.parseLong(value)));
                    } else if ("product".equals(key) && !value.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("product")), "%" + value.toLowerCase() + "%"));
                    } else if ("minPrice".equals(key) && !value.isEmpty()) {
                        predicates.add(criteriaBuilder.ge(root.get("price"), Long.parseLong(value)));
                    } else if ("maxPrice".equals(key) && !value.isEmpty()) {
                        predicates.add(criteriaBuilder.le(root.get("price"), Long.parseLong(value)));
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore invalid number format exceptions
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}