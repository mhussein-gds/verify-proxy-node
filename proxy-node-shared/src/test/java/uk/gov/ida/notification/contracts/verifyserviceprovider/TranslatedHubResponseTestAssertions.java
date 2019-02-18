package uk.gov.ida.notification.contracts.verifyserviceprovider;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TranslatedHubResponseTestAssertions {

    public static void assertAttributes(Response decryptedEidasResponse) {
        org.opensaml.saml.saml2.core.Attribute attribute;
        Assertion eidasAssertion = decryptedEidasResponse.getAssertions().get(0);
        List<Attribute> attributes = eidasAssertion.getAttributeStatements().get(0).getAttributes();

        assertEquals(4, attributes.size());

        attribute = attributes.get(0);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName", attribute.getName());
        assertEquals("FirstName", attribute.getFriendlyName());
        assertEquals("Jean Paul", getFirstAttributeValueTextContent(attribute));

        attribute = attributes.get(1);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName", attribute.getName());
        assertEquals("FamilyName", attribute.getFriendlyName());
        assertEquals("Smith", getFirstAttributeValueTextContent(attribute));

        attribute = attributes.get(2);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/DateOfBirth", attribute.getName());
        assertEquals("DateOfBirth", attribute.getFriendlyName());
        assertEquals("1990-01-01", getFirstAttributeValueTextContent(attribute));

        attribute = attributes.get(3);
        assertEquals("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier", attribute.getName());
        assertEquals("PersonIdentifier", attribute.getFriendlyName());
        assertEquals("UK/EU/123456", getFirstAttributeValueTextContent(attribute));
    }

    public static void assertAssertionStatements(Response decryptedEidasResponse) {

        Assertion eidasAssertion = decryptedEidasResponse.getAssertions().get(0);

        assertEquals(2, eidasAssertion.getStatements().size());
        assertEquals(1, eidasAssertion.getAttributeStatements().size());
        assertEquals(1, eidasAssertion.getAuthnStatements().size());
    }

    private static String getFirstAttributeValueTextContent(Attribute attribute) {
        return attribute.getAttributeValues().get(0).getDOM().getTextContent();
    }
}
