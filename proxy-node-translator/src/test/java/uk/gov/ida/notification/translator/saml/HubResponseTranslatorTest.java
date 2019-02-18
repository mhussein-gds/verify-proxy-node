package uk.gov.ida.notification.translator.saml;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.saml.saml2.core.Response;
import uk.gov.ida.notification.VerifySamlInitializer;
import uk.gov.ida.notification.contracts.HubResponseTranslatorRequest;
import uk.gov.ida.notification.contracts.verifyserviceprovider.TranslatedHubResponseBuilder;
import uk.gov.ida.notification.contracts.verifyserviceprovider.TranslatedHubResponseTestAssertions;
import uk.gov.ida.notification.exceptions.hubresponse.HubResponseTranslationException;
import uk.gov.ida.notification.saml.SamlParser;
import uk.gov.ida.saml.core.test.builders.ResponseBuilder;

import java.net.URI;

import static uk.gov.ida.saml.core.test.TestCertificateStrings.STUB_COUNTRY_PUBLIC_PRIMARY_CERT;

public class HubResponseTranslatorTest {

    static {
        try {
            InitializationService.initialize();
            VerifySamlInitializer.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void translateShouldReturnResponse() {

        HubResponseTranslator translator =
                new HubResponseTranslator("Issuer", "connectorMetadataURL");

        Response response =
            translator.translate(buildHubResponseContainer());

        TranslatedHubResponseTestAssertions.assertAssertionStatements(response);
        TranslatedHubResponseTestAssertions.assertAttributes(response);
    }

    @Test
    public void translateShouldReturnResponseCancellation() {

        HubResponseTranslator translator =
                new HubResponseTranslator("Issuer", "connectorMetadataURL");

        translator.translate(buildTranslatedHubResponseCancellation());
    }

    @Test
    public void translateShouldReturnIllegalArgumentExceptionWhenAttributesNull() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("HubResponseContainer Attributes null.");

        HubResponseTranslator translator =
                new HubResponseTranslator("Issuer", "connectorMetadataURL");

        Response response =
                translator.translate(buildHubResponseContainerWithNoAttributes());
    }

    @Test
    public void translateShouldReturnIllegalArgumentExceptionWhenRequiredAttributeNull() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("HubResponseContainer Attribute Surnames null.");

        HubResponseTranslator translator =
                new HubResponseTranslator("Issuer", "connectorMetadataURL");

        translator.translate(buildHubResponseContainerWithOnlyOneAttribute());
    }

    @Test
    public void translateShouldReturnExceptionWhenRequestError() {

        expectedException.expect(HubResponseTranslationException.class);
        expectedException.expectMessage("Received error status from VSP: ");

        HubResponseTranslator translator =
                new HubResponseTranslator("Issuer", "connectorMetadataURL");

        translator.translate(buildTranslatedHubResponseRequestError());
    }

    private HubResponseContainer buildHubResponseContainerWithNoAttributes() {
        return new HubResponseContainer(
                new HubResponseTranslatorRequest(),
                TranslatedHubResponseBuilder.buildTranslatedHubResponseIdentityVerifiedNoAttributes()
        );
    }

    private HubResponseContainer buildHubResponseContainerWithOnlyOneAttribute() {
        return new HubResponseContainer(
                new HubResponseTranslatorRequest(),
                TranslatedHubResponseBuilder.buildTranslatedHubResponseOneAttributeOnly()
        );
    }

    private HubResponseContainer buildHubResponseContainer() {
        return new HubResponseContainer(buildHubResponseTranslatorRequest(), TranslatedHubResponseBuilder.buildTranslatedHubResponseIdentityVerified());
    }

    private HubResponseContainer buildTranslatedHubResponseCancellation() {
        return new HubResponseContainer(buildHubResponseTranslatorRequest(), TranslatedHubResponseBuilder.buildTranslatedHubResponseCancellation());
    }

    private HubResponseContainer buildTranslatedHubResponseRequestError() {
        return new HubResponseContainer(buildHubResponseTranslatorRequest(), TranslatedHubResponseBuilder.buildTranslatedHubResponseRequestError());
    }

    private HubResponseTranslatorRequest buildHubResponseTranslatorRequest() {
        return new HubResponseTranslatorRequest(
                "",
                "_1234",
                ResponseBuilder.DEFAULT_REQUEST_ID,
                "LEVEL_2",
                URI.create("http://localhost:8081/bob"),
                STUB_COUNTRY_PUBLIC_PRIMARY_CERT);
    }
}
