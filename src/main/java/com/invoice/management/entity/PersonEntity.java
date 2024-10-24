package com.invoice.management.entity;

import com.invoice.management.constant.Countries;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entity class representing a Person (individual or company) in the database.
 */
@Entity(name = "person")
@Getter
@Setter
public class PersonEntity {

    /**
     * The unique identifier for the person.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the person or company.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The identification number of the person or company.
     */
    @Column(nullable = false)
    private String identificationNumber;

    /**
     * The tax number of the person or company.
     */
    private String taxNumber;

    /**
     * The account number of the person or company.
     */
    @Column(nullable = false)
    private String accountNumber;

    /**
     * The bank code associated with the account.
     */
    @Column(nullable = false)
    private String bankCode;

    /**
     * The International Bank Account Number (IBAN).
     */
    private String iban;

    /**
     * The telephone number of the person or company.
     */
    @Column(nullable = false)
    private String telephone;

    /**
     * The email address of the person or company.
     */
    @Column(nullable = false)
    private String mail;

    /**
     * The street address of the person or company.
     */
    @Column(nullable = false)
    private String street;

    /**
     * The ZIP code of the person or company.
     */
    @Column(nullable = false)
    private String zip;

    /**
     * The city of the person or company.
     */
    @Column(nullable = false)
    private String city;

    /**
     * The country of the person or company.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Countries country;

    /**
     * Additional notes about the person or company.
     */
    private String note;

    /**
     * Indicates whether the person or company is hidden.
     */
    @Column(nullable = false)
    private boolean hidden = false;

    /**
     * List of invoices where this person is the buyer.
     */
    @OneToMany(mappedBy = "buyer")
    private List<InvoiceEntity> purchases;

    /**
     * List of invoices where this person is the seller.
     */
    @OneToMany(mappedBy = "seller")
    private List<InvoiceEntity> sales;
}
