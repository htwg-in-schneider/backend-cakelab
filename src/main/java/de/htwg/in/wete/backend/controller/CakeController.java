package de.htwg.in.wete.backend.controller;
import de.htwg.in.wete.backend.model.Cake;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/torten")
public class CakeController {

    @GetMapping
    public List<Cake> getTorten() {

        Cake t1 = new Cake();
        t1.setId(1);
        t1.setName("Lotus-Karamell");
        t1.setBeschreibung("Kuchen mit Lotus und Karamell");
        t1.setPreis(39.90);
        t1.setBildUrl("/src/assets/images/Kuchen_Lotus-caramell.png");

        Cake t2 = new Cake();
        t2.setId(2);
        t2.setName("Schoko-Ganache");
        t2.setBeschreibung("Schokoladen kuchen");
        t2.setPreis(39.90);
        t2.setBildUrl("/src/assets/images/Kuchen_Schokolade.png");

        Cake t3 = new Cake();
        t3.setId(3);
        t3.setName("Pistazien-Himbeer");
        t3.setBeschreibung("...");
        t3.setPreis(39.90);
        t3.setBildUrl("/src/assets/images/Kuchen_pistazien-Himbeer.png");

        Cake t4 = new Cake();
        t4.setId(4);
        t4.setName("Beeren-Sahne");
        t4.setBeschreibung("...");
        t4.setPreis(39.90);
        t4.setBildUrl("/src/assets/images/Kuchen_beeren-Sahne.png");

        return Arrays.asList(t1, t2, t3, t4);
    }
}
