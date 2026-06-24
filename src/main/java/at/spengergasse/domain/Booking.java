package at.spengergasse.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public class Booking {

    @Id
    private Long bookingId;

    private String gastName;
    private String zimmerKategorie;
    private LocalDate vonDatum;
    private LocalDate bisDatum;
    private int anzahlGaeste;
    private double gesamtpreis;

    private static final AtomicLong sequence = new AtomicLong(1000);

    protected Booking() {
    }

    public Booking(String gastName, String zimmerKategorie, LocalDate vonDatum,
                   LocalDate bisDatum, int anzahlGaeste, double gesamtpreis) {
        this.gastName = gastName;
        this.zimmerKategorie = zimmerKategorie;
        this.vonDatum = vonDatum;
        this.bisDatum = bisDatum;
        this.anzahlGaeste = anzahlGaeste;
        this.gesamtpreis = gesamtpreis;
        setBookingId();
    }

    public void setBookingId() {
        bookingId = sequence.getAndIncrement();
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getGastName() {
        return gastName;
    }

    public String getZimmerKategorie() {
        return zimmerKategorie;
    }

    public LocalDate getVonDatum() {
        return vonDatum;
    }

    public LocalDate getBisDatum() {
        return bisDatum;
    }

    public int getAnzahlGaeste() {
        return anzahlGaeste;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }
}