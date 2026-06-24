package at.spengergasse.views.airbnb;

import at.spengergasse.domain.Listing;
import at.spengergasse.repository.ListingRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Airbnb")
@Route("Airbnb")
@Menu(order = 4, icon = LineAwesomeIconUrl.AIRBNB)
public class AirbnbView extends VerticalLayout {

    private final ListingRepository listingRepository;
    private FlexLayout grid;

    public AirbnbView(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;

        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        H2 header = new H2("Entdecke einzigartige Unterkünfte");
        header.getStyle()
                .set("font-size", "26px")
                .set("font-weight", "bold")
                .set("font-style", "italic")
                .set("color", "#2E8B57")
                .set("margin-bottom", "0.3rem");

        Paragraph sub = new Paragraph("Handverlesene Wohnungen, Hütten und Villen – passend zu deiner Reise.");
        sub.getStyle().set("margin-top", "0").set("margin-bottom", "1rem");

        Button addButton = new Button("Neues Inserat", new Icon(VaadinIcon.PLUS), e -> addEditListing(null));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.getStyle().set("margin-bottom", "1.5rem");

        add(header, sub, addButton);

        seedSampleDataIfEmpty();

        grid = new FlexLayout();
        grid.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "1.5rem")
                .set("justify-content", "center")
                .set("max-width", "1100px");

        add(grid);

