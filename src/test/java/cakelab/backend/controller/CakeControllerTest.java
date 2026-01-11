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
import cakelab.backend.model.Cake;
import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.CakeRepository;
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


@SpringBootTest
@ActiveProfiles("test")
public class CakeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CakeRepository cakeRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
          
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
         .apply(springSecurity())
                .build();
                  userRepository.deleteAll();
        User workerUser = new User();
        
        workerUser.setEmail("admin@example.com");
        workerUser.setOauthId("auth0|admin");
        workerUser.setRole(Role.ADMIN);
        workerUser.setName("Anna"); 
        userRepository.save(workerUser);
        cakeRepository.deleteAll();
    }

    @Test
    public void testGetcakes() throws Exception {
        // GIVEN: Ein Produkt in der Datenbank
        Cake cake = new Cake();
        cake.setName("Erdbeerkuchen");
        cake.setBeschreibung("...");
        cake.setCategory(Category.FRUCHTIG);
        cake.setPreis(39.90);
        cake.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        cakeRepository.save(cake);

        // WHEN: Alle Produkte über REST-Endpunkt abgerufen
        mockMvc.perform(get("/api/cake"))
                // THEN: Status ist OK und Produktdetails korrekt
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Erdbeerkuchen"))
                .andExpect(jsonPath("$[0].beschreibung").value("..."))
                .andExpect(jsonPath("$[0].category").value("FRUCHTIG"))
                .andExpect(jsonPath("$[0].preis").value(39.90))
                .andExpect(jsonPath("$[0].bildUrl").value("/assets/images/Kuchen_Lotus-caramell.png"));
    }


    @Test
    public void testGetcakesByName() throws Exception {
        // GIVEN: Multiple cakes in the database
        Cake p1 = new Cake();
        p1.setName("Schoko Kuchen");
        p1.setBeschreibung("...");
        p1.setCategory(Category.SCHOKOLADIG);
        p1.setPreis(39.90);
        p1.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        cakeRepository.save(p1);

        Cake p2 = new Cake();
        p2.setName("Schoko Erdbeer Kuchen");
        p2.setBeschreibung("...");
        p2.setCategory(Category.SCHOKOLADIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        cakeRepository.save(p2);

        Cake p3 = new Cake();
        p3.setName("Beeren Kuchen");
        p3.setBeschreibung("...");
        p3.setCategory(Category.FRUCHTIG);
        p3.setPreis(39.90);
        p3.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        cakeRepository.save(p3);

        // WHEN: cakes are requested with name filter
        mockMvc.perform(get("/api/cake")
                .param("name", "Schoko"))
                // THEN: Only the matching cake is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Schoko Kuchen"))
                .andExpect(jsonPath("$[1].name").value("Schoko Erdbeer Kuchen"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetcakesByCategory() throws Exception {
        // GIVEN: Multiple cakes in the database
        Cake p1 = new Cake();
        p1.setName("Karamell Kuchen 1");
        p1.setBeschreibung("...");
        p1.setCategory(Category.KARAMELL);
        p1.setPreis(39.90);
        p1.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        cakeRepository.save(p1);

        Cake p2 = new Cake();
        p2.setName("Schoko Kuchen 2");
        p2.setBeschreibung("...");
        p2.setCategory(Category.SCHOKOLADIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_Schokolade.png");
        cakeRepository.save(p2);

        // WHEN: cakes are requested with category filter
        mockMvc.perform(get("/api/cake")
                .param("category", "KARAMELL"))
                // THEN: Only the matching cake is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Karamell Kuchen 1"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetcakesByNameAndCategory() throws Exception {
        // GIVEN: Multiple cakes in the database
        Cake p1 = new Cake();
        p1.setName("Ananas Vanille Kuchen");
        p1.setBeschreibung("...");
        p1.setCategory(Category.FRUCHTIG);
        p1.setPreis(1100.0);
        p1.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        cakeRepository.save(p1);

        Cake p2 = new Cake();
        p2.setName("Ananas Kuchen");
        p2.setBeschreibung("...");
        p2.setCategory(Category.FRUCHTIG);
        p2.setPreis(39.90);
        p2.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        cakeRepository.save(p2);

        Cake p3 = new Cake();
        p3.setName("Vanille Kuchen");
        p3.setBeschreibung("...");
        p3.setCategory(Category.SONSTIGES);
        p3.setPreis(39.90);
        p3.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        cakeRepository.save(p3);

        // WHEN: cakes are requested with both name and category filter
        mockMvc.perform(get("/api/cake")
                .param("name", "Vanille")
                .param("category", "FRUCHTIG"))
                // THEN: Only the matching cake is returned
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ananas Vanille Kuchen"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    /**
     * Testet das Abrufen eines Produkts nach ID.
     */
    @Test
    public void testGetcakeById() throws Exception {
        // GIVEN: Produkt in der Datenbank
        Cake cake = new Cake();
        cake.setName("Himbeerkuchen");
        cake.setBeschreibung("...");
        cake.setCategory(Category.FRUCHTIG);
        cake.setPreis(39.90);
        cake.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        cake = cakeRepository.save(cake);

        // WHEN: Produkt nach ID abgerufen
        mockMvc.perform(get("/api/cake/" + cake.getId()))
        
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
    public void testCreatecake() throws Exception {
        // GIVEN: Keine Produkte in der Datenbank (sicher durch @BeforeEach)

        // WHEN: Neues Produkt erstellt
        String cakePayload = "{\"name\":\"Blaubeerkuchen\",\"beschreibung\":\"...\","
                + "\"category\":\"FRUCHTIG\",\"preis\":39.90,\"bildUrl\":\"/assets/images/Kuchen_Lotus-caramell.png\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/cake")
        .with(jwt().jwt(jwt -> jwt.claim("sub", "auth0|admin"))) // simulate admin user
                .contentType(MediaType.APPLICATION_JSON)
                .content(cakePayload))
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
        Cake saved = cakeRepository.findById(id)
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
    public void testUpdatecake() throws Exception {
        // GIVEN: Bestehendes Produkt
        Cake existingcake = new Cake();
        existingcake.setName("Lotus-Karamell");
        existingcake.setBeschreibung("...");
        existingcake.setCategory(Category.KARAMELL);
        existingcake.setPreis(39.90);
        existingcake.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");
        Long id = cakeRepository.save(existingcake).getId();

        // WHEN: Produkt aktualisiert
        String updatePayload = "{\"name\":\"Lotus-Karamell\",\"beschreibung\":\"...\","
                + "\"category\":\"KARAMELL\",\"preis\":39.90,\"bildUrl\":\"/assets/images/Kuchen_Lotus-caramell.png\"}";
        mockMvc.perform(put("/api/cake/" + id)
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
        Cake updated = cakeRepository.findById(id)
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
    public void testDeletecake() throws Exception {
        // GIVEN: Produkt existiert
        Cake cake = new Cake();
        cake.setName("To Be Deleted");
        cake.setBeschreibung("This cake will be deleted.");
        cake.setCategory(Category.SCHOKOLADIG);
        cake.setPreis(39.90);
        cake.setBildUrl("https://example.com/to_be_deleted.jpg");
        cake = cakeRepository.save(cake);

        // WHEN: Produkt gelöscht
         mockMvc.perform(delete("/api/cake/" + cake.getId())
                .with(jwt().jwt(jwt -> jwt.claim("sub", "auth0|admin")))) // simulate admin user
                // THEN: Status No Content
                .andExpect(status().isNoContent());

        // THEN: Produkt existiert nicht mehr in der Datenbank
        Optional<Cake> deleted = cakeRepository.findById(cake.getId());
        assertFalse(deleted.isPresent(), "Produkt sollte gelöscht sein");
    }
}
