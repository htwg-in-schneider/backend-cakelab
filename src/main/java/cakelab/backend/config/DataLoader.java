package cakelab.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cakelab.backend.model.Category;
import cakelab.backend.model.Cake;
import cakelab.backend.model.Review;
import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.CakeRepository;
import cakelab.backend.repository.ReviewRepository;
import cakelab.backend.repository.UserRepository;
import java.util.Arrays;
import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;

@Configuration
@Profile("!test")
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository,CakeRepository 
        cakeRepository, ReviewRepository reviewRepository) {
        return args -> {
            loadInitialUsers(userRepository);

            // only load Cakes and reviews if none exist
            if (cakeRepository.count() == 0) { // Check if the repository is empty
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(cakeRepository, reviewRepository);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }
        };
    }
 private void loadInitialUsers(UserRepository userRepository) {

        upsertUser(userRepository,  "maxmuster+kunde@gmail.com", "auth0|693b0fd10aca7b7f6a0e9bb3", Role.KUNDE);
        upsertUser(userRepository,"maxmuster+admin@gmail.com", "auth0|693b0fa43ead746953ac297a",Role.ADMIN);
    }


    private void upsertUser(UserRepository userRepository,  String email, String oauthId, Role role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            User e = existing.get();
            
            e.setOauthId(oauthId);
            e.setRole(role);
            userRepository.save(e);
            LOGGER.info("Updated existing {} user with email={}", role, email);
        } else {
            User u = new User();
           
            u.setEmail(email);
            u.setOauthId(oauthId);
            u.setRole(role);
            userRepository.save(u);
            LOGGER.info("Created new {} user with email={}", role, email);
        }
    }

    private void loadInitialData(CakeRepository CakeRepository, ReviewRepository reviewRepository) {
        Cake lotusKaramell = new Cake();
        lotusKaramell.setName("Lotus-Karamell");
        lotusKaramell.setBeschreibung("Ein zarter Biskuitboden kombiniert mit einer cremigen Lotus-Biscoff-Schicht.");
        lotusKaramell.setCategory(Category.KARAMELL);
        lotusKaramell.setPreis(39.90);
        lotusKaramell.setBildUrl("/assets/images/Kuchen_Lotus-caramell.png");

        Cake schokoGanache = new Cake();
        schokoGanache.setName("Schoko-Ganache");
        schokoGanache.setBeschreibung("Intensiver Schokoladenkuchen mit zarter Zartbitter-Ganache und einem Hauch Espresso");
        schokoGanache.setCategory(Category.SCHOKOLADIG);
        schokoGanache.setPreis(39.90);
        schokoGanache.setBildUrl("/assets/images/Kuchen_Schokolade.png");

        Cake pistazienHimbeer = new Cake();
        pistazienHimbeer.setName("Pistazien-Himbeer");
        pistazienHimbeer.setBeschreibung("Saftiger Pistazienboden kombiniert mit frischer Himbeercreme und leichter Mascarpone.");
        pistazienHimbeer.setCategory(Category.FRUCHTIG);
        pistazienHimbeer.setPreis(39.90);
        pistazienHimbeer.setBildUrl("/assets/images/Kuchen_pistazien-Himbeer.png");

        Cake beerenSahne = new Cake();
        beerenSahne.setName("Beeren-Sahne");
        beerenSahne.setBeschreibung("Locker gebackener Vanilleboden mit einer Mischung aus Waldbeeren und Sahnecreme");
        beerenSahne.setCategory(Category.FRUCHTIG);
        beerenSahne.setPreis(39.90);
        beerenSahne.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        Cake karamellCrunch= new Cake(); 
        karamellCrunch.setCategory(Category.KARAMELL);
         
        karamellCrunch.setName("Karamell-Crunch"); 
        karamellCrunch.setBeschreibung( "Saftiger Karamellkuchen mit knusprigem Topping"); 
        karamellCrunch.setPreis( 45.00); 
        karamellCrunch.setCategory(Category.KARAMELL);
        karamellCrunch.setBildUrl("/src/assets/images/Kuchen_karamell-crunch.png"); 
        Cake zitronMohn= new Cake(); 
        zitronMohn.setName("Zitrone-Mohn");
        zitronMohn.setBeschreibung("Frischer Zitronenkuchen mit feinem Mohnaroma"); 
        zitronMohn.setBildUrl("/src/assets/images/Kuchen_zitrone-mohn.png");
        zitronMohn.setCategory(Category.SONSTIGES);
zitronMohn.setPreis(39.90);


Cake mangoCheesecake= new Cake();
        mangoCheesecake.setName("Mango-Cheesecake");
        mangoCheesecake.setBeschreibung("Cremiger Frischkäsekuchen mit frischer Mangosauce"); 
        mangoCheesecake.setPreis(34.90);
        mangoCheesecake.setBildUrl("/src/assets/images/Kuchen_mango-cheesecake.png"); 
   mangoCheesecake.setCategory(Category.FRUCHTIG);
        Cake schokoErdnuss=new Cake();
        schokoErdnuss.setName("Schoko-Erdnussbutter");
        schokoErdnuss.setBeschreibung("Intensiver Schokokuchen mit salziger Erdnussbuttercreme");
        schokoErdnuss.setPreis(43.90);

    schokoErdnuss.setCategory(Category.SCHOKOLADIG);
    schokoErdnuss.setBildUrl("/src/assets/images/Kuchen_schoko-erdnussbutter.png");
    Cake honigMandel= new Cake(); 
    honigMandel.setName("Honig-Mandel");
    honigMandel.setCategory(Category.SONSTIGES);
        honigMandel.setBeschreibung("Feiner Honigteig mit karamellisierten Mandeln");
        honigMandel.setPreis(34.90);
        honigMandel.setBildUrl("/src/assets/images/Kuchen_honig-mandel.png");



        CakeRepository.saveAll(Arrays.asList(lotusKaramell, schokoGanache, pistazienHimbeer, beerenSahne, honigMandel,karamellCrunch, zitronMohn,mangoCheesecake,schokoErdnuss));


        // Add reviews
        Review r1a = new Review();
        r1a.setStars(5);
        r1a.setText("wunderschöne Torte");
        r1a.setUserName("Anna");
        r1a.setCake(lotusKaramell);
        
        Review r1b = new Review();
        r1b.setStars(4);
        r1b.setText("Bin ziemlich zufrieden.");
        r1b.setUserName("Oli");
        r1b.setCake(lotusKaramell);

        Review r2 = new Review();
        r2.setStars(4);
        r2.setText("schmeckt gut");
        r2.setUserName("Ben");
        r2.setCake(schokoGanache);

        Review r3 = new Review();
        r3.setStars(3);
        r3.setText("Die Lieferung hat den Kuchen leider etwas rumgeschüttelt");
        r3.setUserName("Chris");
        r3.setCake(pistazienHimbeer);

        reviewRepository.saveAll(Arrays.asList(r1a, r1b, r2, r3));

        LOGGER.info("Initial data loaded successfully.");
    }
}