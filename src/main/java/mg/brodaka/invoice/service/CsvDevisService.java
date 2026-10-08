package mg.brodaka.invoice.service;

import mg.brodaka.invoice.csv.CsvNumbers;
import mg.brodaka.invoice.csv.CsvOrderParser;
import mg.brodaka.invoice.csv.CsvOrderRow;
import mg.brodaka.invoice.model.InvoiceForm;
import mg.brodaka.invoice.model.InvoiceLine;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CsvDevisService {

    private final CsvOrderParser csvOrderParser;
    private final InvoicePdfService invoicePdfService;

    public CsvDevisService(CsvOrderParser csvOrderParser, InvoicePdfService invoicePdfService) {
        this.csvOrderParser = csvOrderParser;
        this.invoicePdfService = invoicePdfService;
    }

    public List<InvoiceForm> toDevis(InputStream csv) throws IOException {
        List<CsvOrderRow> rows = csvOrderParser.parse(csv);
        Map<String, List<CsvOrderRow>> grouped = new LinkedHashMap<>();
        for (CsvOrderRow row : rows) {
            grouped.computeIfAbsent(row.getCustomerName(), key -> new ArrayList<>()).add(row);
        }
        List<InvoiceForm> devis = new ArrayList<>();
        for (Map.Entry<String, List<CsvOrderRow>> entry : grouped.entrySet()) {
            devis.add(toInvoice(entry.getKey(), entry.getValue()));
        }
        return devis;
    }

    public byte[] generateZip(InputStream csv) throws IOException {
        List<InvoiceForm> devisList = toDevis(csv);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(output)) {
            for (InvoiceForm devis : devisList) {
                String filename = InvoiceFileName.pdf(devis.getClientName(), devis.getInvoiceDate());
                zip.putNextEntry(new ZipEntry(filename));
                zip.write(invoicePdfService.generate(devis));
                zip.closeEntry();
            }
        }
        return output.toByteArray();
    }

    private static InvoiceForm toInvoice(String clientName, List<CsvOrderRow> rows) {
        List<CsvOrderRow> ordered = new ArrayList<>(rows);
        ordered.sort(Comparator.comparing(CsvOrderRow::getId, Comparator.nullsLast(Integer::compareTo)));
        CsvOrderRow first = ordered.getFirst();

        InvoiceForm form = new InvoiceForm();
        form.setClientName(clientName);
        form.setInvoiceDate(LocalDate.now());
        form.setDemandeNumero(CsvNumbers.paddedId(ordered.stream()
                .map(CsvOrderRow::getId)
                .filter(id -> id != null)
                .min(Integer::compareTo)
                .orElse(null)));
        form.setClientRef(first.getId() == null ? "" : String.valueOf(first.getId()));
        form.setHeaderLargeur(zeroToNull(first.getLargeur()));
        form.setHeaderHauteur(zeroToNull(first.getHauteur()));
        form.setSupport(headerSupport(ordered));

        BigDecimal totalBroderie = BigDecimal.ZERO;
        BigDecimal totalConception = BigDecimal.ZERO;
        BigDecimal totalSupport = BigDecimal.ZERO;
        BigDecimal prixTotal = BigDecimal.ZERO;

        for (CsvOrderRow row : ordered) {
            InvoiceLine line = new InvoiceLine();
            line.setDemande(row.getType() == null ? "Broderie" : row.getType());
            line.setDescription(line.getDemande());
            line.setQuantite(row.getQuantity());
            line.setNombreDePoints(row.getPoints() == null ? 0 : row.getPoints());
            line.setPrixConception(row.getConception());
            line.setPrixBroderie(row.getPrixVente());
            line.setTotalLigne(row.getTotalAvecConception());
            line.setLargeur(zeroToNull(row.getLargeur()));
            line.setLongueur(zeroToNull(row.getHauteur()));
            form.getLines().add(line);

            totalBroderie = totalBroderie.add(nullToZero(row.getTotalBroderie()));
            totalConception = totalConception.add(nullToZero(row.getTotalAvecConception()));
            totalSupport = totalSupport.add(nullToZero(row.getTotalAvecSupport()));
            prixTotal = prixTotal.add(nullToZero(row.getTotalAllNet()));
        }

        form.setTotalBroderie(totalBroderie);
        form.setTotalAvecSupport(totalSupport);
        form.setTotalAvecConception(totalConception);
        form.setPrixTotal(prixTotal);
        return form;
    }

    private static String headerSupport(List<CsvOrderRow> rows) {
        boolean anySupport = rows.stream()
                .map(CsvOrderRow::getPrixSupport)
                .anyMatch(value -> value != null && value.compareTo(BigDecimal.ZERO) > 0);
        if (!anySupport) {
            return "Aucun";
        }
        return rows.stream()
                .map(row -> CsvNumbers.supportLabel(row.getType(), row.getPrixSupport()))
                .filter(label -> label != null && !label.isBlank())
                .findFirst()
                .orElse("Support");
    }

    private static BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static BigDecimal zeroToNull(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return value != null && value.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : value;
        }
        return value;
    }
}
