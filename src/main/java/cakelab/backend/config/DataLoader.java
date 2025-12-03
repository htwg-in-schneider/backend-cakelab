package cakelab.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//import cakelab.backend.model.Category;
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

    private void loadInitialData(ProductRepository repository, ReviewRepository reviewRepository) {
        Product lotusKaramell = new Product();
        //lotusKaramell.setId(1);
        lotusKaramell.setName("Lotus-Karamell");
        lotusKaramell.setBeschreibung("...");
        lotusKaramell.setPreis(39.90);
        lotusKaramell.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");

        Product schokoGanache = new Product();
        //schokoGanache.setId(2);
        schokoGanache.setName("Schoko-Ganache");
        schokoGanache.setBeschreibung("...");
        schokoGanache.setPreis(39.90);
        schokoGanache.setBildUrl("/assets/images/Kuchen_Schokolade.png");

        Product pistazienHimbeer = new Product();
        //pistazienHimbeer.setId(3);
        pistazienHimbeer.setName("Pistazien-Himbeer");
        pistazienHimbeer.setBeschreibung("...");
        pistazienHimbeer.setPreis(39.90);
        pistazienHimbeer.setBildUrl("/assets/images/Kuchen_pistazien-Himbeer.png");

        Product beerenSahne = new Product();
        //beerenSahne.setId(4);
        beerenSahne.setName("Beeren-Sahne");
        beerenSahne.setBeschreibung("...");
        beerenSahne.setPreis(39.90);
        beerenSahne.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");

        repository.saveAll(Arrays.asList(lotusKaramell, schokoGanache, pistazienHimbeer, beerenSahne));


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