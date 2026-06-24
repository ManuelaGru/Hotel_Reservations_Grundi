package at.spengergasse.views.rooms;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Rooms")
@Route("Rooms")
@Menu(order = 3, icon = LineAwesomeIconUrl.COUCH_SOLID)
public class RoomsView extends VerticalLayout {

    private record RoomCategory(String name, String beschreibung) {
    }

    private H3 detailTitle;
    private Paragraph detailText;

    public RoomsView() {
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle()
                .set("text-align", "center")
                .set("font-size", "16px")
                .set("background-image", "url('images/Strandaussicht.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-attachment", "fixed")
                .set("padding", "2rem 0");

        H2 header = new H2("Unsere Zimmer & Unterkünfte");
        header.getStyle().set("color", "white").set("text-shadow", "0 0 8px black");

        Image logo = new Image("images/LOGO.jpg", "Hotel Logo");
        logo.setHeight("100px");
        logo.getStyle()
                .set("border-radius", "50%")
                .set("border", "3px solid white")
                .set("box-shadow", "0 0 10px rgba(0,0,0,0.5)")
                .set("margin", "1rem 0");

        List<RoomCategory> categories = List.of(
                new RoomCategory("Hotelzimmer",
                        "Kleine Zimmer bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                                "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                                "werden kostenlos gestellt. Hotelzimmer unterliegen den Vorgaben " +
                                "des jeweiligen Hotels und Landes und können nur bei direkter " +
                                "Anfrage näher aufgezeigt werden."),
                new RoomCategory("Ferienwohnungen",
                        "Kleine Wohnungen bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                                "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                                "werden kostenlos gestellt."),
                new RoomCategory("Villen",
                        "Kleine Villen bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                                "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                                "werden kostenlos gestellt. Villen können zusätzlich auf Anfrage " +
                                "individuell zusammengestellt werden."),
                new RoomCategory("Weitere Optionen",
                        "Sprechen Sie uns gerne auf individuelle Wünsche und " +
                                "Sonderkonditionen an.")
        );

        // ── Schmale Tabelle: nur Kategorie-Namen ──
        Grid<RoomCategory> grid = new Grid<>(RoomCategory.class, false);
        grid.addColumn(RoomCategory::name)
                .setHeader("Kategorie")
                .setSortable(true);
        grid.setItems(categories);
        grid.setWidth("260px");
        grid.setHeight("220px");
        grid.getStyle()
                .set("background-color", "#FFF9C4")
                .set("border", "3px solid #FF9800")
                .set("border-radius", "8px")
                .set("box-shadow", "0 0 12px 3px #FFB74D");

        // ── Zeilen einfärben: per JavaScript anhand des Textinhalts ──
        // (umgeht setClassNameGenerator, das in dieser Vaadin-Version
        // nicht gefunden wurde)
        grid.getElement().executeJs(
                "const colorRow = (cell) => {" +
                        "  const text = cell.textContent.trim();" +
                        "  if (text === 'Hotelzimmer') cell.style.backgroundColor = '#FFF9C4';" +
                        "  else if (text === 'Ferienwohnungen') cell.style.backgroundColor = '#C8E6C9';" +
                        "  else if (text === 'Villen') cell.style.backgroundColor = '#FFE0B2';" +
                        "};" +
                        "const grid = this;" +
                        "const applyColors = () => {" +
                        "  grid.shadowRoot.querySelectorAll('vaadin-grid-cell-content').forEach(colorRow);" +
                        "};" +
                        "setTimeout(applyColors, 200);" +
                        "grid.addEventListener('grid-data-source-changed', () => setTimeout(applyColors, 100));"
        );

        // Klick auf eine Zeile zeigt deren Text rechts in der Box
        grid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresent(selected -> {
                detailTitle.setText(selected.name());
                detailText.setText(selected.beschreibung());
            });
        });

        // ── Detail-Box rechts neben der Tabelle ──
        detailTitle = new H3("Hotelzimmer");
        detailTitle.getStyle().set("margin-top", "0");

        detailText = new Paragraph(categories.get(0).beschreibung());
        detailText.getStyle().set("text-align", "left").set("margin", "0");

        Div detailBox = new Div(detailTitle, detailText);
        detailBox.getStyle()
                .set("background-color", "white")
                .set("border-radius", "8px")
                .set("padding", "1.5rem")
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.2)")
                .set("width", "420px")
                .set("height", "220px")
                .set("box-sizing", "border-box")
                .set("text-align", "left")
                .set("overflow-y", "auto");

        HorizontalLayout tableAndDetail = new HorizontalLayout(grid, detailBox);
        tableAndDetail.setAlignItems(Alignment.START);
        tableAndDetail.getStyle().set("gap", "1.5rem").set("margin", "1rem 0");

        grid.select(categories.get(0));

        // ── Die zwei Hinweis-Absätze, mit rotem 1mm-Rand ──
        Div textBackground = new Div();
        textBackground.getStyle()
                .set("background-color", "rgba(255,255,255,0.85)")
                .set("border-radius", "8px")
                .set("padding", "1.5rem")
                .set("width", "700px")
                .set("box-sizing", "border-box")
                .set("margin-top", "1rem")
                .set("border", "1mm solid red");

        Paragraph equipmentNote = new Paragraph(
                "Die Ausstattung der Räume richtet sich nach dem jeweiligen Hotel " +
                        "bzw. dem konkreten Angebot. Aus Sicherheitsgründen werden detaillierte " +
                        "Informationen dazu nicht öffentlich herausgegeben.");
        equipmentNote.getStyle().set("font-style", "italic");

        Paragraph combinedTripNote = new Paragraph(
                "Zusammengesetzte Reisen über mehrere Länder mit Übernachtungen an " +
                        "jeder Station sind möglich. Villen können nur ab einer Woche " +
                        "Aufenthalt gebucht werden. Es wird eine Kaution sowie ein " +
                        "Vorvertrag vorausgesetzt.");
        combinedTripNote.getStyle().set("font-weight", "bold");

        textBackground.add(equipmentNote, combinedTripNote);

        add(header, logo, tableAndDetail, textBackground);
    }

}