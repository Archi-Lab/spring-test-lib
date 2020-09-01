package thkoeln.st.springtestlib.core;

import java.util.List;
import java.util.UUID;

/**
 * This class represents an http link which follows the principles of REST maturity level 3
 */
public class Link {

    private static final String ID_PLACEHOLDER = "{id}";

    private final String relation;
    private final String link;
    private String customizedLink;


    public Link(String relation, String link) {
        this.relation = relation;
        this.link = link;
    }

    /**
     * Replaces all placeholders for ids in the link with the given ids
     * @param ids sorted list of the ids which should replace the placeholder
     */
    public void calculateCustomizedLink(List<UUID> ids) {
        String[] splitLink = link.split("/");

        int idNr = 0;
        customizedLink = "";
        for (String linkPart : splitLink) {
            if (linkPart.equals(ID_PLACEHOLDER)) {
                linkPart = idNr < ids.size() ? ids.get(idNr++).toString() : linkPart;
            }

            customizedLink += linkPart + "/";
        }
    }

    public boolean equals(String link) {
        String[] customizedLinkSplit = customizedLink.split("/");
        String[] testLinkSplit = link.split("/");

        if (customizedLinkSplit.length != testLinkSplit.length) {
            return false;
        }

        for (int i = 0; i < customizedLinkSplit.length; i++) {
            String customizedLinkPart = customizedLinkSplit[i];
            String testLinkPart = testLinkSplit[i];

            if (!customizedLinkPart.equals(ID_PLACEHOLDER) && !customizedLinkPart.equals(testLinkPart)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the protocol and domain from a link
     * @param link link containing protocol and domain
     * @return link without protocol and domain
     */
    public static String getDomainLessLink(String link) {
        link = link.trim();

        // Remove Protocol and Domain
        String[] splitLink = link.split("/");
        String newLink = "";
        for (int i = 3; i < splitLink.length; i++) {
            newLink += splitLink[i] + "/";
        }

        return newLink;
    }

    public String getRelation() {
        return relation;
    }

    public String getLink() {
        return link;
    }

    public String getCustomizedLink() {
        return customizedLink;
    }
}
