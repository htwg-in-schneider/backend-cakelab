package cakelab.backend.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import cakelab.backend.model.Category;
import cakelab.backend.model.Product;
import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.ProductRepository;
import cakelab.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

/**
 * Integrationstests für den ProductController.
 * Testet sowohl die REST-Endpunkte als auch die Speicherung in der Datenbank.
 */
@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Initialisiert MockMvc und leert die Datenbank vor jedem Test.
     */
    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
          
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
         .apply(springSecurity())
                .build();
                  userRepository.deleteAll();
        User workerUser = new User();
        
        workerUser.setEmail("admin@example.com");
        workerUser.setOauthId("auth0|admin");
        workerUser.setRole(Role.MITARBEITER);
        userRepository.save(workerUser);
        productRepository.deleteAll();
    }

    /**
     * Testet das Abrufen aller Produkte über REST-Endpunkt.
     */
    @Test
    public void testGetProducts() throws Exception {
        // GIVEN: Ein Produkt in der Datenbank
        Product product = new Product();
        product.setName("Erdbeerkuchen");
        product.setBeschreibung("...");
        product.setCategory(Category.FRUCHTIG);
        product.setPreis(39.90);
        product.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        productRepository.save(product);

        // WHEN: Alle Produkte über REST-Endpunkt abgerufen
        mockMvc.perform(get("/api/product"))
                // THEN: Status ist OK und Produktdetails korrekt
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Erdbeerkuchen"))
                .andExpect(jsonPath("$[0].beschreibung").value("..."))
                .andExpect(jsonPath("$[0].category").value("FRUCHTIG"))
                .andExpect(jsonPath("$[0].preis").value(39.90))
                .andExpect(jsonPath("$[0].bildUrl").value("/assets/images/Kuchen_Lotus-caramell.png"));
    }


    @Test
    public void testGetProductsByName() throws Exception {
        // GIVEN: Multiple products in the database
        Product p1 = new Product();
        p1.setName("Schoko Kuchen");
        p1.setBeschreibung("...");
        p1.setCategory(Category.SCHOKOLADIG);
        p1.setPreis(39.90);
        p1.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Schoko Erdbeer Kuchen");
        p2.setBeschreibung("...");
        p2.setCategory(Category.SCHOKOLADIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Beeren Kuchen");
        p3.setBeschreibung("...");
        p3.setCategory(Category.FRUCHTIG);
        p3.setPreis(39.90);
        p3.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        productRepository.save(p3);

        // WHEN: Products are requested with name filter
        mockMvc.perform(get("/api/product")
                .param("name", "Schoko"))
                // THEN: Only the matching product is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Schoko Kuchen"))
                .andExpect(jsonPath("$[1].name").value("Schoko Erdbeer Kuchen"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetProductsByCategory() throws Exception {
        // GIVEN: Multiple products in the database
        Product p1 = new Product();
        p1.setName("Karamell Kuchen 1");
        p1.setBeschreibung("...");
        p1.setCategory(Category.KARAMELL);
        p1.setPreis(39.90);
        p1.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Schoko Kuchen 2");
        p2.setBeschreibung("...");
        p2.setCategory(Category.SCHOKOLADIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        productRepository.save(p2);

        // WHEN: Products are requested with category filter
        mockMvc.perform(get("/api/product")
                .param("category", "KARAMELL"))
                // THEN: Only the matching product is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Karamell Kuchen 1"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetProductsByNameAndCategory() throws Exception {
        // GIVEN: Multiple products in the database
        Product p1 = new Product();
        p1.setName("Ananas Vanille Kuchen");
        p1.setBeschreibung("...");
        p1.setCategory(Category.FRUCHTIG);
        p1.setPreis(1100.0);
        p1.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Ananas Kuchen");
        p2.setBeschreibung("...");
        p2.setCategory(Category.FRUCHTIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Vanille Kuchen");
        p3.setBeschreibung("...");
        p3.setCategory(Category.SONSTIGES);
        p3.setPreis(39.90);
        p3.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        productRepository.save(p3);

        // WHEN: Products are requested with both name and category filter
        mockMvc.perform(get("/api/product")
                .param("name", "Vanille")
                .param("category", "FRUCHTIG"))
                // THEN: Only the matching product is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ananas Vanille Kuchen"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    /**
     * Testet das Abrufen eines Produkts nach ID.
     */
    @Test
    public void testGetProductById() throws Exception {
        // GIVEN: Produkt in der Datenbank
        Product product = new Product();
        product.setName("Himbeerkuchen");
        product.setBeschreibung("...");
        product.setCategory(Category.FRUCHTIG);
        product.setPreis(39.90);
        product.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        product = productRepository.save(product);

        // WHEN: Produkt nach ID abgerufen
        mockMvc.perform(get("/api/product/" + product.getId()))
        
                .andDo(MockMvcResultHandlers.print())
                // THEN: Status OK, Produktdetails korrekt
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Himbeerkuchen"))
                .andExpect(jsonPath("$.beschreibung").value("..."))
                .andExpect(jsonPath("$.category").value("FRUCHTIG"))
                .andExpect(jsonPath("$.preis").value(39.90))
                .andExpect(jsonPath("$.bildUrl").value("/assets/images/Kuchen_Lotus-caramell.png"));
    }

    /**
     * Testet das Erstellen eines neuen Produkts über REST-Endpunkt.
     */
    @Test
    public void testCreateProduct() throws Exception {
        // GIVEN: Keine Produkte in der Datenbank (sicher durch @BeforeEach)

        // WHEN: Neues Produkt erstellt
        String productPayload = "{\"name\":\"Blaubeerkuchen\",\"beschreibung\":\"...\","
                + "\"category\":\"FRUCHTIG\",\"preis\":39.90,\"bildUrl\":\"/assets/images/Kuchen_Lotus-caramell.png\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/product")
        .with(jwt().jwt(jwt -> jwt.claim("sub", "auth0|admin"))) // simulate admin user
                .contentType(MediaType.APPLICATION_JSON)
                .content(productPayload))
                // THEN: Status OK, Produktdetails korrekt
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Blaubeerkuchen"))
                .andExpect(jsonPath("$.beschreibung").value("..."))
                .andExpect(jsonPath("$.category").value("FRUCHTIG"))
                .andExpect(jsonPath("$.preis").value(39.90))
                .andExpect(jsonPath("$.bildUrl").value("/assets/images/Kuchen_Lotus-caramell.png"))
                .andReturn();

        // THEN: Produkt hat eine ID
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode json = new ObjectMapper().readTree(responseContent);
        Long id = json.has("id") && !json.get("id").isNull() ? json.get("id").asLong() : null;
        assertNotNull(id, "Das erstellte Produkt sollte eine ID haben");

        // THEN: Produkt wurde in der Datenbank gespeichert
        Product saved = productRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Produkt nicht gefunden, ID: " + id));
        assertEquals("Blaubeerkuchen", saved.getName());
        assertEquals(Category.FRUCHTIG, saved.getCategory());
        assertEquals("...", saved.getBeschreibung());
        assertEquals(39.90, saved.getPreis());
        assertEquals("/assets/images/Kuchen_Lotus-caramell.png", saved.getBildUrl());
    }

    /**
     * Testet das Aktualisieren eines bestehenden Produkts.
     */
    @Test
    public void testUpdateProduct() throws Exception {
        // GIVEN: Bestehendes Produkt
        Product existingProduct = new Product();
        existingProduct.setName("Lotus-Karamell");
        existingProduct.setBeschreibung("...");
        existingProduct.setCategory(Category.KARAMELL);
        existingProduct.setPreis(39.90);
        existingProduct.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        Long id = productRepository.save(existingProduct).getId();

        // WHEN: Produkt aktualisiert
        String updatePayload = "{\"name\":\"Lotus-Karamell\",\"beschreibung\":\"...\","
                + "\"category\":\"KARAMELL\",\"preis\":39.90,\"bildUrl\":\"/assets/images/Kuchen_Lotus-caramell.png\"}";
        mockMvc.perform(put("/api/product/" + id)
         .with(jwt().jwt(jwt -> jwt.claim("sub", "auth0|admin"))) // simulate admin user
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
                // THEN: Status OK, Produktdetails korrekt
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lotus-Karamell"))
                .andExpect(jsonPath("$.beschreibung").value("..."))
                .andExpect(jsonPath("$.category").value("KARAMELL"))
                .andExpect(jsonPath("$.preis").value(39.90))
                .andExpect(jsonPath("$.bildUrl").value("/assets/images/Kuchen_Lotus-caramell.png"));

        // THEN: Produkt wurde in der Datenbank aktualisiert
        Product updated = productRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Produkt nicht gefunden, ID: " + id));
        assertEquals("Lotus-Karamell", updated.getName());
        assertEquals("...", updated.getBeschreibung());
        assertEquals(Category.KARAMELL, updated.getCategory());
        assertEquals(39.90, updated.getPreis());
        assertEquals("/assets/images/Kuchen_Lotus-caramell.png", updated.getBildUrl());
    }

    /**
     * Testet das Löschen eines Produkts.
     */
    @Test
    public void testDeleteProduct() throws Exception {
        // GIVEN: Produkt existiert
        Product product = new Product();
        product.setName("To Be Deleted");
        product.setBeschreibung("This product will be deleted.");
        product.setCategory(Category.SCHOKOLADIG);
        product.setPreis(39.90);
        product.setBildUrl("https://example.com/to_be_deleted.jpg");
        product = productRepository.save(product);

        // WHEN: Produkt gelöscht
         mockMvc.perform(delete("/api/product/" + product.getId())
                .with(jwt().jwt(jwt -> jwt.claim("sub", "auth0|admin")))) // simulate admin user
                // THEN: Status No Content
                .andExpect(status().isNoContent());

        // THEN: Produkt existiert nicht mehr in der Datenbank
        Optional<Product> deleted = productRepository.findById(product.getId());
        assertFalse(deleted.isPresent(), "Produkt sollte gelöscht sein");
    }
}
