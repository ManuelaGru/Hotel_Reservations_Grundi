package at.spengergasse.views.pricelist;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

@PageTitle("PriceList")
@Route("PriceList")
@Menu(order = 2, icon = LineAwesomeIconUrl.ANGLE_DOUBLE_RIGHT_SOLID)
public class PriceListView extends VerticalLayout {

    private final Map<String, Integer> basePrices = Map.of(
            "Hotelzimmer", 90,
            "Ferienwohnung", 120,
            "Privatzimmer", 60,
            "Villa", 350
    );

    // ──────────────────────────────────────────────
    // Pro Region: Map<Land, Preisfaktor>
    // LinkedHashMap, damit die Reihenfolge im Dropdown
    // immer gleich bleibt (nicht zufällig wie bei Map.of)
    // ──────────────────────────────────────────────
    private final Map<String, Map<String, Double>> countriesByRegion = createCountriesByRegion();

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

    private ComboBox<String> category;
    private ComboBox<String> region;
    private ComboBox<String> country;
    private DatePicker fromDate;
    private DatePicker toDate;
    private H3 priceLabel;

    public PriceListView() {
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

        H3 flightHeader = new H3("Flugbuchung");
        flightHeader.getStyle()
                .set("font-size", "20px")
                .set("font-weight", "bold")
                .set("color", "#003366")
                .set("margin-bottom", "0.5rem");

        Anchor skyscanner = new Anchor("https://www.skyscanner.de", "Skyscanner – Flüge vergleichen");
        skyscanner.setTarget("_blank");
        skyscanner.getStyle().set("font-size", "16px");

        Anchor googleFlights = new Anchor("https://www.google.com/travel/flights", "Google Flights");
        googleFlights.setTarget("_blank");
        googleFlights.getStyle().set("font-size", "16px");

        Anchor checkfelix = new Anchor("https://www.checkfelix.com", "Checkfelix");
        checkfelix.setTarget("_blank");
        checkfelix.getStyle().set("font-size", "16px");

        VerticalLayout flightLinks = new VerticalLayout(skyscanner, googleFlights, checkfelix);
        flightLinks.setSpacing(false);
        flightLinks.setAlignItems(Alignment.CENTER);
        flightLinks.getStyle().set("margin-bottom", "2.5rem");

        H2 header = new H2("Unsere Preisliste");
        header.getStyle()
                .set("font-size", "26px")
                .set("font-weight", "bold")
                .set("font-style", "italic")
                .set("color", "#2E8B57")
                .set("margin-bottom", "1rem");

        fromDate = new DatePicker("Von");
        toDate = new DatePicker("Bis");
        fromDate.addValueChangeListener(e -> updatePrice());
        toDate.addValueChangeListener(e -> updatePrice());
        fromDate.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");
        toDate.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        HorizontalLayout dateRow = new HorizontalLayout(fromDate, toDate);
        dateRow.setAlignItems(Alignment.BASELINE);
        dateRow.getStyle().set("gap", "3rem");

        category = new ComboBox<>("Unterkunftsart");
        category.setItems(basePrices.keySet());
        category.setValue("Hotelzimmer");
        category.addValueChangeListener(e -> updatePrice());
        category.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        region = new ComboBox<>("Region");
        region.setItems(countriesByRegion.keySet());
        region.setValue("Europa");
        region.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        // Land-Dropdown: Inhalt hängt von der gewählten Region ab
        country = new ComboBox<>("Land");
        country.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        // priceLabel MUSS existieren, bevor updateCountryOptions()
        // zum ersten Mal aufgerufen wird (das löst sonst eine
        // NullPointerException in updatePrice() aus)
        priceLabel = new H3();

        // Wenn sich die Region ändert: Land-Liste neu befüllen
        region.addValueChangeListener(e -> updateCountryOptions());
        country.addValueChangeListener(e -> updatePrice());

        // Beim Start einmal initial befüllen (Region = Europa)
        updateCountryOptions();

        HorizontalLayout selection = new HorizontalLayout(category, region, country);
        selection.setAlignItems(Alignment.BASELINE);
        selection.getStyle().set("gap", "2rem");

        Paragraph note = new Paragraph("Pauschalangebote möglich auf Anfrage.");
        note.getStyle().set("font-style", "italic").set("margin-top", "1.5rem");

        add(flightHeader, flightLinks, header, dateRow, selection, priceLabel, note);

        updatePrice();
    }

    // ──────────────────────────────────────────────
    // Befüllt das Land-Dropdown passend zur aktuell
    // gewählten Region und wählt automatisch das
    // erste Land vor
    // ──────────────────────────────────────────────
    private void updateCountryOptions() {
        Map<String, Double> countries = countriesByRegion.get(region.getValue());
        country.setItems(countries.keySet());
        country.setValue(countries.keySet().iterator().next());
        // setValue löst addValueChangeListener aus -> updatePrice() wird automatisch aufgerufen
    }

    private void updatePrice() {
        if (country.getValue() == null) {
            return; // noch nicht initialisiert
        }

        int base = basePrices.get(category.getValue());
        double factor = countriesByRegion.get(region.getValue()).get(country.getValue());
        int pricePerNight = (int) Math.round(base * factor);

        if (fromDate.getValue() != null && toDate.getValue() != null) {
            LocalDate from = fromDate.getValue();
            LocalDate to = toDate.getValue();
            long nights = ChronoUnit.DAYS.between(from, to);

            if (nights > 0) {
                int total = (int) (nights * pricePerNight);
                priceLabel.setText(category.getValue() + " in " + country.getValue() + ": "
                        + pricePerNight + " €/Nacht × " + nights + " Nächte = " + total + " €");
            } else {
                priceLabel.setText("Bitte gültigen Zeitraum wählen.");
            }
        } else {
            priceLabel.setText(category.getValue() + " in " + country.getValue() + ": " + pricePerNight + " €/Nacht");
        }
    }

}