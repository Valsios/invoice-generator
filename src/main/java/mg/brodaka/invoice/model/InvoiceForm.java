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
    private String demandeNumero;
    private String clientRef;
    private String adresse;
    private String contact;
    private String support;
    private BigDecimal headerLargeur;
    private BigDecimal headerHauteur;
    private BigDecimal totalBroderie = BigDecimal.ZERO;
    private BigDecimal totalAvecConception = BigDecimal.ZERO;
    private BigDecimal totalAvecSupport = BigDecimal.ZERO;
    private BigDecimal prixTotal = BigDecimal.ZERO;

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

    public String getDemandeNumero() {
        return demandeNumero;
    }

    public void setDemandeNumero(String demandeNumero) {
        this.demandeNumero = demandeNumero;
    }

    public String getClientRef() {
        return clientRef;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public BigDecimal getHeaderLargeur() {
        return headerLargeur;
    }

    public void setHeaderLargeur(BigDecimal headerLargeur) {
        this.headerLargeur = headerLargeur;
    }

    public BigDecimal getHeaderHauteur() {
        return headerHauteur;
    }

    public void setHeaderHauteur(BigDecimal headerHauteur) {
        this.headerHauteur = headerHauteur;
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

    public BigDecimal getTotalAvecSupport() {
        return totalAvecSupport;
    }

    public void setTotalAvecSupport(BigDecimal totalAvecSupport) {
        this.totalAvecSupport = totalAvecSupport;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }
}
