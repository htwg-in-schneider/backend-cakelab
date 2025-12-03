package cakelab.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cakelab.backend.model.Category;
import cakelab.backend.model.Product;
import cakelab.backend.model.Review;
import cakelab.backend.repository.ProductRepository;
import cakelab.backend.repository.ReviewRepository;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("!test")
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    public CommandLineRunner loadData(ProductRepository repository, ReviewRepository reviewRepository) {
        return args -> {
            if (repository.count() == 0) { // Check if the repository is empty
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(repository, reviewRepository);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }
        };
    }

    @SuppressWarnings("null")
    private void loadInitialData(ProductRepository repository, ReviewRepository reviewRepository) {
        Product lotusKaramell = new Product();
        lotusKaramell.setName("Lotus-Karamell");
        lotusKaramell.setBeschreibung("Ein zarter Biskuitboden kombiniert mit einer cremigen Lotus-Biscoff-Schicht.");
        lotusKaramell.setCategory(Category.KARAMELL);
        lotusKaramell.setPreis(39.90);
        lotusKaramell.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");

        Product schokoGanache = new Product();
        schokoGanache.setName("Schoko-Ganache");
        schokoGanache.setBeschreibung(
                "Intensiver Schokoladenkuchen mit zarter Zartbitter-Ganache und einem Hauch Espresso.");
        schokoGanache.setCategory(Category.SCHOKOLADIG);
        schokoGanache.setPreis(42.50);
        schokoGanache.setBildUrl("/assets/images/Kuchen_Schokolade.png");

        Product pistazienHimbeer = new Product();
        pistazienHimbeer.setName("Pistazien-Himbeer");
        pistazienHimbeer.setBeschreibung(
                "Saftiger Pistazienboden kombiniert mit frischer Himbeercreme und leichter Mascarpone.");
        pistazienHimbeer.setCategory(Category.FRUCHTIG);
        pistazienHimbeer.setPreis(44.90);
        pistazienHimbeer.setBildUrl("/assets/images/Kuchen_pistazien-Himbeer.png");

        Product beerenSahne = new Product();
        beerenSahne.setName("Beeren-Sahne");
        beerenSahne.setBeschreibung("Locker gebackener Vanilleboden mit einer Mischung aus Waldbeeren und Sahnecreme.");
        beerenSahne.setCategory(Category.FRUCHTIG);
        beerenSahne.setPreis(37.90);
        beerenSahne.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");

        Product karamellCrunch = new Product();
        karamellCrunch.setName("Karamell-Crunch");
        karamellCrunch.setBeschreibung("Saftiger Karamellkuchen mit knusprigem Topping");
        karamellCrunch.setCategory(Category.KARAMELL);
        karamellCrunch.setPreis(45.00);
        karamellCrunch.setBildUrl("/assets/images/Kuchen_karamell-crunch.png");

        Product zitroneMohn = new Product();
        zitroneMohn.setName("Zitrone-Mohn");
        zitroneMohn.setBeschreibung("Frischer Zitronenkuchen mit feinem Mohnaroma");
        zitroneMohn.setCategory(Category.FRUCHTIG);
        zitroneMohn.setPreis(39.90);
        zitroneMohn.setBildUrl("/assets/images/Kuchen_zitrone-mohn.png");

        Product mangoCheesecake = new Product();
        mangoCheesecake.setName("Mango-Cheesecake");
        mangoCheesecake.setBeschreibung("Cremiger Frischkäsekuchen mit frischer Mangosauce");
        mangoCheesecake.setCategory(Category.FRUCHTIG);
        mangoCheesecake.setPreis(34.90);
        mangoCheesecake.setBildUrl("/assets/images/Kuchen_mango-cheesecake.png");

        Product schokoErdnuss = new Product();
        schokoErdnuss.setName("Schoko-Erdnussbutter");
        schokoErdnuss.setBeschreibung("Intensiver Schokokuchen mit salziger Erdnussbuttercreme");
        schokoErdnuss.setCategory(Category.SCHOKOLADIG);
        schokoErdnuss.setPreis(43.90);
        schokoErdnuss.setBildUrl("/assets/images/Kuchen_schoko-erdnussbutter.png");

        Product honigMandel = new Product();
        honigMandel.setName("Honig-Mandel");
        honigMandel.setBeschreibung("Feiner Honigteig mit karamellisierten Mandeln");
        honigMandel.setCategory(Category.KARAMELL);
        honigMandel.setPreis(34.90);
        honigMandel.setBildUrl("/assets/images/Kuchen_honig-mandel.png");

        // alle Produkte speichern
        repository.saveAll(Arrays.asList(
                lotusKaramell,
                schokoGanache,
                pistazienHimbeer,
                beerenSahne,
                karamellCrunch,
                zitroneMohn,
                mangoCheesecake,
                schokoErdnuss,
                honigMandel));

        // Add reviews
        Review r1a = new Review();
        r1a.setStars(5);
        r1a.setText("wunderschöne Torte");
        r1a.setUserName("Anna");
        r1a.setProduct(lotusKaramell);

        Review r1b = new Review();
        r1b.setStars(4);
        r1b.setText("Bin ziemlich zufrieden.");
        r1b.setUserName("Oli");
        r1b.setProduct(lotusKaramell);

        Review r2 = new Review();
        r2.setStars(4);
        r2.setText("schmeckt gut");
        r2.setUserName("Ben");
        r2.setProduct(schokoGanache);

        Review r3 = new Review();
        r3.setStars(3);
        r3.setText("Die Lieferung hat den Kuchen leider etwas rumgeschüttelt");
        r3.setUserName("Chris");
        r3.setProduct(pistazienHimbeer);

        reviewRepository.saveAll(Arrays.asList(r1a, r1b, r2, r3));

        LOGGER.info("Initial data loaded successfully.");
    }
}