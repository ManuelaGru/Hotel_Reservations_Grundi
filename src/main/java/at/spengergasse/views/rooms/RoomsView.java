package at.spengergasse.views.rooms;

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

@PageTitle("Rooms")
@Route("Rooms")
@Menu(order = 3, icon = LineAwesomeIconUrl.COUCH_SOLID)
public class RoomsView extends VerticalLayout {

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

        Div hotelBox = createBox("Hotelzimmer",
                "Kleine Zimmer bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                        "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                        "werden kostenlos gestellt. Hotelzimmer unterliegen den Vorgaben " +
                        "des jeweiligen Hotels und Landes und können nur bei direkter " +
                        "Anfrage näher aufgezeigt werden.", "-15px");

        Div apartmentBox = createBox("Ferienwohnungen",
                "Kleine Wohnungen bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                        "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                        "werden kostenlos gestellt.", "15px");

        Div villaBox = createBox("Villen",
                "Kleine Villen bis 25 m² für 1 Person, 30–45 m² für 2 Personen, " +
                        "ab 60 m² für 3 oder mehr Personen. Kinderbetten bis 3 Jahre " +
                        "werden kostenlos gestellt. Villen können zusätzlich auf Anfrage " +
                        "individuell zusammengestellt werden.", "-15px");

        Div placeholderBox = createBox("Weitere Optionen",
                "Sprechen Sie uns gerne auf individuelle Wünsche und " +
                        "Sonderkonditionen an.", "15px");

        Image logo = new Image("images/LOGO.jpg", "Hotel Logo");
        logo.setHeight("100px");
        logo.getStyle()
                .set("border-radius", "50%")
                .set("border", "3px solid white")
                .set("box-shadow", "0 0 10px rgba(0,0,0,0.5)");

        HorizontalLayout topRow = new HorizontalLayout(hotelBox, logo, apartmentBox);
        topRow.setAlignItems(Alignment.CENTER);
        topRow.getStyle().set("margin-top", "2rem");

        HorizontalLayout bottomRow = new HorizontalLayout(villaBox, placeholderBox);
        bottomRow.setAlignItems(Alignment.START);

        Div textBackground = new Div();
        textBackground.getStyle()
                .set("background-color", "rgba(255,255,255,0.85)")
                .set("border-radius", "8px")
                .set("padding", "1.5rem")
                .set("width", "100%")
                .set("box-sizing", "border-box")
                .set("margin-top", "1.5rem");

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

        add(header, topRow, bottomRow, textBackground);
    }

    private Div createBox(String title, String text, String verticalOffset) {
        H3 boxHeader = new H3(title);
        Paragraph boxText = new Paragraph(text);

        Div box = new Div(boxHeader, boxText);
        box.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "8px")
                .set("padding", "1.5rem")
                .set("margin", "1rem")
                .set("max-width", "320px")
                .set("background-color", "rgba(255,255,255,0.9)")
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.2)")
                .set("transform", "translateY(" + verticalOffset + ")");

        return box;
    }

}