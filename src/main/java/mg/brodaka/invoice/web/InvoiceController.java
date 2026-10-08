package mg.brodaka.invoice.web;

import jakarta.validation.Valid;
import mg.brodaka.invoice.model.InvoiceForm;
import mg.brodaka.invoice.model.InvoiceLine;
import mg.brodaka.invoice.service.CsvDevisService;
import mg.brodaka.invoice.service.InvoiceCalculator;
import mg.brodaka.invoice.service.InvoiceFileName;
import mg.brodaka.invoice.service.InvoicePdfService;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Controller
public class InvoiceController {

    private final InvoiceCalculator invoiceCalculator;
    private final InvoicePdfService invoicePdfService;
    private final CsvDevisService csvDevisService;

    public InvoiceController(
            InvoiceCalculator invoiceCalculator,
            InvoicePdfService invoicePdfService,
            CsvDevisService csvDevisService) {
        this.invoiceCalculator = invoiceCalculator;
        this.invoicePdfService = invoicePdfService;
        this.csvDevisService = csvDevisService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/")
    public String form(Model model) {
        InvoiceForm form = new InvoiceForm();
        form.getLines().add(new InvoiceLine());
        model.addAttribute("invoiceForm", form);
        return "invoice-form";
    }

    @PostMapping("/facture/pdf")
    public Object generatePdf(
            @Valid @ModelAttribute("invoiceForm") InvoiceForm invoiceForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "invoice-form";
        }
        invoiceCalculator.recalculate(invoiceForm);
        byte[] pdf = invoicePdfService.generate(invoiceForm);
        String filename = InvoiceFileName.pdf(invoiceForm.getClientName(), invoiceForm.getInvoiceDate());
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(pdf);
    }

    @PostMapping("/devis/csv")
    public Object generateFromCsv(@RequestParam("file") MultipartFile file, Model model) {
        if (file == null || file.isEmpty()) {
            return csvError(model, "Choisissez un fichier CSV.");
        }
        try {
            byte[] zip = csvDevisService.generateZip(file.getInputStream());
            String filename = "Brodaka-devis-" + LocalDate.now() + ".zip";
            ContentDisposition disposition = ContentDisposition.attachment()
                    .filename(filename, StandardCharsets.UTF_8)
                    .build();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                    .body(zip);
        } catch (Exception e) {
            return csvError(model, e.getMessage() == null ? "Impossible de lire le CSV." : e.getMessage());
        }
    }

    private String csvError(Model model, String message) {
        InvoiceForm form = new InvoiceForm();
        form.getLines().add(new InvoiceLine());
        model.addAttribute("invoiceForm", form);
        model.addAttribute("csvError", message);
        return "invoice-form";
    }
}
