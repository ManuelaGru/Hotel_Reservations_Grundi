package at.spengergasse.service;

import at.spengergasse.domain.Booking;
import at.spengergasse.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class BookingService {

    private final BookingRepository repository;

    private final Map<String, Integer> basePrices = Map.of(
            "Hotelzimmer", 90,
            "Ferienwohnung", 120,
            "Privatzimmer", 60,
            "Villa", 350,
            "Airbnb", 0
    );

    private final Map<String, Integer> maxGaeste = createMaxGaeste();
    private final Map<String, Integer> verpflegungsAufpreis = createVerpflegungsAufpreis();

    // ──────────────────────────────────────────────
    // Gleiche Region/Land-Struktur wie in PriceListView:
    // Map<Region, Map<Land, Preisfaktor>>
    // ──────────────────────────────────────────────
    private final Map<String, Map<String, Double>> countriesByRegion = createCountriesByRegion();

    private static Map<String, Integer> createMaxGaeste() {
        Map<String, Integer> limits = new LinkedHashMap<>();
        limits.put("Hotelzimmer", 4);
        limits.put("Ferienwohnung", 6);
        limits.put("Privatzimmer", 2);
        limits.put("Villa", 10);
        return limits;
    }

    private static Map<String, Integer> createVerpflegungsAufpreis() {
        Map<String, Integer> aufpreis = new LinkedHashMap<>();
        aufpreis.put("Keine", 0);
        aufpreis.put("Frühstück", 12);
        aufpreis.put("Halbpension", 28);
        aufpreis.put("Vollpension", 45);
        return aufpreis;
    }

    private static Map<String, Map<String, Double>> createCountriesByRegion() {
        Map<String, Map<String, Double>> data = new LinkedHashMap<>();

        Map<String, Double> europa = new LinkedHashMap<>();
        europa.put("Österreich", 1.0);
        europa.put("Deutschland", 1.05);
        europa.put("Schweiz", 1.4);
        europa.put("Italien", 1.1);
        europa.put("Frankreich", 1.15);
        europa.put("Spanien", 0.95);
        europa.put("Griechenland", 0.9);
        data.put("Europa", europa);

        Map<String, Double> usa = new LinkedHashMap<>();
        usa.put("New York", 1.6);
        usa.put("Florida", 1.3);
        usa.put("Kalifornien", 1.5);
        usa.put("Texas", 1.2);
        usa.put("Hawaii", 1.8);
        data.put("USA", usa);

        Map<String, Double> china = new LinkedHashMap<>();
        china.put("Peking", 1.2);
        china.put("Shanghai", 1.25);
        china.put("Hongkong", 1.4);
        data.put("China", china);

        Map<String, Double> japan = new LinkedHashMap<>();
        japan.put("Tokio", 1.6);
        japan.put("Osaka", 1.4);
        japan.put("Kyoto", 1.5);
        data.put("Japan", japan);

        Map<String, Double> afrika = new LinkedHashMap<>();
        afrika.put("Ägypten", 0.7);
        afrika.put("Marokko", 0.75);
        afrika.put("Südafrika", 0.85);
        afrika.put("Kenia", 0.8);
        data.put("Afrika", afrika);

        return data;
    }

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }
    public void deleteById(Long bookingId) {
        repository.deleteById(bookingId);
    }
    // ──────────────────────────────────────────────
    // Jetzt mit Region + Land statt nur Kategorie
    // ──────────────────────────────────────────────
    public Booking createBooking(String name, String kategorie, LocalDate von,
                                 LocalDate bis, int gaeste, String verpflegung,
                                 String region, String land) {
        validateBookingData(name, kategorie, von, bis, gaeste);

        long nights = ChronoUnit.DAYS.between(von, bis);
        int basePreis = basePrices.get(kategorie);

        double landFaktor = countriesByRegion.get(region).get(land);
        double preisProNacht = basePreis * landFaktor;

        String tatsaechlicheVerpflegung = "Keine";
        if ("Hotelzimmer".equals(kategorie) && verpflegung != null) {
            tatsaechlicheVerpflegung = verpflegung;
            int aufpreisProPersonUndNacht = verpflegungsAufpreis.getOrDefault(verpflegung, 0);
            preisProNacht += aufpreisProPersonUndNacht * gaeste;
        }

        double gesamtpreis = nights * preisProNacht;

        Booking booking = new Booking(name, kategorie, von, bis, gaeste, gesamtpreis,
                tatsaechlicheVerpflegung, region, land);
        return repository.save(booking);
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }

    public Map<String, Integer> getBasePrices() {
        return basePrices;
    }

    public Map<String, Integer> getVerpflegungsAufpreis() {
        return verpflegungsAufpreis;
    }

    public Map<String, Map<String, Double>> getCountriesByRegion() {
        return countriesByRegion;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public String addTenSampleBookings() {
        String[] namen = {"Huber", "Gruber", "Wagner", "Bauer", "Hofer",
                "Eder", "Schmid", "Pichler", "Leitner", "Fuchs"};
        String[] kategorien = {"Hotelzimmer", "Ferienwohnung", "Privatzimmer", "Villa"};
        String[] verpflegungsarten = {"Keine", "Frühstück", "Halbpension", "Vollpension"};
        String[] regionen = countriesByRegion.keySet().toArray(new String[0]);

        Random random = new Random();
        int erfolgreich = 0;
        int abgelehnt = 0;

        for (int i = 0; i < 10; i++) {
            String name = namen[random.nextInt(namen.length)];
            String kategorie = kategorien[random.nextInt(kategorien.length)];
            LocalDate von = LocalDate.now().plusDays(random.nextInt(30));
            LocalDate bis = von.plusDays(1 + random.nextInt(7));
            int gaeste = 1 + random.nextInt(6);
            String verpflegung = verpflegungsarten[random.nextInt(verpflegungsarten.length)];
            String region = regionen[random.nextInt(regionen.length)];
            String[] laenderInRegion = countriesByRegion.get(region).keySet().toArray(new String[0]);
            String land = laenderInRegion[random.nextInt(laenderInRegion.length)];

            try {
                createBooking(name, kategorie, von, bis, gaeste, verpflegung, region, land);
                erfolgreich++;
            } catch (IllegalArgumentException ex) {
                System.out.println("Abgelehnt: " + name + " (" + kategorie + ", " + gaeste + " Gäste) – " + ex.getMessage());
                abgelehnt++;
            }
        }

        return erfolgreich + " Buchungen hinzugefügt, " + abgelehnt + " abgelehnt (zu viele Gäste).";
    }

    private void validateBookingData(String name, String kategorie, LocalDate von,
                                     LocalDate bis, int gaeste) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bitte einen Namen eingeben.");
        }
        if (von == null || bis == null) {
            throw new IllegalArgumentException("Bitte Von- und Bis-Datum auswählen.");
        }

        long nights = ChronoUnit.DAYS.between(von, bis);
        if (nights <= 0) {
            throw new IllegalArgumentException("Bis-Datum muss nach dem Von-Datum liegen.");
        }

        Integer limit = maxGaeste.get(kategorie);
        if (limit != null && gaeste > limit) {
            throw new IllegalArgumentException(
                    kategorie + " erlaubt maximal " + limit + " Personen (eingegeben: " + gaeste + ")."
            );
        }
    }

}