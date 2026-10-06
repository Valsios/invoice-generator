package mg.brodaka.invoice.service;

import mg.brodaka.invoice.model.InvoiceForm;
import mg.brodaka.invoice.model.InvoiceLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceCalculatorTest {

    private final InvoiceCalculator calculator = new InvoiceCalculator();

    @Test
    void embroideryPriceUses700PointsOver1000() {
        assertThat(calculator.embroideryPrice(1000, 1)).isEqualByComparingTo("700.00");
        assertThat(calculator.embroideryPrice(1000, 3)).isEqualByComparingTo("2100.00");
        assertThat(calculator.embroideryPrice(500, 1)).isEqualByComparingTo("350.00");
        assertThat(calculator.embroideryPrice(null, 2)).isEqualByComparingTo("0.00");
    }

    @Test
    void totalsSumEmbroideryAndDesign() {
        InvoiceForm form = new InvoiceForm();
        form.getLines().clear();
        form.getLines().add(line(1000, 1, "100"));
        form.getLines().add(line(2000, 2, "50.5"));

        calculator.recalculate(form);

        assertThat(form.getLines().get(0).getPrixBroderie()).isEqualByComparingTo("700.00");
        assertThat(form.getLines().get(0).getTotalLigne()).isEqualByComparingTo("800.00");
        assertThat(form.getLines().get(1).getPrixBroderie()).isEqualByComparingTo("2800.00");
        assertThat(form.getTotalBroderie()).isEqualByComparingTo("3500.00");
        assertThat(form.getTotalAvecConception()).isEqualByComparingTo("3650.50");
        assertThat(form.getInvoiceDate()).isNotNull();
    }

    private static InvoiceLine line(int points, int quantite, String conception) {
        InvoiceLine line = new InvoiceLine();
        line.setDescription("Motif");
        line.setLongueur(new BigDecimal("10"));
        line.setLargeur(new BigDecimal("5"));
        line.setNombreDePoints(points);
        line.setQuantite(quantite);
        line.setPrixConception(new BigDecimal(conception));
        return line;
    }
}
