package mg.brodaka.invoice.service;

import mg.brodaka.invoice.csv.CsvOrderParser;
import mg.brodaka.invoice.model.InvoiceForm;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvDevisServiceTest {

    @Test
    void groupsRowsByCustomerName() throws Exception {
        InvoicePdfService pdfService = mock(InvoicePdfService.class);
        when(pdfService.generate(any())).thenReturn("%PDF".getBytes());
        CsvDevisService service = new CsvDevisService(new CsvOrderParser(), pdfService);

        try (InputStream in = getClass().getResourceAsStream("/commande-sample.csv")) {
            List<InvoiceForm> devis = service.toDevis(in);
            assertThat(devis).hasSize(2);

            InvoiceForm sandhie = devis.getFirst();
            assertThat(sandhie.getClientName()).isEqualTo("Sandhie Christina");
            assertThat(sandhie.getLines()).hasSize(2);
            assertThat(sandhie.getDemandeNumero()).isEqualTo("024");
            assertThat(sandhie.getSupport()).isEqualTo("Aucun");
            assertThat(sandhie.getTotalBroderie()).isEqualByComparingTo("5260");
            assertThat(sandhie.getTotalAvecSupport()).isEqualByComparingTo("5260");
            assertThat(sandhie.getTotalAvecConception()).isEqualByComparingTo("35260");
            assertThat(sandhie.getPrixTotal()).isEqualByComparingTo("35260");
            assertThat(sandhie.getLines().get(0).getPrixBroderie()).isEqualByComparingTo("1260");
            assertThat(sandhie.getLines().get(1).getQuantite()).isEqualTo(2);

            InvoiceForm maison = devis.get(1);
            assertThat(maison.getClientName()).isEqualTo("Maison Soa");
            assertThat(maison.getLines()).hasSize(1);
        }
    }

    @Test
    void zipContainsOnePdfPerCustomer() throws Exception {
        InvoicePdfService pdfService = mock(InvoicePdfService.class);
        when(pdfService.generate(any())).thenReturn("%PDF-fake".getBytes());
        CsvDevisService service = new CsvDevisService(new CsvOrderParser(), pdfService);

        try (InputStream in = getClass().getResourceAsStream("/commande-sample.csv")) {
            byte[] zip = service.generateZip(in);
            assertThat(zip.length).isGreaterThan(100);
            assertThat(new String(zip, 0, 2)).isEqualTo("PK");
        }
    }
}
