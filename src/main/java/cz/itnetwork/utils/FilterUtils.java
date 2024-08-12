package cz.itnetwork.utils;

import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class FilterUtils {

    public static Specification<PersonEntity> createPersonSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            String name = params.getOrDefault("name", "");
            String identificationNumber = params.getOrDefault("identificationNumber", "");

            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"),
                    criteriaBuilder.like(root.get("identificationNumber"), "%" + identificationNumber + "%"),
                    criteriaBuilder.equal(root.get("hidden"), false)
            );
        };
    }

    public static Specification<InvoiceEntity> createInvoiceSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            String buyerId = params.get("buyerID");
            String sellerId = params.get("sellerID");
            String product = params.get("product");
            String minPrice = params.get("minPrice");
            String maxPrice = params.get("maxPrice");

            var predicate = criteriaBuilder.conjunction();

            if (buyerId != null && !buyerId.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("buyer").get("id"), Long.parseLong(buyerId)));
            }
            if (sellerId != null && !sellerId.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("seller").get("id"), Long.parseLong(sellerId)));
            }
            if (product != null && !product.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("product")), "%" + product.toLowerCase() + "%"));
            }
            if (minPrice != null && !minPrice.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get("price"), Long.parseLong(minPrice)));
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.le(root.get("price"), Long.parseLong(maxPrice)));
            }

            return predicate;
        };
    }
}