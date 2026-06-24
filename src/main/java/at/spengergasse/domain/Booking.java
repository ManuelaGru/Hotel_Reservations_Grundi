package at.spengergasse.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @NotBlank(message = "Name darf nicht leer sein")
    private String gastName;

    @NotBlank(message = "Bitte eine Unterkunftsart wählen")
    private String zimmerKategorie;

    @NotNull(message = "Bitte ein Von-Datum wählen")
    private LocalDate vonDatum;

    @NotNull(message = "Bitte ein Bis-Datum wählen")
    private LocalDate bisDatum;

    @Min(value = 1, message = "Mindestens 1 Gast erforderlich")
    @Max(value = 20, message = "Maximal 20 Gäste möglich")
    private int anzahlGaeste;

    private double gesamtpreis;
    private String verpflegung;
    private String region;
    private String land;

    protected Booking() {
    }

    public Booking(String gastName, String zimmerKategorie, LocalDate vonDatum,
                   LocalDate bisDatum, int anzahlGaeste, double gesamtpreis,
                   String verpflegung, String region, String land) {
        this.gastName = gastName;
        this.zimmerKategorie = zimmerKategorie;
        this.vonDatum = vonDatum;
        this.bisDatum = bisDatum;
        this.anzahlGaeste = anzahlGaeste;
        this.gesamtpreis = gesamtpreis;
        this.verpflegung = verpflegung;
        this.region = region;
        this.land = land;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getGastName() {
        return gastName;
    }

    public void setGastName(String gastName) {
        this.gastName = gastName;
    }

    public String getZimmerKategorie() {
        return zimmerKategorie;
    }

    public void setZimmerKategorie(String zimmerKategorie) {
        this.zimmerKategorie = zimmerKategorie;
    }

    public LocalDate getVonDatum() {
        return vonDatum;
    }

    public void setVonDatum(LocalDate vonDatum) {
        this.vonDatum = vonDatum;
    }

    public LocalDate getBisDatum() {
        return bisDatum;
    }

    public void setBisDatum(LocalDate bisDatum) {
        this.bisDatum = bisDatum;
    }

    public int getAnzahlGaeste() {
        return anzahlGaeste;
    }

    public void setAnzahlGaeste(int anzahlGaeste) {
        this.anzahlGaeste = anzahlGaeste;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }

    public void setGesamtpreis(double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    public String getVerpflegung() {
        return verpflegung;
    }

    public void setVerpflegung(String verpflegung) {
        this.verpflegung = verpflegung;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getVerpflegungAnzeige() {
        if (verpflegung == null || verpflegung.equals("Keine")) {
            return "Ohne Verpflegung";
        } else {
            return verpflegung;
        }
    }
}