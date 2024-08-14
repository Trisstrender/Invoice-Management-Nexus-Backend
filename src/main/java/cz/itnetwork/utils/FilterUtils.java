package cz.itnetwork.utils;

import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterUtils {

    public static Specification<PersonEntity> createPersonSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("name")) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + params.get("name").toLowerCase() + "%"));
            }

            if (params.containsKey("identificationNumber")) {
                predicates.add(criteriaBuilder.like(root.get("identificationNumber"),
                        "%" + params.get("identificationNumber") + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("hidden"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<InvoiceEntity> createInvoiceSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("buyerID") && params.get("buyerID") != null && !params.get("buyerID").isEmpty()) {
                try {
                    Long buyerId = Long.parseLong(params.get("buyerID"));
                    predicates.add(criteriaBuilder.equal(root.get("buyer").get("id"), buyerId));
                } catch (NumberFormatException e) {
                    // Log the error and potentially skip adding this predicate
                }
            }

            if (params.containsKey("sellerID") && params.get("sellerID") != null && !params.get("sellerID").isEmpty()) {
                try {
                    Long sellerId = Long.parseLong(params.get("sellerID"));
                    predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), sellerId));
                } catch (NumberFormatException e) {
                    // Log the error and potentially skip adding this predicate
                }
            }

            if (params.containsKey("product") && params.get("product") != null && !params.get("product").isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("product")),
                        "%" + params.get("product").toLowerCase() + "%"));
            }

            if (params.containsKey("minPrice") && params.get("minPrice") != null && !params.get("minPrice").isEmpty()) {
                try {
                    Long minPrice = Long.parseLong(params.get("minPrice"));
                    predicates.add(criteriaBuilder.ge(root.get("price"), minPrice));
                } catch (NumberFormatException e) {
                    // Log the error and potentially skip adding this predicate
                }
            }

            if (params.containsKey("maxPrice") && params.get("maxPrice") != null && !params.get("maxPrice").isEmpty()) {
                try {
                    Long maxPrice = Long.parseLong(params.get("maxPrice"));
                    predicates.add(criteriaBuilder.le(root.get("price"), maxPrice));
                } catch (NumberFormatException e) {
                    // Log the error and potentially skip adding this predicate
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}