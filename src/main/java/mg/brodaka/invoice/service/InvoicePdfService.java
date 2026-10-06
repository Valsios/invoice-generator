package mg.brodaka.invoice.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import mg.brodaka.invoice.model.InvoiceForm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Locale;

@Service
public class InvoicePdfService {

    private final SpringTemplateEngine templateEngine;

    public InvoicePdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generate(InvoiceForm invoice) {
        Context context = new Context(Locale.FRENCH);
        context.setVariable("invoice", invoice);
        context.setVariable("logoDataUri", logoDataUri());
        String html = templateEngine.process("invoice-pdf", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, staticBaseUri());
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de générer le PDF", e);
        }
    }

    private static String staticBaseUri() {
        try {
            return new ClassPathResource("static/").getURL().toExternalForm();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String logoDataUri() {
        try {
            byte[] bytes = new ClassPathResource("static/images/logo-brodaka.jpg").getContentAsByteArray();
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
