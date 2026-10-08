package mg.brodaka.invoice.service;

import mg.brodaka.invoice.model.InvoiceForm;
import mg.brodaka.invoice.model.InvoiceLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceCalculator {

    public static final BigDecimal EMBROIDERY_RATE = new BigDecimal("700");
    public static final BigDecimal POINTS_DIVISOR = new BigDecimal("1000");
    private static final int SCALE = 2;

    public void recalculate(InvoiceForm form) {
        form.setInvoiceDate(LocalDate.now());
        List<InvoiceLine> lines = form.getLines();
        if (lines == null) {
            BigDecimal zero = BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
            form.setTotalBroderie(zero);
            form.setTotalAvecSupport(zero);
            form.setTotalAvecConception(zero);
            form.setPrixTotal(zero);
            return;
        }

        BigDecimal totalBroderie = BigDecimal.ZERO;
        BigDecimal totalConception = BigDecimal.ZERO;

        for (InvoiceLine line : lines) {
            BigDecimal prixBroderie = embroideryPrice(line.getNombreDePoints(), line.getQuantite());
            BigDecimal prixConception = zeroIfNull(line.getPrixConception());
            BigDecimal totalLigne = prixConception.add(prixBroderie);

            line.setPrixBroderie(prixBroderie);
            line.setTotalLigne(totalLigne);

            totalBroderie = totalBroderie.add(prixBroderie);
            totalConception = totalConception.add(prixConception);
        }

        BigDecimal totalAvecConception = totalBroderie.add(totalConception).setScale(SCALE, RoundingMode.HALF_UP);
        form.setTotalBroderie(totalBroderie.setScale(SCALE, RoundingMode.HALF_UP));
        form.setTotalAvecSupport(totalAvecConception);
        form.setTotalAvecConception(totalAvecConception);
        form.setPrixTotal(totalAvecConception);
        if (form.getSupport() == null || form.getSupport().isBlank()) {
            form.setSupport("Aucun");
        }
        InvoiceLine first = lines.isEmpty() ? null : lines.getFirst();
        if (first != null) {
            if (form.getHeaderLargeur() == null) {
                form.setHeaderLargeur(first.getLargeur());
            }
            if (form.getHeaderHauteur() == null) {
                form.setHeaderHauteur(first.getLongueur());
            }
            for (InvoiceLine line : lines) {
                if (line.getDemande() == null || line.getDemande().isBlank()) {
                    line.setDemande(line.getDescription());
                }
            }
        }
    }

    public BigDecimal embroideryPrice(Integer nombreDePoints, Integer quantite) {
        if (nombreDePoints == null) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }
        int qty = quantite == null ? 0 : quantite;
        return EMBROIDERY_RATE
                .multiply(BigDecimal.valueOf(nombreDePoints))
                .divide(POINTS_DIVISOR, SCALE, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(qty))
                .setScale(SCALE, RoundingMode.HALF_UP);
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
