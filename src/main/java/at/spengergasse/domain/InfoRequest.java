package at.spengergasse.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.concurrent.atomic.AtomicLong;

@Entity
public class InfoRequest {

    @Id
    private Long infoRequestId;

    @NotBlank(message = "Bitte einen Vornamen eingeben.")
    private String vorname;

    @NotBlank(message = "Bitte einen Nachnamen eingeben.")
    private String nachname;

    private String strasse;
    private String hausnummer;
    private String postleitzahl;
    private String ort;
    private String land;

    @NotBlank(message = "Bitte eine Telefonnummer eingeben.")
    @Pattern(regexp = "[0-9+\\-\\s]+", message = "Telefonnummer darf nur Ziffern, +, - und Leerzeichen enthalten.")
    private String telefonnummer;

    @NotBlank(message = "Bitte eine E-Mail-Adresse eingeben.")
    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String mailadresse;

    private String zusatzfragen;

    private static final AtomicLong sequence = new AtomicLong(1000);

    // JPA braucht einen leeren Konstruktor (ohne Parameter)
    protected InfoRequest() {
    }

    public InfoRequest(String vorname, String nachname, String strasse, String hausnummer,
                       String postleitzahl, String ort, String land,
                       String telefonnummer, String mailadresse, String zusatzfragen) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.land = land;
        this.telefonnummer = telefonnummer;
        this.mailadresse = mailadresse;
        this.zusatzfragen = zusatzfragen;
        setInfoRequestId();
    }

    public void setInfoRequestId() {
        this.infoRequestId = sequence.getAndIncrement();
    }

    public Long getInfoRequestId() { return infoRequestId; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getStrasse() { return strasse; }
    public void setStrasse(String strasse) { this.strasse = strasse; }

    public String getHausnummer() { return hausnummer; }
    public void setHausnummer(String hausnummer) { this.hausnummer = hausnummer; }

    public String getPostleitzahl() { return postleitzahl; }
    public void setPostleitzahl(String postleitzahl) { this.postleitzahl = postleitzahl; }

    public String getOrt() { return ort; }
    public void setOrt(String ort) { this.ort = ort; }

    public String getLand() { return land; }
    public void setLand(String land) { this.land = land; }

    public String getTelefonnummer() { return telefonnummer; }
    public void setTelefonnummer(String telefonnummer) { this.telefonnummer = telefonnummer; }

    public String getMailadresse() { return mailadresse; }
    public void setMailadresse(String mailadresse) { this.mailadresse = mailadresse; }

    public String getZusatzfragen() { return zusatzfragen; }
    public void setZusatzfragen(String zusatzfragen) { this.zusatzfragen = zusatzfragen; }
}