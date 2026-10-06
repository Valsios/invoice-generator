package mg.brodaka.invoice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceForm {

    @NotBlank
    private String clientName;

    @NotEmpty
    @Valid
    private List<InvoiceLine> lines = new ArrayList<>();

    private LocalDate invoiceDate;
    private BigDecimal totalBroderie = BigDecimal.ZERO;
    private BigDecimal totalAvecConception = BigDecimal.ZERO;

    public InvoiceForm() {
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(List<InvoiceLine> lines) {
        this.lines = lines;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalBroderie() {
        return totalBroderie;
    }

    public void setTotalBroderie(BigDecimal totalBroderie) {
        this.totalBroderie = totalBroderie;
    }

    public BigDecimal getTotalAvecConception() {
        return totalAvecConception;
    }

    public void setTotalAvecConception(BigDecimal totalAvecConception) {
        this.totalAvecConception = totalAvecConception;
    }
}
