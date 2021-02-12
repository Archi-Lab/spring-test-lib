package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.RelationElement;
import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;


public class UmlRelationParser implements ElementParser<UmletElement> {

    @Override
    public RelationElement parseElement(UmletElement sourceElement) {
        String[] split = sourceElement.getAdditionalAttributes().split(";");
        Point start = new Point(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
        Point end = new Point(Float.parseFloat(split[split.length-2]), Float.parseFloat(split[split.length-1]));

        Point origin = new Point(sourceElement.getUmletCoordinates().getX(), sourceElement.getUmletCoordinates().getY());
        start.add(origin);
        end.add(origin);

        return new RelationElement(start, end);
    }
}
