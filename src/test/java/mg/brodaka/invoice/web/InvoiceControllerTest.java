package mg.brodaka.invoice.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void formIsAvailable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("invoice-form"));
    }

    @Test
    void generatesPdf() throws Exception {
        mockMvc.perform(post("/facture/pdf")
                        .param("clientName", "Maison Soa")
                        .param("lines[0].description", "Monogramme")
                        .param("lines[0].longueur", "12.5")
                        .param("lines[0].largeur", "8")
                        .param("lines[0].nombreDePoints", "1000")
                        .param("lines[0].quantite", "2")
                        .param("lines[0].prixConception", "150"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", containsString("Brodaka-Maison-Soa-")));
    }

    @Test
    void generatesPdfWithEmptyDimensionFields() throws Exception {
        mockMvc.perform(post("/facture/pdf")
                        .param("clientName", "Maison Soa")
                        .param("lines[0].description", "Monogramme")
                        .param("lines[0].longueur", "")
                        .param("lines[0].largeur", "")
                        .param("lines[0].nombreDePoints", "1000")
                        .param("lines[0].quantite", "2")
                        .param("lines[0].prixConception", "150"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void generatesZipFromCsv() throws Exception {
        byte[] csv = getClass().getResourceAsStream("/commande-sample.csv").readAllBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "commande.csv",
                "text/csv",
                csv);
        mockMvc.perform(multipart("/devis/csv").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/zip")))
                .andExpect(header().string("Content-Disposition", containsString("Brodaka-devis-")));
    }
}
