package at.spengergasse.views.bookings;

import at.spengergasse.domain.Booking;
import at.spengergasse.service.BookingService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.util.Map;

@PageTitle("Bookings")
@Route("Bookings")
@Menu(order = 1, icon = LineAwesomeIconUrl.CALENDAR_CHECK)
public class BookingsView extends VerticalLayout {

    private final BookingService bookingService;

    private TextField gastName;
    private ComboBox<String> zimmerKategorie;
    private DatePicker vonDatum;
    private DatePicker bisDatum;
    private IntegerField anzahlGaeste;
    private ComboBox<String> verpflegung;
    private ComboBox<String> region;
    private ComboBox<String> land;
    private Grid<Booking> grid;
    private Button removeAll;

    public BookingsView(BookingService bookingService) {
        this.bookingService = bookingService;

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
        zimmerKategorie.setItems(bookingService.getBasePrices().keySet());
        zimmerKategorie.setValue("Hotelzimmer");
        zimmerKategorie.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        region = new ComboBox<>("Region");
        region.setItems(bookingService.getCountriesByRegion().keySet());
        region.setValue("Europa");
        region.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        land = new ComboBox<>("Land");
        land.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        region.addValueChangeListener(e -> updateLandOptions());
        updateLandOptions();

        vonDatum = new DatePicker("Von");
        bisDatum = new DatePicker("Bis");
        vonDatum.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");
        bisDatum.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        anzahlGaeste = new IntegerField("Anzahl Gäste");
        anzahlGaeste.setValue(1);
        anzahlGaeste.setMin(1);
        anzahlGaeste.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        verpflegung = new ComboBox<>("Verpflegung");
        verpflegung.setItems(bookingService.getVerpflegungsAufpreis().keySet());
        verpflegung.setValue("Keine");
        verpflegung.getStyle().set("--lumo-contrast-10pct", "#AFEEEE");

        zimmerKategorie.addValueChangeListener(e -> updateVerpflegungVisibility());

        HorizontalLayout row1 = new HorizontalLayout(gastName, zimmerKategorie, region, land);
        row1.setAlignItems(Alignment.BASELINE);
        row1.getStyle().set("gap", "1.5rem");

        HorizontalLayout row2 = new HorizontalLayout(vonDatum, bisDatum, anzahlGaeste, verpflegung);
        row2.setAlignItems(Alignment.BASELINE);
        row2.getStyle().set("gap", "1.5rem");

        updateVerpflegungVisibility();

        Button buchen = new Button("Buchung anlegen", e -> createBooking());
        buchen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        removeAll = new Button("Remove All", e -> openConfirmDeleteDialog());
        removeAll.getStyle()
                .set("background-color", "#FFCDD2")
                .set("color", "#7A0000")
                .set("border", "1px solid transparent")
                .set("transition", "border-color 0.1s")
                .set("font-size", "7px")
                .set("padding", "2px 6px")
                .set("min-width", "0");
        removeAll.getElement().executeJs(
                "this.addEventListener('mousedown', () => { if (!this.disabled) this.style.border='2px solid #D32F2F'; });" +
                        "this.addEventListener('mouseup', () => this.style.border='1px solid transparent');" +
                        "this.addEventListener('mouseleave', () => this.style.border='1px solid transparent');"
        );

        Button addTen = new Button("Add 10", e -> {
            String ergebnis = bookingService.addTenSampleBookings();
            refreshGrid();
            Notification.show(ergebnis);
        });
        addTen.getStyle()
                .set("background-color", "#E0E0E0")
                .set("color", "#212121")
                .set("border", "1px solid transparent")
                .set("transition", "border-color 0.1s")
                .set("font-size", "7px")
                .set("padding", "2px 6px")
                .set("min-width", "0");
        addTen.getElement().executeJs(
                "this.addEventListener('mousedown', () => this.style.border='2px solid black');" +
                        "this.addEventListener('mouseup', () => this.style.border='1px solid transparent');" +
                        "this.addEventListener('mouseleave', () => this.style.border='1px solid transparent');"
        );

        Button addWrong = new Button("Add WRONG booking", e -> addWrongBooking());
        addWrong.getStyle()
                .set("background-color", "#FFE0B2")
                .set("color", "#7A3E00")
                .set("border", "1px solid transparent")
                .set("transition", "border-color 0.1s")
                .set("font-size", "7px")
                .set("padding", "2px 6px")
                .set("min-width", "0");
        addWrong.getElement().executeJs(
                "this.addEventListener('mousedown', () => this.style.border='2px solid #E65100');" +
                        "this.addEventListener('mouseup', () => this.style.border='1px solid transparent');" +
                        "this.addEventListener('mouseleave', () => this.style.border='1px solid transparent');"
        );

        HorizontalLayout buttonRow = new HorizontalLayout(buchen, removeAll, addTen, addWrong);
        buttonRow.setSpacing(true);
        buttonRow.setAlignItems(Alignment.CENTER);
        buttonRow.getStyle().set("gap", "1rem");

        grid = new Grid<>(Booking.class, false);
        grid.addColumn(Booking::getBookingId).setHeader("ID").setSortable(true);
        grid.addColumn(Booking::getGastName).setHeader("Name").setSortable(true);
        grid.addColumn(Booking::getZimmerKategorie).setHeader("Kategorie").setSortable(true);
        grid.addColumn(Booking::getLand).setHeader("Land").setSortable(true);
        grid.addColumn(Booking::getVonDatum).setHeader("Von").setSortable(true);
        grid.addColumn(Booking::getBisDatum).setHeader("Bis").setSortable(true);
        grid.addColumn(Booking::getAnzahlGaeste).setHeader("Gäste").setSortable(true);
        grid.addColumn(Booking::getVerpflegungAnzeige).setHeader("Verpflegung").setSortable(true);
        grid.addColumn(b -> b.getGesamtpreis() + " €").setHeader("Preis").setSortable(true);

        // ── NEU: Aktion-Spalte mit Löschen-Button pro Zeile ──
        grid.addComponentColumn(booking -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> {
                bookingService.deleteById(booking.getBookingId());
                refreshGrid();
                Notification.show("Buchung von " + booking.getGastName() + " wurde gelöscht.");
            });
            return deleteButton;
        }).setHeader("Aktion").setSortable(false);

        grid.setWidth("1250px");
        grid.setHeight("400px");
        grid.getStyle()
                .set("margin", "0 auto")
                .set("background-color", "rgba(200, 200, 200, 0.5)")
                .set("border-radius", "8px");

        Div gridWrapper = new Div(grid);
        gridWrapper.setWidthFull();
        gridWrapper.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("margin-top", "2rem");

        add(header, row1, row2, buttonRow, gridWrapper);

        refreshGrid();
    }

    private void updateLandOptions() {
        Map<String, Double> countries = bookingService.getCountriesByRegion().get(region.getValue());
        land.setItems(countries.keySet());
        land.setValue(countries.keySet().iterator().next());
    }

    private void updateVerpflegungVisibility() {
        boolean istHotel = "Hotelzimmer".equals(zimmerKategorie.getValue());
        verpflegung.setEnabled(istHotel);
        if (!istHotel) {
            verpflegung.setValue("Keine");
        }
    }

    private void createBooking() {
        try {
            bookingService.createBooking(
                    gastName.getValue(),
                    zimmerKategorie.getValue(),
                    vonDatum.getValue(),
                    bisDatum.getValue(),
                    anzahlGaeste.getValue(),
                    verpflegung.getValue(),
                    region.getValue(),
                    land.getValue()
            );

            refreshGrid();
            Notification.show("Buchung für " + gastName.getValue() + " wurde angelegt!");

            gastName.clear();
            vonDatum.clear();
            bisDatum.clear();
            anzahlGaeste.setValue(1);
            verpflegung.setValue("Keine");

        } catch (IllegalArgumentException ex) {
            Notification.show(ex.getMessage());
        }
    }

    private void addWrongBooking() {
        try {
            bookingService.createBooking(
                    "Testperson (WRONG)",
                    "Hotelzimmer",
                    LocalDate.now(),
                    LocalDate.now().plusDays(2),
                    99,
                    "Keine",
                    "Europa",
                    "Österreich"
            );
            refreshGrid();
        } catch (IllegalArgumentException ex) {
            Notification.show("Erwarteter Fehler: " + ex.getMessage());
        }
    }

    private void refreshGrid() {
        var bookings = bookingService.findAll();
        grid.setItems(bookings);
        removeAll.setEnabled(!bookings.isEmpty());
    }

    private void openConfirmDeleteDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Wirklich alle Buchungen löschen?");

        Paragraph warnText = new Paragraph("Dieser Vorgang kann nicht rückgängig gemacht werden.");

        Button bestaetigen = new Button("Ja, alle löschen", e -> {
            bookingService.deleteAll();
            refreshGrid();
            Notification.show("Alle Buchungen wurden gelöscht.");
            dialog.close();
        });
        bestaetigen.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button abbrechen = new Button("Abbrechen", e -> dialog.close());

        dialog.add(warnText);
        dialog.getFooter().add(abbrechen, bestaetigen);
        dialog.open();
    }

}