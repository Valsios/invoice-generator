package mg.brodaka.invoice.service;

import java.time.LocalDate;

public final class InvoiceFileName {

    private InvoiceFileName() {
    }

    public static String pdf(String clientName, LocalDate invoiceDate) {
        String client = sanitize(clientName);
        if (client.isBlank()) {
            client = "client";
        }
        return "Brodaka-" + client + "-" + invoiceDate + ".pdf";
    }

    static String sanitize(String clientName) {
        if (clientName == null) {
            return "";
        }
        String cleaned = clientName.trim()
                .replaceAll("[\\\\/:*?\"<>|]+", "-")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return cleaned.replaceAll("^-+|-+$", "");
    }
}
