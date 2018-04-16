import org.junit.jupiter.api.Test;
import org.reso.upi.MalformedUpiTextException;
import org.reso.upi.Upi;
import org.reso.upi.UpiInterface;
import org.reso.upi.country_code.CountryCode;
import org.reso.upi.property_type_code.PropertyTypeCode;
import org.reso.upi.validation.ValidationMessage;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UpiTest {
    private String[] goodUpis = {
            "US-04015-N-11022331-R-N",
            "US-42049-49888-1213666-R-N",
            "US-36061-N-010237502R1-R-N",
            "US-36061-N-010237502R1-S-113",
            "US-06075-N-40010333-T-10",
            "US-13051-N-1122444-R-N",
            "US-36061-N-0122213-S-118",
    };

    private String[] incompleteUpis = {
            "US-123331-N-87-99",
            "OIOASPODASDO APOSAPSCAS",
    };

    private String[][] badValueUpis = {
            // Both Bad
            {
                    "XX-123331-N-N-99798987-99",
                    new ValidationMessage(400, "The Country Code is not recognized.").toString(),
                    new ValidationMessage(410, "The Property Type Code is non standard").toString()
            },

            // Non Standard Property Type
            {
                    "US-04019-N-12401001H-B-65A",
                    new ValidationMessage(410, "The Property Type Code is non standard").toString()

            },

            // Bad Country Code
            {
                    "GX-04019-N-12401001H-R-65A",
                    new ValidationMessage(400, "The Country Code is not recognized.").toString(),

            },
    };

    /* Full Integration Script (from README) */
    @Test
    void givenUpi() throws MalformedUpiTextException {
        UpiInterface upi = new Upi("US-04015-N-11022331-B-13");

        assertEquals("US", upi.getCountryCode().toString());
        assertEquals("04015", upi.getSubCountryCode());
        assertEquals("N", upi.getSubCountyCode());
        assertEquals("11022331", upi.getPropertyId());
        assertEquals("B", upi.getPropertyTypeCode().toString());
        assertEquals("13", upi.getSubProperty());

        // You can also add an arbitrary description
        upi.setDescription("This is some cool description");
        assertEquals("This is some cool description", upi.getDescription());

        // Or the full UPI Code again
        assertEquals("US-04015-N-11022331-B-13", upi.getRawUpiText());

        // Loose validation
        ArrayList<ValidationMessage> errors = upi.validate();

        assertTrue(errors.size() == 0);

        ArrayList<ValidationMessage> strictErrors = upi.validate(true);

        /* This should be one error */
        assertTrue(strictErrors.size() == 1);
        assertEquals(strictErrors.get(0).getCode(), 410);

        assertTrue(upi.isValid());

        // this should be false
        assertFalse(upi.isValid(true));
    }

    /* Parsing Tests (no validation) */
    @Test
    void parseGoodUpi() throws MalformedUpiTextException {

        for (String upiText : this.goodUpis) {
            Upi upi = new Upi();
            upi.parseUpi(upiText);

            String[] upiPieces = upiText.split("-");

            assertEquals(upiText, upi.getRawUpiText(), "Failed to set `upiText` for `" + upiText + "`");

            assertEquals(upiPieces[0], upi.getCountryCode().toString(), "Failed to set `countryCode` for `" + upiText + "`");
            assertEquals(upiPieces[1], upi.getSubCountryCode(), "Failed to set `SubCountryCode` for `" + upiText + "`");
            assertEquals(upiPieces[2], upi.getSubCountyCode(), "Failed to set `SubCountyCode` for `" + upiText + "`");
            assertEquals(upiPieces[3], upi.getPropertyId(), "Failed to set `PropertyId` for `" + upiText + "`");
            assertEquals(upiPieces[4], upi.getPropertyTypeCode().toString(), "Failed to set `org.reso.upi.property_type_code.PropertyTypeCode` for `" + upiText + "`");
            assertEquals(upiPieces[5], upi.getSubProperty(), "Failed to set `SubProperty` for `" + upiText + "`");
        }
    }

    @Test
    void parseGoodUpiWithDescription() throws MalformedUpiTextException {
        Upi upi = new Upi();
        String upiText = this.goodUpis[0];
        String description = "My description goes here";

        upi.parseUpi(upiText, description);

        // @Todo: duplicated
        String[] upiPieces = upiText.split("-");

        assertEquals(upiText, upi.getRawUpiText(), "Failed to set `upiText` for `" + upiText + "`");

        assertEquals(upiPieces[0], upi.getCountryCode().toString(), "Failed to set `countryCode` for `" + upiText + "`");
        assertEquals(upiPieces[1], upi.getSubCountryCode(), "Failed to set `SubCountryCode` for `" + upiText + "`");
        assertEquals(upiPieces[2], upi.getSubCountyCode(), "Failed to set `SubCountyCode` for `" + upiText + "`");
        assertEquals(upiPieces[3], upi.getPropertyId(), "Failed to set `PropertyId` for `" + upiText + "`");
        assertEquals(upiPieces[4], upi.getPropertyTypeCode().toString(), "Failed to set `org.reso.upi.property_type_code.PropertyTypeCode` for `" + upiText + "`");
        assertEquals(upiPieces[5], upi.getSubProperty(), "Failed to set `SubProperty` for `" + upiText + "`");
        // @todo: end duplicated

        assertEquals(description, upi.getDescription());
    }

    @Test()
    void constructsWithGoodUpi() throws MalformedUpiTextException {
        String upiText = this.goodUpis[3];
        Upi upi = new Upi(upiText);

        String[] upiPieces = upiText.split("-");

//            assertEquals(true, upi.isParsed(), "Failed to set `parseUpid` for `" + upiText + "`");
        assertEquals(upiText, upi.getRawUpiText(), "Failed to set `upiText` for `" + upiText + "`");

        assertEquals(upiPieces[0], upi.getCountryCode().toString(), "Failed to set `countryCode` for `" + upiText + "`");
        assertEquals(upiPieces[1], upi.getSubCountryCode(), "Failed to set `SubCountryCode` for `" + upiText + "`");
        assertEquals(upiPieces[2], upi.getSubCountyCode(), "Failed to set `SubCountyCode` for `" + upiText + "`");
        assertEquals(upiPieces[3], upi.getPropertyId(), "Failed to set `PropertyId` for `" + upiText + "`");
        assertEquals(upiPieces[4], upi.getPropertyTypeCode().toString(), "Failed to set `org.reso.upi.property_type_code.PropertyTypeCode` for `" + upiText + "`");
        assertEquals(upiPieces[5], upi.getSubProperty(), "Failed to set `SubProperty` for `" + upiText + "`");
    }

    @Test
    void throwsExceptionWhenFailsToParseWithMalformedUpi() {
        String upiText = this.incompleteUpis[0];

        boolean exceptionThrown = false;
        try {
            Upi upi = new Upi();
            upi.parseUpi(upiText);

        } catch (MalformedUpiTextException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown, "Failed to throw an exception");
    }

    @Test
    void failsToConstructWithMalformedUpi() {
        String upiText = this.incompleteUpis[0];

        boolean exceptionThrown = false;
        try {
            new Upi(upiText);

        } catch (MalformedUpiTextException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown, "Failed to throw an exception");
    }


    /* Enumeration Value Tests */
    // @todo: these are just sugar tests, move them to more extensive class tests
    @Test
    void setCountryNameFromGoodCountryCode() {
        Upi upi = new Upi();
        upi.setCountryCode(CountryCode.FI);

        assertEquals(CountryCode.FI, upi.getCountryCode());
        assertEquals(CountryCode.FI.getName(), upi.getCountryName());
    }

    @Test
    void setsBothFromGoodStringCountryCode() {
        Upi upi = new Upi();
        upi.setCountryCode("fi"); // lowercase to try and throw it off

        assertEquals(CountryCode.FI, upi.getCountryCode());
        assertEquals(CountryCode.FI.getName(), upi.getCountryName());
    }

    @Test
    void setsUndefinedWhenCountryCodeNotFound() {
        Upi upi = new Upi();
        upi.setCountryCode("xX"); // mixed case to try and throw it off

        assertEquals(CountryCode.UNDEFINED, upi.getCountryCode());
        assertEquals(CountryCode.UNDEFINED.getName(), upi.getCountryName());
    }


    @Test
    void setsNonStandardPropertyTypeWhenNotFound() {
        Upi upi = new Upi();
        upi.setPropertyTypeCode("xX");

        assertEquals(PropertyTypeCode.NON_STANDARD, upi.getPropertyTypeCode());
        assertEquals(PropertyTypeCode.NON_STANDARD.getCode(), "xX");
    }


    /* Validation Tests */
    @Test
    void testValidatesGoodCodesNonStrict() throws MalformedUpiTextException {
        for (String upiText : this.goodUpis) {
            Upi upi = new Upi(upiText);

            assertTrue(upi.isValid());
            assertTrue(upi.getValidationMessages().size() == 0);
        }
    }

    /* Validation Tests */
    @Test
    void testValidatesGoodCodesStrict() throws MalformedUpiTextException {
        for (String upiText : this.goodUpis) {
            Upi upi = new Upi(upiText);

            assertTrue(upi.isValid(true));
            assertTrue(upi.getValidationMessages().size() == 0);
        }
    }

    @Test
    void testDoesNotValidateCompleteCodesWithBadEnumsWhenStrict() throws MalformedUpiTextException {
        for (String[] testCase : this.badValueUpis) {
            Upi upi = new Upi(testCase[0]);

            assertFalse(upi.isValid(true));
            assertTrue(upi.getValidationMessages().size() == testCase.length - 1, "");

            // Now, make sure the right messages were produced
            for (int i = 0; i < upi.getValidationMessages().size(); i++) {
                String message = testCase[i + 1];
                assertEquals(message, upi.getValidationMessages().get(i).toString());
            }
        }
    }

    @Test
    void toUpiWithGoodUpis() throws MalformedUpiTextException {
        for (String upiText : this.goodUpis) {
            Upi upi = new Upi(upiText);
            assertEquals(upiText, upi.toUpi());
        }
    }

    @Test
    void failsToUpiWithIncompleteUpis() {
        Upi upi = new Upi(); // empty
        upi.setCountryCode(CountryCode.UK); // set one for kicks

        boolean exceptionThrown = false;

        try {
            upi.toUpi();
        } catch (MalformedUpiTextException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }
}
