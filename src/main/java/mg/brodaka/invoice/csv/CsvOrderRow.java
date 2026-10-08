package mg.brodaka.invoice.csv;

import java.math.BigDecimal;

public class CsvOrderRow {

    private Integer id;
    private String customerRaw;
    private String customerName;
    private String type;
    private Integer quantity;
    private BigDecimal conception;
    private BigDecimal prixVente;
    private BigDecimal prixSupport;
    private BigDecimal totalBroderie;
    private BigDecimal totalAvecSupport;
    private BigDecimal totalAvecConception;
    private BigDecimal totalAllNet;
    private BigDecimal hauteur;
    private BigDecimal largeur;
    private Integer points;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerRaw() {
        return customerRaw;
    }

    public void setCustomerRaw(String customerRaw) {
        this.customerRaw = customerRaw;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getConception() {
        return conception;
    }

    public void setConception(BigDecimal conception) {
        this.conception = conception;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getPrixSupport() {
        return prixSupport;
    }

    public void setPrixSupport(BigDecimal prixSupport) {
        this.prixSupport = prixSupport;
    }

    public BigDecimal getTotalBroderie() {
        return totalBroderie;
    }

    public void setTotalBroderie(BigDecimal totalBroderie) {
        this.totalBroderie = totalBroderie;
    }

    public BigDecimal getTotalAvecSupport() {
        return totalAvecSupport;
    }

    public void setTotalAvecSupport(BigDecimal totalAvecSupport) {
        this.totalAvecSupport = totalAvecSupport;
    }

    public BigDecimal getTotalAvecConception() {
        return totalAvecConception;
    }

    public void setTotalAvecConception(BigDecimal totalAvecConception) {
        this.totalAvecConception = totalAvecConception;
    }

    public BigDecimal getTotalAllNet() {
        return totalAllNet;
    }

    public void setTotalAllNet(BigDecimal totalAllNet) {
        this.totalAllNet = totalAllNet;
    }

    public BigDecimal getHauteur() {
        return hauteur;
    }

    public void setHauteur(BigDecimal hauteur) {
        this.hauteur = hauteur;
    }

    public BigDecimal getLargeur() {
        return largeur;
    }

    public void setLargeur(BigDecimal largeur) {
        this.largeur = largeur;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
