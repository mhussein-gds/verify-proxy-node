package uk.gov.ida.notification.saml.translation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.core.config.InitializationService;
import org.opensaml.saml.saml2.core.AuthnRequest;
import uk.gov.ida.notification.helpers.FileHelpers;
import uk.gov.ida.notification.saml.SamlMarshaller;
import uk.gov.ida.notification.saml.SamlParser;
import uk.gov.ida.saml.core.extensions.IdaAuthnContext;

import static org.junit.Assert.*;

public class EidasAuthnRequestTranslatorTest {

    @BeforeClass
    public static void setup() throws Exception {
        InitializationService.initialize();
    }

    @Test
    public void shouldBuildHubAuthnRequestWithSameId() throws Exception {
        SamlParser samlParser = new SamlParser();
        SamlMarshaller samlMarshaller = new SamlMarshaller();
        EidasAuthnRequestTranslator translator = new EidasAuthnRequestTranslator("http://proxy-node.uk", "http://verify-hub.uk", samlParser, samlMarshaller);
        String eidasAuthnRequestXml = FileHelpers.readFileAsString("eidas_authn_request.xml");

        String hubAuthnRequestXml = translator.translate(eidasAuthnRequestXml);

        AuthnRequest hubAuthnRequest = samlParser.parseSamlString(hubAuthnRequestXml, AuthnRequest.class);
        assertEquals("_171ccc6b39b1e8f6e762c2e4ee4ded3a", hubAuthnRequest.getID());
        assertEquals(IdaAuthnContext.LEVEL_2_AUTHN_CTX, getLoa(hubAuthnRequest));
    }

    private static String getLoa(AuthnRequest hubAuthnRequest) {
        return hubAuthnRequest.getRequestedAuthnContext().getAuthnContextClassRefs().get(0).getAuthnContextClassRef();
    }
}