        refreshGrid();
    }

    // ──────────────────────────────────────────────
    // Beim allerersten Start ist die Tabelle leer ->
    // einmalig die 6 ursprünglichen Fake-Inserate anlegen,
    // damit die Seite nicht leer aussieht
    // ──────────────────────────────────────────────
    private void seedSampleDataIfEmpty() {
        if (listingRepository.count() > 0) {
            return;
        }
        List<Listing> startListings = List.of(
                new Listing("https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600",
                        "Gemütliches Loft Apartment", "Wien, Österreich", 89, 4.8),
                new Listing("https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=600",
                        "Modernes Studio mit Skyline", "Berlin, Deutschland", 75, 4.6),
                new Listing("https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=600",
                        "Holzhütte im Wald", "Tirol, Österreich", 110, 4.9),
                new Listing("https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?w=600",
                        "Helle Designerwohnung", "Salzburg, Österreich", 95, 4.7),
                new Listing("https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=600",
                        "Villa mit Pool", "Côte d'Azur, Frankreich", 280, 5.0),
                new Listing("https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=600",
                        "Minimalistisches Apartment", "Zürich, Schweiz", 130, 4.5)
        );
        listingRepository.saveAll(startListings);
    }

    private void refreshGrid() {
        grid.removeAll();
        for (Listing listing : listingRepository.findAll()) {
            grid.add(createCard(listing));
        }
    }

    // ──────────────────────────────────────────────
    // EINE Methode für Neu anlegen UND Bearbeiten.
    // existingListing == null  ->  neues, leeres Listing wird erzeugt
    // existingListing != null  ->  bestehendes Listing wird befüllt angezeigt
    // ──────────────────────────────────────────────
    private void addEditListing(Listing existingListing) {
        Listing listing;
        Dialog dialog = new Dialog();
        dialog.setWidth("450px");

        if (existingListing == null) {
            dialog.setHeaderTitle("Neues Inserat");
            listing = new Listing("", "", "", 0, 0.0);
        } else {
            dialog.setHeaderTitle("Inserat bearbeiten");
            listing = existingListing;
        }

        TextField imageUrl = new TextField("Bild-URL");
        imageUrl.setWidthFull();

        TextField title = new TextField("Titel");
        title.setWidthFull();

        TextField location = new TextField("Standort");
        location.setWidthFull();

        NumberField pricePerNight = new NumberField("Preis pro Nacht (€)");
        pricePerNight.setWidthFull();

        NumberField rating = new NumberField("Bewertung (0–5)");
        rating.setWidthFull();

        Binder<Listing> binder = new Binder<>(Listing.class);
        binder.forField(imageUrl).bind(Listing::getImageUrl, Listing::setImageUrl);
        binder.forField(title).bind(Listing::getTitle, Listing::setTitle);
        binder.forField(location).bind(Listing::getLocation, Listing::setLocation);
        binder.forField(pricePerNight)
                .withConverter(Double::intValue, Integer::doubleValue)
                .bind(Listing::getPricePerNight, Listing::setPricePerNight);
        binder.forField(rating).bind(Listing::getRating, Listing::setRating);

        binder.setBean(listing);

        VerticalLayout formLayout = new VerticalLayout(imageUrl, title, location, pricePerNight, rating);
        formLayout.setSpacing(true);
        formLayout.setPadding(false);

        Button speichern = new Button("Speichern", e -> {
            if (binder.validate().isOk()) {
                listingRepository.save(listing);
                refreshGrid();
                Notification.show("Inserat \"" + listing.getTitle() + "\" wurde gespeichert.");
                dialog.close();
            } else {
                Notification.show("Bitte überprüfe deine Eingaben.");
            }
        });
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button abbrechen = new Button("Abbrechen", e -> dialog.close());

        dialog.add(formLayout);
        dialog.getFooter().add(abbrechen, speichern);
        dialog.open();
    }

    private void deleteListing(Listing listing) {
        listingRepository.delete(listing);
        refreshGrid();
        Notification.show("Inserat \"" + listing.getTitle() + "\" wurde gelöscht.");
    }

    // ──────────────────────────────────────────────
    // Baut eine einzelne Inserat-Karte (VerticalLayout)
    // ──────────────────────────────────────────────
    private VerticalLayout createCard(Listing listing) {
        VerticalLayout card = new VerticalLayout();
        card.setSpacing(false);
        card.setPadding(false);
        card.setWidth("260px");
        card.getStyle()
                .set("border-radius", "12px")
                .set("overflow", "hidden")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)")
                .set("background", "white")
                .set("text-align", "left")
                .set("transition", "transform 0.2s")
                .set("cursor", "pointer")
                .set("flex-shrink", "0");

        card.getElement().executeJs(
                "this.addEventListener('mouseenter', () => this.style.transform='translateY(-4px)');" +
                        "this.addEventListener('mouseleave', () => this.style.transform='translateY(0)');"
        );

        Image img = new Image(listing.getImageUrl(), listing.getTitle());
        img.setWidth("100%");
        img.setHeight("170px");
        img.getStyle().set("object-fit", "cover").set("display", "block");

        H3 title = new H3(listing.getTitle());
        title.getStyle()
                .set("margin", "0.6rem 0.8rem 0.2rem")
                .set("font-size", "16px");

        Paragraph location = new Paragraph(listing.getLocation());
        location.getStyle()
                .set("margin", "0 0.8rem")
                .set("color", "#666")
                .set("font-size", "14px");

        Span rating = new Span("★ " + listing.getRating());
        rating.getStyle().set("color", "#2E8B57").set("font-weight", "bold").set("font-size", "14px");

        Span price = new Span(listing.getPricePerNight() + " € / Nacht");
        price.getStyle().set("font-weight", "bold").set("font-size", "14px");

        HorizontalLayout footer = new HorizontalLayout(price, rating);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        footer.setPadding(false);
        footer.getStyle().set("margin", "0.5rem 0.8rem 0.8rem");

        // ── Admin-Aktionen: Bearbeiten + Löschen ──
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editButton.addClickListener(e -> addEditListing(listing));

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        deleteButton.addClickListener(e -> deleteListing(listing));

        HorizontalLayout adminRow = new HorizontalLayout(editButton, deleteButton);
        adminRow.setSpacing(false);
        adminRow.getStyle().set("margin", "0 0.4rem 0.4rem");

        card.add(img, title, location, footer, adminRow);
        return card;
    }

}