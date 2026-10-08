package mg.brodaka.invoice.csv;

import java.math.BigDecimal;

public final class CsvNumbers {

    private CsvNumbers() {
    }

    public static String blankToNull(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static BigDecimal money(String raw) {
        String cleaned = digits(raw);
        if (cleaned == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(cleaned);
    }

    public static Integer integer(String raw) {
        String cleaned = digits(raw);
        if (cleaned == null) {
            return null;
        }
        return new BigDecimal(cleaned).intValue();
    }

    public static String customerDisplayName(String customer) {
        String value = blankToNull(customer);
        if (value == null) {
            return "Client";
        }
        int url = value.indexOf(" (http");
        if (url > 0) {
            return value.substring(0, url).trim();
        }
        int paren = value.indexOf(" (");
        if (paren > 0 && value.contains("notion.com")) {
            return value.substring(0, paren).trim();
        }
        return value;
    }

    public static String supportLabel(String type, BigDecimal prixSupport) {
        if (prixSupport == null || prixSupport.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        if (type == null || type.isBlank()) {
            return "Support";
        }
        StringBuilder label = new StringBuilder();
        for (String part : type.split(",")) {
            String token = part.trim();
            if (token.isEmpty() || token.equalsIgnoreCase("Broderie")) {
                continue;
            }
            if (!label.isEmpty()) {
                label.append(", ");
            }
            label.append(token);
        }
        return label.isEmpty() ? "Support" : label.toString();
    }

    public static String paddedId(Integer id) {
        if (id == null) {
            return "";
        }
        return String.format("%03d", id);
    }

    private static String digits(String raw) {
        String value = blankToNull(raw);
        if (value == null) {
            return null;
        }
        String cleaned = value
                .replace("\u00A0", "")
                .replace("\u202F", "")
                .replace(" ", "")
                .replace("'", "")
                .replace(",", ".");
        if (cleaned.isEmpty()) {
            return null;
        }
        return cleaned;
    }
}
