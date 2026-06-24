package at.spengergasse.views.home;

import at.spengergasse.domain.InfoRequest;
import at.spengergasse.repository.InfoRequestRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Home")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.APP_STORE)
public class HomeView extends VerticalLayout {

    private final InfoRequestRepository infoRequestRepository;

    private TextField name;
    private Button sayHelloInfo;

    public HomeView(InfoRequestRepository infoRequestRepository) {
        this.infoRequestRepository = infoRequestRepository;

        setSpacing(false);
        setPadding(false);
        setSizeFull();

        Div hero = new Div();
        hero.getStyle()
                .set("background-image", "url('images/Strandaussicht.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("height", "50vh")
                .set("width", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center");

        H1 company = new H1("Hotel Reservations Grundi");
        company.getStyle()
                .set("color", "orange")
                .set("-webkit-text-stroke", "1px white")
                .set("text-shadow", "0 0 10px orange, 0 0 20px orange, 0 0 30px white")
                .set("font-family", "cursive")
                .set("font-size", "3rem")
                .set("margin", "0")
                .set("white-space", "nowrap");

        Image headerIcon = new Image("icons/icon.png", "Hotel Icon");
        headerIcon.setHeight("60px");
        headerIcon.getStyle().set("filter", "drop-shadow(0 0 6px white)");

        HorizontalLayout headerRow = new HorizontalLayout(company, headerIcon);
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.getStyle().set("gap", "1rem");

        hero.add(headerRow);

        Paragraph description = new Paragraph(
                "Willkommen im Hotel Reservations Grundi. Bei uns erwartet Sie " +
                        "ein erstklassiger Aufenthalt mit komfortablen Zimmern, " +
                        "exzellentem Service und allen Annehmlichkeiten für einen " +
                        "unvergesslichen Urlaub." + " Sie finden genau den Aufenthalt," +
                        "den Sie benötigen - egal ob ruhig und verträumt oder aufregend " +
                        "oder einfach ganz an Ihre persönlichen Vorgaben angepasst!"
        );
        description.getStyle()
                .set("font-style", "italic")
                .set("font-family", "Arial")
                .set("font-size", "16px")
                .set("width", "500px");

        Image logo = new Image("images/LOGO.jpg", "Hotel Logo");
        logo.setHeight("150px");
        HorizontalLayout descriptionRow = new HorizontalLayout(description, logo);
        descriptionRow.setAlignItems(Alignment.CENTER);
        descriptionRow.getStyle()
                .set("margin", "2rem auto 0 auto")
                .set("padding-left", "100px");

        logo.getStyle().set("transform", "rotate(-16deg)");

        name = new TextField("Your name");
        sayHelloInfo = new Button("Send me Information");
        sayHelloInfo.addClickListener(e -> openRequestDialog());
        sayHelloInfo.addClickShortcut(Key.ENTER);

        Div impressum = new Div();
        impressum.getStyle()
                .set("text-align", "center")
                .set("font-size", "12px")
                .set("color", "gray")
                .set("margin-top", "2rem");
        impressum.add(new Paragraph("Grundbek Manuela, Hochgasse 17/3, 1010 Wien"));
        impressum.add(new Paragraph("Tel: +43 676 5842159 | Mail: TraumurlaubOhneStress@gmail.com"));

        setAlignItems(Alignment.CENTER);
        add(hero, descriptionRow, name, sayHelloInfo, impressum);
    }

    private void openRequestDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Informationen anfordern");
        dialog.setWidth("500px");

        TextField vorname = new TextField("Vorname");
        vorname.setValue(name.getValue() != null ? name.getValue() : "");
        vorname.setWidthFull();

        TextField nachname = new TextField("Nachname");
        nachname.setWidthFull();

        TextField strasse = new TextField("Straße");
        TextField hausnummer = new TextField("Hausnummer");
        HorizontalLayout strasseRow = new HorizontalLayout(strasse, hausnummer);
        strasseRow.setWidthFull();
        strasse.setWidth("70%");
        hausnummer.setWidth("30%");

        TextField plz = new TextField("Postleitzahl");
        TextField ort = new TextField("Ort");
        HorizontalLayout ortRow = new HorizontalLayout(plz, ort);
        ortRow.setWidthFull();
        plz.setWidth("30%");
        ort.setWidth("70%");

        ComboBox<String> land = new ComboBox<>("Land");
        land.setItems(
                "Österreich", "Deutschland", "Schweiz", "Italien",
                "Frankreich", "Spanien", "Niederlande", "Polen",
                "Tschechien", "Ungarn", "Slowakei", "Slowenien", "Kroatien"
        );
        land.setValue("Österreich");
        land.setWidthFull();

        TextField telefonnummer = new TextField("Telefonnummer");
        telefonnummer.setWidthFull();

        TextField mailadresse = new TextField("E-Mail-Adresse");
        mailadresse.setWidthFull();

        TextArea zusatzfragen = new TextArea("Zusätzliche Fragen / Anmerkungen");
        zusatzfragen.setWidthFull();

        VerticalLayout formLayout = new VerticalLayout(
                vorname, nachname, strasseRow, ortRow, land,
                telefonnummer, mailadresse, zusatzfragen
        );
        formLayout.setSpacing(true);
        formLayout.setPadding(false);

        Button absenden = new Button("Absenden", e -> {
            String fehler = validateForm(vorname.getValue(), nachname.getValue(),
                    telefonnummer.getValue(), mailadresse.getValue());

            if (fehler != null) {
                Notification.show(fehler);
                return; // Dialog bleibt offen, nichts wird "abgesendet"
            }

            InfoRequest request = new InfoRequest(
                    vorname.getValue(), nachname.getValue(),
                    strasse.getValue(), hausnummer.getValue(),
                    plz.getValue(), ort.getValue(), land.getValue(),
                    telefonnummer.getValue(), mailadresse.getValue(),
                    zusatzfragen.getValue()
            );
            infoRequestRepository.save(request);

            Notification.show("Vielen Dank, " + vorname.getValue() + "! Wir melden uns bald bei Ihnen.");
            dialog.close();
        });
        absenden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button abbrechen = new Button("Abbrechen", e -> dialog.close());

        dialog.add(formLayout);
        dialog.getFooter().add(abbrechen, absenden);

        dialog.open();
    }

    // ──────────────────────────────────────────────
    // Prüft die Formularfelder. Gibt eine Fehlermeldung
    // zurück, falls etwas ungültig ist - oder null,
    // wenn alles in Ordnung ist
    // ──────────────────────────────────────────────
    private String validateForm(String vorname, String nachname,
                                String telefonnummer, String mailadresse) {
        if (vorname == null || vorname.isBlank()) {
            return "Bitte einen Vornamen eingeben.";
        }
        if (nachname == null || nachname.isBlank()) {
            return "Bitte einen Nachnamen eingeben.";
        }
        if (mailadresse == null || !mailadresse.contains("@")) {
            return "Bitte eine gültige E-Mail-Adresse eingeben (muss @ enthalten).";
        }
        if (telefonnummer == null || !telefonnummer.matches("[0-9+\\-\\s]+")) {
            return "Telefonnummer darf nur Ziffern, +, - und Leerzeichen enthalten.";
        }
        return null; // alles gültig
    }

}