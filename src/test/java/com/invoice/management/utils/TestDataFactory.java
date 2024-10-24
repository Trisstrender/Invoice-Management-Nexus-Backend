package com.invoice.management.utils;

import cz.itnetwork.constant.Countries;
import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;

import java.time.LocalDate;

public class TestDataFactory {

    public static PersonDTO createValidPersonDTO() {
        return new PersonDTO(
                null,
                "Test Person",
                "12345678",
                "CZ12345678",
                "1234567890",
                "0800",
                "CZ6555101000001234567890",
                "+420123456789",
                "test@example.com",
                "Test Street 123",
                "12345",
                "Test City",
                Countries.CZECHIA,
                false,
                "Test note"
        );
    }

    public static InvoiceDTO createValidInvoiceDTO() {
        return new InvoiceDTO(
                null,
                1001,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Test Product",
                1000L,
                21,
                "Test note",
                createValidPersonDTO(),
                createValidPersonDTO()
        );
    }
}
