package at.spengergasse.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;

    @NotBlank(message = "Bitte eine Bild-URL eingeben.")
    private String imageUrl;

    @NotBlank(message = "Bitte einen Titel eingeben.")
    private String title;

    @NotBlank(message = "Bitte einen Standort eingeben.")
    private String location;

    @Positive(message = "Preis muss größer als 0 sein.")
    private int pricePerNight;

    @Min(value = 0, message = "Bewertung darf nicht negativ sein.")
    private double rating;

    protected Listing() {
    }

    public Listing(String imageUrl, String title, String location, int pricePerNight, double rating) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
    }

    public Long getListingId() { return listingId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(int pricePerNight) { this.pricePerNight = pricePerNight; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
