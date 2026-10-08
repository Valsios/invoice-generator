package mg.brodaka.invoice.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvOrderParser {

    public List<CsvOrderRow> parse(InputStream inputStream) throws IOException {
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (content.startsWith("\uFEFF")) {
            content = content.substring(1);
        }
        char delimiter = detectDelimiter(content);
        try (CSVParser parser = CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build()
                .parse(new StringReader(content))) {
            List<CsvOrderRow> rows = new ArrayList<>();
            for (CSVRecord record : parser) {
                CsvOrderRow row = map(record);
                if (row.getCustomerName() != null) {
                    rows.add(row);
                }
            }
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("Le CSV ne contient aucune commande.");
            }
            return rows;
        }
    }

    static char detectDelimiter(String content) {
        String header = content.lines().findFirst().orElse("");
        int semicolons = countUnquoted(header, ';');
        int commas = countUnquoted(header, ',');
        return semicolons > commas ? ';' : ',';
    }

    private static int countUnquoted(String line, char delimiter) {
        int count = 0;
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == delimiter && !inQuotes) {
                count++;
            }
        }
        return count;
    }

    private static CsvOrderRow map(CSVRecord record) {
        CsvOrderRow row = new CsvOrderRow();
        row.setId(CsvNumbers.integer(value(record, "ID")));
        String customer = value(record, "customer");
        row.setCustomerRaw(customer);
        row.setCustomerName(CsvNumbers.customerDisplayName(customer));
        row.setType(CsvNumbers.blankToNull(value(record, "Type")));
        Integer quantity = CsvNumbers.integer(value(record, "quantity"));
        row.setQuantity(quantity == null || quantity < 1 ? 1 : quantity);
        row.setConception(CsvNumbers.money(value(record, "Conception Ar")));
        row.setPrixVente(CsvNumbers.money(value(record, "Prix de vente Ar")));
        row.setPrixSupport(CsvNumbers.money(value(record, "Prix support")));
        row.setTotalBroderie(CsvNumbers.money(value(record, "Total broderie")));
        row.setTotalAvecSupport(CsvNumbers.money(value(record, "Total avec support")));
        row.setTotalAvecConception(CsvNumbers.money(value(record, "Total avec conception")));
        row.setTotalAllNet(CsvNumbers.money(value(record, "Total all net")));
        row.setHauteur(CsvNumbers.money(value(record, "Hauteur(Cm)")));
        row.setLargeur(CsvNumbers.money(value(record, "Largeur(Cm)")));
        row.setPoints(CsvNumbers.integer(value(record, "Point")));
        return row;
    }

    private static String value(CSVRecord record, String header) {
        if (!record.isMapped(header)) {
            for (String name : record.getParser().getHeaderNames()) {
                if (name != null && name.replace("\uFEFF", "").equals(header)) {
                    return record.get(name);
                }
            }
            throw new IllegalArgumentException("Colonne CSV manquante : " + header);
        }
        return record.get(header);
    }
}
