package at.spengergasse.views.bookings;

import at.spengergasse.domain.Booking;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Bookings")
@Route("Bookings")
@Menu(order = 1, icon = LineAwesomeIconUrl.CALENDAR_CHECK)
public class BookingsView extends VerticalLayout {

    private final Map<String, Integer> basePrices = Map.of(
            "Hotelzimmer", 90,
            "Ferienwohnung", 120,
            "Privatzimmer", 60,
            "Villa", 350,
            "Airbnb", 0  // Preis variiert je nach Inserat ("nach Angebot")
    );

    // ──────────────────────────────────────────────
    // Maximale Personenzahl pro Kategorie.
    // "Airbnb" bewusst NICHT enthalten -> bedeutet
    // "nach Angebot" = kein festes Limit
    // ──────────────────────────────────────────────
    private final Map<String, Integer> maxGaeste = createMaxGaeste();

    private static Map<String, Integer> createMaxGaeste() {
        Map<String, Integer> limits = new LinkedHashMap<>();
        limits.put("Hotelzimmer", 4);
        limits.put("Ferienwohnung", 6);
        limits.put("Privatzimmer", 2);
        limits.put("Villa", 10);
        return limits;
    }

    private final List<Booking> bookings = new ArrayList<>();

    private TextField gastName;
    private ComboBox<String> zimmerKategorie;
    private DatePicker vonDatum;
    private DatePicker bisDatum;
    private IntegerField anzahlGaeste;
    private Grid<Booking> grid;

    public BookingsView() {
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle()
                .set("text-align", "center")
                .set("background-image", "linear-gradient(rgba(255,255,255,0.75), rgba(255,255,255,0.75)), url('images/Strandaussicht.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-attachment", "fixed");

        H2 header = new H2("Buchung anlegen");
        header.getStyle()
                .set("font-size", "26px")
                .set("font-weight", "bold")
                .set("font-style", "italic")
                .set("color", "#2E8B57")
                .set("margin-bottom", "1rem");

        gastName = new TextField("Name");
        gastName.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        zimmerKategorie = new ComboBox<>("Unterkunftsart");
        zimmerKategorie.setItems(basePrices.keySet());
        zimmerKategorie.setValue("Hotelzimmer");
        zimmerKategorie.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        vonDatum = new DatePicker("Von");
        bisDatum = new DatePicker("Bis");
        vonDatum.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");
        bisDatum.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        anzahlGaeste = new IntegerField("Anzahl Gäste");
        anzahlGaeste.setValue(1);
        anzahlGaeste.setMin(1);
        anzahlGaeste.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        HorizontalLayout row1 = new HorizontalLayout(gastName, zimmerKategorie);
        row1.setAlignItems(Alignment.BASELINE);
        row1.getStyle().set("gap", "2rem");

        HorizontalLayout row2 = new HorizontalLayout(vonDatum, bisDatum, anzahlGaeste);
        row2.setAlignItems(Alignment.BASELINE);
        row2.getStyle().set("gap", "2rem");

        Button buchen = new Button("Buchung anlegen", e -> createBooking());
        buchen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        grid = new Grid<>(Booking.class, false);
        grid.addColumn(Booking::getBookingId).setHeader("ID");
        grid.addColumn(Booking::getGastName).setHeader("Name");
        grid.addColumn(Booking::getZimmerKategorie).setHeader("Kategorie");
        grid.addColumn(Booking::getVonDatum).setHeader("Von");
        grid.addColumn(Booking::getBisDatum).setHeader("Bis");
        grid.addColumn(Booking::getAnzahlGaeste).setHeader("Gäste");
        grid.addColumn(b -> b.getGesamtpreis() + " €").setHeader("Preis");
        grid.setWidth("800px");
        grid.setHeight("300px");

        add(header, row1, row2, buchen, grid);
    }

    // ──────────────────────────────────────────────
    // Liest die Formularwerte aus, prüft sie (Validierung
    // mit Exceptions) und legt bei Erfolg eine neue
    // Buchung an
    // ──────────────────────────────────────────────
    private void createBooking() {
        try {
            validateInput();

            LocalDate von = vonDatum.getValue();
            LocalDate bis = bisDatum.getValue();
            long nights = ChronoUnit.DAYS.between(von, bis);

            int preisProNacht = basePrices.get(zimmerKategorie.getValue());
            double gesamtpreis = nights * preisProNacht;

            Booking booking = new Booking(
                    gastName.getValue(),
                    zimmerKategorie.getValue(),
                    von,
                    bis,
                    anzahlGaeste.getValue(),
                    gesamtpreis
            );

            bookings.add(booking);
            grid.setItems(bookings);

            Notification.show("Buchung für " + gastName.getValue() + " wurde angelegt!");

            gastName.clear();
            vonDatum.clear();
            bisDatum.clear();
            anzahlGaeste.setValue(1);

        } catch (IllegalArgumentException ex) {
            // Validierungsfehler: Meldung direkt aus der Exception anzeigen
            Notification.show(ex.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // Prüft alle Eingaben und wirft bei Problemen eine
    // IllegalArgumentException mit passender Meldung
    // ──────────────────────────────────────────────
    private void validateInput() {
        if (gastName.isEmpty()) {
            throw new IllegalArgumentException("Bitte einen Namen eingeben.");
        }
        if (vonDatum.isEmpty() || bisDatum.isEmpty()) {
            throw new IllegalArgumentException("Bitte Von- und Bis-Datum auswählen.");
        }

        LocalDate von = vonDatum.getValue();
        LocalDate bis = bisDatum.getValue();
        long nights = ChronoUnit.DAYS.between(von, bis);

        if (nights <= 0) {
            throw new IllegalArgumentException("Bis-Datum muss nach dem Von-Datum liegen.");
        }

        String kategorie = zimmerKategorie.getValue();
        int gaeste = anzahlGaeste.getValue();

        // "Airbnb" ist absichtlich NICHT in maxGaeste enthalten
        // -> kein Limit, da Personenzahl je nach Inserat variiert
        Integer limit = maxGaeste.get(kategorie);
        if (limit != null && gaeste > limit) {
            throw new IllegalArgumentException(
                    kategorie + " erlaubt maximal " + limit + " Personen (eingegeben: " + gaeste + ")."
            );
        }
    }

}