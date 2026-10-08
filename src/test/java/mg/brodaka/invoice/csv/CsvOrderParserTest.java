package mg.brodaka.invoice.csv;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvOrderParserTest {

    private final CsvOrderParser parser = new CsvOrderParser();

    @Test
    void parsesNarrowNoBreakSpacesAndCustomerName() throws Exception {
        try (InputStream in = sample()) {
            List<CsvOrderRow> rows = parser.parse(in);
            assertThat(rows).hasSize(3);
            CsvOrderRow first = rows.getFirst();
            assertThat(first.getCustomerName()).isEqualTo("Sandhie Christina");
            assertThat(first.getConception()).isEqualByComparingTo("15000");
            assertThat(first.getPrixVente()).isEqualByComparingTo("1260");
            assertThat(first.getPoints()).isEqualTo(1800);
            assertThat(first.getQuantity()).isEqualTo(1);
        }
    }

    @Test
    void parsesSemicolonExcelExport() throws Exception {
        try (InputStream in = CsvOrderParserTest.class.getResourceAsStream("/commande-semicolon.csv")) {
            List<CsvOrderRow> rows = parser.parse(in);
            assertThat(rows).hasSize(2);
            assertThat(rows.getFirst().getCustomerName()).isEqualTo("Sandhie Christina");
            assertThat(rows.getFirst().getId()).isEqualTo(24);
            assertThat(rows.getFirst().getType()).isEqualTo("Broderie, T-shirt");
            assertThat(rows.get(1).getId()).isEqualTo(25);
        }
    }

    private static InputStream sample() {
        return CsvOrderParserTest.class.getResourceAsStream("/commande-sample.csv");
    }
}
