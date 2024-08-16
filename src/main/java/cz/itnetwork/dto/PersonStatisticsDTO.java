package cz.itnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing statistics for a person.
 * This class is used to encapsulate statistical data related to a person's financial activities.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonStatisticsDTO {
    /**
     * The unique identifier of the person.
     */
    private Long personId;

    /**
     * The name of the person.
     */
    private String personName;

    /**
     * The total revenue associated with the person.
     */
    private Long revenue;
}