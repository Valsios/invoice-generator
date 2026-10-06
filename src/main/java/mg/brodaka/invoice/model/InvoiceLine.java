package mg.brodaka.invoice.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceLine {

    @NotBlank
    private String description;

    @DecimalMin("0")
    private BigDecimal longueur;

    @DecimalMin("0")
    private BigDecimal largeur;

    @NotNull
    @Min(0)
    private Integer nombreDePoints;

    @NotNull
    @Min(1)
    private Integer quantite;

    @NotNull
    @DecimalMin("0")
    private BigDecimal prixConception;

    private BigDecimal prixBroderie = BigDecimal.ZERO;
    private BigDecimal totalLigne = BigDecimal.ZERO;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLongueur() {
        return longueur;
    }

    public void setLongueur(BigDecimal longueur) {
        this.longueur = longueur;
    }

    public BigDecimal getLargeur() {
        return largeur;
    }

    public void setLargeur(BigDecimal largeur) {
        this.largeur = largeur;
    }

    public Integer getNombreDePoints() {
        return nombreDePoints;
    }

    public void setNombreDePoints(Integer nombreDePoints) {
        this.nombreDePoints = nombreDePoints;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixConception() {
        return prixConception;
    }

    public void setPrixConception(BigDecimal prixConception) {
        this.prixConception = prixConception;
    }

    public BigDecimal getPrixBroderie() {
        return prixBroderie;
    }

    public void setPrixBroderie(BigDecimal prixBroderie) {
        this.prixBroderie = prixBroderie;
    }

    public BigDecimal getTotalLigne() {
        return totalLigne;
    }

    public void setTotalLigne(BigDecimal totalLigne) {
        this.totalLigne = totalLigne;
    }
}
