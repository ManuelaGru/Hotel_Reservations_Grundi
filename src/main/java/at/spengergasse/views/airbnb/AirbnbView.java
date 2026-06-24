package at.spengergasse.views.airbnb;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Airbnb")
@Route("Airbnb")
@Menu(order = 4, icon = LineAwesomeIconUrl.AIRBNB)
public class AirbnbView extends VerticalLayout {

    // ──────────────────────────────────────────────
    // Kleine Hilfsklasse für ein Fake-Inserat
    // (record = unveränderliche Datenklasse, spart Getter/Konstruktor-Code)
    // ──────────────────────────────────────────────
    private record Listing(String imageUrl, String title, String location, int pricePerNight, double rating) {
    }

    public AirbnbView() {
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
        sub.getStyle().set("margin-top", "0").set("margin-bottom", "1.5rem");

        add(header, sub);

        // ──────────────────────────────────────────────
        // Liste der Fake-Inserate (Bilder via Unsplash-Direktlinks)
        // ──────────────────────────────────────────────
        List<Listing> listings = List.of(
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

        // ──────────────────────────────────────────────
        // Äußeres Grid: bleibt FlexLayout, damit Karten
        // nebeneinander stehen und automatisch umbrechen.
        // ──────────────────────────────────────────────
        FlexLayout grid = new FlexLayout();
        grid.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "1.5rem")
                .set("justify-content", "center")
                .set("max-width", "1100px");

        for (Listing listing : listings) {
            grid.add(createCard(listing));
        }

        add(grid);
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

        // Hover-Effekt: Karte hebt sich leicht beim Drüberfahren
        card.getElement().executeJs(
                "this.addEventListener('mouseenter', () => this.style.transform='translateY(-4px)');" +
                        "this.addEventListener('mouseleave', () => this.style.transform='translateY(0)');"
        );

        Image img = new Image(listing.imageUrl(), listing.title());
        img.setWidth("100%");
        img.setHeight("170px");
        img.getStyle().set("object-fit", "cover").set("display", "block");

        H3 title = new H3(listing.title());
        title.getStyle()
                .set("margin", "0.6rem 0.8rem 0.2rem")
                .set("font-size", "16px");

        Paragraph location = new Paragraph(listing.location());
        location.getStyle()
                .set("margin", "0 0.8rem")
                .set("color", "#666")
                .set("font-size", "14px");

        Span rating = new Span("★ " + listing.rating());
        rating.getStyle().set("color", "#2E8B57").set("font-weight", "bold").set("font-size", "14px");

        Span price = new Span(listing.pricePerNight() + " € / Nacht");
        price.getStyle().set("font-weight", "bold").set("font-size", "14px");

        HorizontalLayout footer = new HorizontalLayout(price, rating);
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        footer.setPadding(false);
        footer.getStyle().set("margin", "0.5rem 0.8rem 0.8rem");

        card.add(img, title, location, footer);
        return card;
    }

}