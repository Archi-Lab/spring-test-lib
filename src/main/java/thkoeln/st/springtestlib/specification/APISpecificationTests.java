package thkoeln.st.springtestlib.specification;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test wheather the api is specified correctly in yaml format
 */
public class APISpecificationTests {

    private class APISpecificationEntry {

        private String link;
        private String httpVerb;


        public APISpecificationEntry(String link, String httpVerb) {
            this.link = link;
            this.httpVerb = httpVerb;
        }
    }

    private LinkedHashMap<String, APISpecificationEntry> loadAPISpecification(String path) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
        Map<String, Map> obj = yaml.load(inputStream);

        LinkedHashMap<String, APISpecificationEntry> apiSpecificationEntries = new LinkedHashMap<>();
        for (Map.Entry<String, Map> entry : obj.entrySet()) {
            String uri = (String)(entry.getValue().get("URI"));
            String httpVerb = (String)(entry.getValue().get("HTTPVerb"));

            APISpecificationEntry apiSpecificationEntry = new APISpecificationEntry(uri, httpVerb);
            apiSpecificationEntries.put(entry.getKey(), apiSpecificationEntry);
        }

        return apiSpecificationEntries;
    }

    public void testAPISpecification(String expectedPath, String actualPath) throws Exception {
        LinkedHashMap<String, APISpecificationEntry> actualApiSpecificationEntries = loadAPISpecification(actualPath);
        LinkedHashMap<String, APISpecificationEntry> expectedApiSpecificationEntries = loadAPISpecification(expectedPath);

        String errorMessage = "\n";
        boolean failed = false;

        for (Map.Entry<String, APISpecificationEntry> expectedEntry : expectedApiSpecificationEntries.entrySet()) {
            APISpecificationEntry expected = expectedEntry.getValue();
            APISpecificationEntry actual = actualApiSpecificationEntries.get(expectedEntry.getKey());

            if (assertVerb(expected.httpVerb, actual.httpVerb) && assertLink(expected.link, actual.link)) {
                errorMessage += "Correct: " + expectedEntry.getKey() + "\n";
            } else {
                errorMessage += "Incorrect: " + expectedEntry.getKey() + "\n";
                failed = true;
            }
        }

        if (failed) {
            throw new Exception(errorMessage);
        }
    }

    private boolean assertVerb(String expectedVerb, String actualVerb) {
        if (expectedVerb == null) {
            return true;
        } else if (actualVerb == null) {
            return false;
        }

        return expectedVerb.trim().equalsIgnoreCase(actualVerb.trim());
    }

    private boolean assertLink(String expectedLink, String actualLink) {
        if (expectedLink == null) {
            return true;
        } else if (actualLink == null) {
            return false;
        }

        String[] expectedLinkParts = expectedLink.trim().split("/");
        String[] actualLinkParts = actualLink.trim().split("/");

        if (expectedLinkParts.length != actualLinkParts.length) {
            return false;
        }

        for (int i = 0; i < expectedLinkParts.length; i++) {
            if (!expectedLinkParts[i].contains("{") && !expectedLinkParts[i].contains("}")) {
                if (!expectedLinkParts[i].equals(actualLinkParts[i])) {
                    return false;
                }
            }
        }

        return true;
    }
}
