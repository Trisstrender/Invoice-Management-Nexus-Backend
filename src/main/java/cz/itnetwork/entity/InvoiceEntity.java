package cz.itnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity class representing an Invoice in the database.
 */
@Entity(name = "invoice")
@Getter
@Setter
public class InvoiceEntity {

    /**
     * The unique identifier for the invoice.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The invoice number.
     */
    @Column(nullable = false)
    private Integer invoiceNumber;

    /**
     * The date the invoice was issued.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate issued;

    /**
     * The due date of the invoice.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    /**
     * The product or service description.
     */
    @Column(nullable = false)
    private String product;

    /**
     * The price of the product or service.
     */
    @Column(nullable = false)
    private Long price;

    /**
     * The VAT percentage.
     */
    @Column(nullable = false)
    private Integer vat;

    /**
     * Additional notes for the invoice.
     */
    private String note;

    /**
     * The buyer associated with this invoice.
     */
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private PersonEntity buyer;

    /**
     * The seller associated with this invoice.
     */
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private PersonEntity seller;
}