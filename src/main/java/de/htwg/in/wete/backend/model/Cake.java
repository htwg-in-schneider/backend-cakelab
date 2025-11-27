package de.htwg.in.wete.backend.model;


public class Cake {
    private long id;
    private String name;
    private String beschreibung;
    private double preis;
    private String bildUrl;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public double getPreis() { return preis; }
    public void setPreis(double preis) { this.preis = preis; }

    public String getBildUrl() { return bildUrl; }
    public void setBildUrl(String bildUrl) { this.bildUrl = bildUrl; }
}
