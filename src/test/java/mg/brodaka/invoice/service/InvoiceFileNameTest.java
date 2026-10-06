package mg.brodaka.invoice.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceFileNameTest {

    @Test
    void usesClientAndDate() {
        assertThat(InvoiceFileName.pdf("Maison Soa", LocalDate.of(2026, 10, 6)))
                .isEqualTo("Brodaka-Maison-Soa-2026-10-06.pdf");
    }
}
