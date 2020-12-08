package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private List<String> contents = new ArrayList<>();


    public void addContent(String newContent) {
        contents.add(newContent);
    }

    public void addContent(Cell newContent) {
        contents.addAll(newContent.contents);
    }

    public boolean containsContent(String content) {
        for (String testContent : contents) {
            if (testContent.equalsIgnoreCase(content)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cell)) {
            return false;
        }

        Cell otherCell = (Cell)obj;

        if (contents.size() != otherCell.contents.size()) {
            return false;
        }

        for (String testContent : otherCell.contents) {
            if (!containsContent(testContent)) {
                return false;
            }
        }
        return true;
    }

    public static Cell parseCell(String content) {
        Cell newCell = new Cell();

        String[] split = content.split(",");
        for (String s : split) {
            s = s.trim();

            if (!s.isEmpty()) {
                newCell.addContent(s);
            }
        }

        return newCell;
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }
}
