package cakelab.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import cakelab.backend.model.Category;
import cakelab.backend.model.Order;
import cakelab.backend.model.OrderItem;
import cakelab.backend.model.Cake;
import cakelab.backend.model.Review;
import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.CakeRepository;
import cakelab.backend.repository.OrderRepository;
import cakelab.backend.repository.ReviewRepository;
import cakelab.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.*;
@Configuration
@Profile("!test")
@Transactional
public class DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, CakeRepository cakeRepository,
            ReviewRepository reviewRepository, OrderRepository orderRepo) {
        return args -> {
            loadInitialUsers(userRepository);
      
 
            // only load Cakes and reviews if none exist
        if (cakeRepository.count() == 0) { // Check if the repository is empty
                LOGGER.info("Database is empty. Loading initial data...");
                loadInitialData(userRepository, cakeRepository, reviewRepository, orderRepo);
            } else {
                LOGGER.info("Database already contains data. Skipping data loading.");
            }  
        }; 
    }

    private void loadInitialUsers(UserRepository userRepository) {

        upsertUser(userRepository, "Max Mustermann", "maxmuster+kunde@gmail.com", "auth0|693b0fd10aca7b7f6a0e9bb3",
                Role.KUNDE);
        upsertUser(userRepository, "Maximilian Mustermann", "maxmuster+admin@gmail.com",
                "auth0|693b0fa43ead746953ac297a", Role.ADMIN);
        upsertUser(userRepository, "Anna", "anna@example.com", "auth0|someoauthid", Role.KUNDE);
        upsertUser(userRepository, "Oli", "oli.doe@example.com", "auth0|anotheroauthid", Role.KUNDE);
        upsertUser(userRepository, "Chris", "chris@example.com", "auth0|anotheroauthid", Role.KUNDE);
        upsertUser(userRepository, "Ben", "ben@example.com", "auth0|anotheroauthid", Role.KUNDE);
    }

    private void upsertUser(UserRepository userRepository, String Name, String email, String oauthId, Role role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            User e = existing.get();

            e.setOauthId(oauthId);
            e.setRole(role);
            e.setName(Name);
            e.setEmail(email);
            userRepository.save(e);
            LOGGER.info("Updated existing {} user with email={}", role, email);
        } else {
            User u = new User();
            u.setName(Name);
            u.setEmail(email);
            u.setOauthId(oauthId);
            u.setRole(role);
            userRepository.save(u);
            LOGGER.info("Created new {} user with email={}", role, email);
        }
    }
    @Transactional

    private void loadInitialData(UserRepository userRepository, CakeRepository cakeRepository,
            ReviewRepository reviewRepository, OrderRepository orderRepo) {
      
        Cake lotusKaramell = new Cake();
        lotusKaramell.setName("Lotus-Karamell");
        lotusKaramell.setBeschreibung("Ein zarter Biskuitboden kombiniert mit einer cremigen Lotus-Biscoff-Schicht.");
        lotusKaramell.setCategory(Category.KARAMELL);
        lotusKaramell.setPreis(39.90);
        lotusKaramell.setBildUrl("Kuchen_Lotus-caramell.png");
        Cake schokoGanache = new Cake();
        schokoGanache.setName("Schoko-Ganache");
        schokoGanache
                .setBeschreibung("Intensiver Schokoladenkuchen mit zarter Zartbitter-Ganache und einem Hauch Espresso");
        schokoGanache.setCategory(Category.SCHOKOLADIG);
        schokoGanache.setPreis(39.90);
        schokoGanache.setBildUrl("Kuchen_Schokolade.png");

        Cake pistazienHimbeer = new Cake();
        pistazienHimbeer.setName("Pistazien-Himbeer");
        pistazienHimbeer.setBeschreibung(
                "Saftiger Pistazienboden kombiniert mit frischer Himbeercreme und leichter Mascarpone.");
        pistazienHimbeer.setCategory(Category.FRUCHTIG);
        pistazienHimbeer.setPreis(39.90);
        pistazienHimbeer.setBildUrl("Kuchen_pistazien-Himbeer.png");

        Cake beerenSahne = new Cake();
        beerenSahne.setName("Beeren-Sahne");
        beerenSahne.setBeschreibung("Locker gebackener Vanilleboden mit einer Mischung aus Waldbeeren und Sahnecreme");
        beerenSahne.setCategory(Category.FRUCHTIG);
        beerenSahne.setPreis(39.90);
        beerenSahne.setBildUrl("/assets/images/Kuchen_beeren-Sahne.png");
        Cake karamellCrunch = new Cake();
        karamellCrunch.setCategory(Category.KARAMELL);

        karamellCrunch.setName("Karamell-Crunch");
        karamellCrunch.setBeschreibung("Saftiger Karamellkuchen mit knusprigem Topping");
        karamellCrunch.setPreis(45.00);
        karamellCrunch.setCategory(Category.KARAMELL);
        karamellCrunch.setBildUrl("/src/assets/images/Kuchen_karamell-crunch.png");
        Cake zitronMohn = new Cake();
        zitronMohn.setName("Zitrone-Mohn");
        zitronMohn.setBeschreibung("Frischer Zitronenkuchen mit feinem Mohnaroma");
        zitronMohn.setBildUrl("/src/assets/images/Kuchen_zitrone-mohn.png");
        zitronMohn.setCategory(Category.SONSTIGES);
        zitronMohn.setPreis(39.90);

        Cake mangoCheesecake = new Cake();
        mangoCheesecake.setName("Mango-Cheesecake");
        mangoCheesecake.setBeschreibung("Cremiger Frischkäsekuchen mit frischer Mangosauce");
        mangoCheesecake.setPreis(34.90);
        mangoCheesecake.setBildUrl("/src/assets/images/Kuchen_mango-cheesecake.png");
        mangoCheesecake.setCategory(Category.FRUCHTIG);
        Cake schokoErdnuss = new Cake();
        schokoErdnuss.setName("Schoko-Erdnussbutter");
        schokoErdnuss.setBeschreibung("Intensiver Schokokuchen mit salziger Erdnussbuttercreme");
        schokoErdnuss.setPreis(43.90);

        schokoErdnuss.setCategory(Category.SCHOKOLADIG);
        schokoErdnuss.setBildUrl("/src/assets/images/Kuchen_schoko-erdnussbutter.png");
        Cake honigMandel = new Cake();
        honigMandel.setName("Honig-Mandel");
        honigMandel.setCategory(Category.SONSTIGES);
        honigMandel.setBeschreibung("Feiner Honigteig mit karamellisierten Mandeln");
        honigMandel.setPreis(34.90);
        honigMandel.setBildUrl("/src/assets/images/Kuchen_honig-mandel.png");

        cakeRepository.saveAll(Arrays.asList(lotusKaramell, schokoGanache, pistazienHimbeer, beerenSahne, honigMandel,
                karamellCrunch, zitronMohn, mangoCheesecake, schokoErdnuss));
                  OrderItem Orderitem1 = new OrderItem();
        Orderitem1.setCake(lotusKaramell);
        Orderitem1.setName("Lotus-Karamell");
        Orderitem1.setPrice(39.90);
        Orderitem1.setQuantity(3);
        OrderItem Orderitem2 = new OrderItem();
        Orderitem2.setCake(lotusKaramell); 
        Orderitem2.setName("Lotus-Karamell");
        Orderitem2.setPrice(39.90);
        Orderitem2.setQuantity(3);
        final User kunde = userRepository.findByEmail("maxmuster+kunde@gmail.com")
                .orElseThrow(() -> new EntityNotFoundException("Kunde nicht gefunden"));
        User admin = userRepository.findByEmail("maxmuster+admin@gmail.com")
                .orElseThrow(() -> new EntityNotFoundException("Admin nicht gefunden"));
        Order order1 = new Order();
        order1.setTotal(79.80);
        order1.setStatus("offen");
        order1.setCreatedAt(LocalDateTime.now());
        order1.setUser(kunde);
        order1.setItems(Arrays.asList(Orderitem1));

        Order order2 = new Order();
        order2.setTotal(79.80);
        order2.setStatus("offen");
        order2.setCreatedAt(LocalDateTime.now());
        order2.setUser(admin);
        order2.setItems(Arrays.asList(Orderitem2));

        orderRepo.saveAll(Arrays.asList(order1, order2));
        
        User anna = userRepository.findByEmail("anna@example.com")
                .orElseThrow(() -> new EntityNotFoundException("User Anna not found"));
        User oli = userRepository.findByEmail("oli.doe@example.com")
                .orElseThrow(() -> new EntityNotFoundException("User Oli not found"));
        User ben = userRepository.findByEmail("ben@example.com")
                .orElseThrow(() -> new EntityNotFoundException("User Ben not found"));
        User chris = userRepository.findByEmail("chris@example.com")
                .orElseThrow(() -> new EntityNotFoundException("User Chris not found"));
                

        // Add reviews
        Review r1a = new Review();
        r1a.setStars(5);
        r1a.setText("wunderschöne Torte");
        r1a.setUser(anna);
        r1a.setCake(lotusKaramell);

        Review r1b = new Review();
        r1b.setStars(4);
        r1b.setText("Bin ziemlich zufrieden.");
        r1b.setUser(oli);
        r1b.setCake(lotusKaramell);

        Review r2 = new Review();
        r2.setStars(4);
        r2.setText("schmeckt gut");
        r2.setUser(ben);
        r2.setCake(schokoGanache);

        Review r3 = new Review();
        r3.setStars(3);
        r3.setText("Die Lieferung hat den Kuchen leider etwas rumgeschüttelt");
        r3.setUser(chris);
        r3.setCake(pistazienHimbeer);

        reviewRepository.saveAll(Arrays.asList(r1a, r1b, r2, r3));

        LOGGER.info("Initial data loaded successfully.");
    }
}