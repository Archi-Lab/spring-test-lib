package thkoeln.st.springtestlib.specification.diagram;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class DiagramExceptionHelper {

    private boolean sumExceptions;
    private List<String> messages = new ArrayList<>();


    public DiagramExceptionHelper(boolean sumExceptions) {
        this.sumExceptions = sumExceptions;
    }

    public void throwException(String message) throws InputMismatchException {
        if (sumExceptions) {
            messages.add(message);
        } else {
            throw new InputMismatchException(message);
        }
    }

    public void throwSummarizedException() throws InputMismatchException {
        if (!messages.isEmpty()) {
            String errorMessage = "\n";
            for (int i = 0; i < messages.size(); i++) {
                errorMessage += messages.get(i);
                if (i < messages.size() - 1) {
                    errorMessage += "\n";
                }
            }
            throw new InputMismatchException(errorMessage);
        }
    }
}
