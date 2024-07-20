package cz.itnetwork.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity(name = "invoice")
@Getter
@Setter
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer invoiceNumber;

    @Temporal(TemporalType.DATE)
    private LocalDate issued;

    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    private String product;

    private Long price;

    private Integer vat;

    private String note;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private PersonEntity buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private PersonEntity seller;
}