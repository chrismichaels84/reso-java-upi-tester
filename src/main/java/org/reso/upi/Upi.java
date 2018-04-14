package org.reso.upi;

import org.reso.upi.country_code.CountryCode;
import org.reso.upi.property_type_code.PropertyTypeCode;
import org.reso.upi.validation.ValidationMessage;
import org.reso.upi.validation.ValidationRuleInterface;
import org.reso.upi.validation.rules.ResoAllPiecesRequiredRule;
import org.reso.upi.validation.rules.ResoValidCountryCodeRule;
import org.reso.upi.validation.rules.ResoValidPropertyTypeCodeRule;

import java.util.ArrayList;

public class Upi implements UpiInterface {

    private String upiText;
    private String description;
    private boolean valid;

    private ArrayList<ValidationMessage> validationMessages;
    private ArrayList<ValidationRuleInterface> validationRules;

    private String undefinedCountryCode;

    private CountryCode countryCode;
    private String subCountryCode;
    private String subCountyCode;
    private String propertyId;
    private PropertyTypeCode propertyTypeCode;
    private String subProperty;


    /* Constructors */

    /**
     * Creates an Empty UPI
     */
    public Upi() {
        ArrayList<ValidationRuleInterface> rules = new ArrayList<ValidationRuleInterface>();

        rules.add(new ResoAllPiecesRequiredRule());
        rules.add(new ResoValidCountryCodeRule());
        rules.add(new ResoValidPropertyTypeCodeRule());

        this.setValidationRules(rules);
    }

    /**
     * Parse a UPI on construction
     *
     * @param upiText
     */
    public Upi(String upiText) throws MalformedUpiTextException {
        this();
        this.parseUpi(upiText);
    }

    /**
     * Parse a UPI on construction
     *
     * @param upiText
     */
    public Upi(String upiText, String description) throws MalformedUpiTextException {
        this(upiText);
        this.setDescription(description);
    }

    /**
     * Give all the parameters, including description
     *
     * @param countryCode
     * @param subCountryCode
     * @param subCountyCode
     * @param propertyId
     * @param propertyTypeCode
     * @param subProperty
     * @param description
     */
    public Upi(
            CountryCode countryCode,
            String subCountryCode,
            String subCountyCode,
            String propertyId,
            PropertyTypeCode propertyTypeCode,
            String subProperty,
            String description
    ) {
        this();

        this.setCountryCode(countryCode);
        this.setSubCountryCode(subCountryCode);
        this.setSubCountyCode(subCountyCode);
        this.setPropertyId(propertyId);
        this.setPropertyTypeCode(propertyTypeCode);
        this.setSubProperty(subProperty);
        this.setDescription(description);
    }

    /**
     * Give all the parameters EXCEPT description
     *
     * @param countryCode
     * @param subCountryCode
     * @param subCountyCode
     * @param propertyId
     * @param propertyTypeCode
     * @param subProperty
     */
    public Upi(
            CountryCode countryCode,
            String subCountryCode,
            String subCountyCode,
            String propertyId,
            PropertyTypeCode propertyTypeCode,
            String subProperty
    ) {
        this(countryCode, subCountryCode, subCountyCode, propertyId, propertyTypeCode, subProperty, "");
    }


    /* Processing Methods */
    /**
     * Will build a UPI from the piece, if it has all the pieces.
     * Validates as non-strict. Just makes sure it has what it needs.
     *
     * @return
     * @throws MalformedUpiTextException
     */
    public String toUpi() throws MalformedUpiTextException {
        this.throwExceptionIfIncompleteUpi();

        // Now that we have all the right pieces, get the right Country Code
        String countryCode = (this.getCountryCode() == CountryCode.UNDEFINED)
                ? this.getUndefinedCountryCode()
                : this.getCountryCode().getAlpha2();

        // And build the string
        return String.valueOf(countryCode) +
                "-" +
                this.subCountryCode +
                "-" +
                this.subCountyCode +
                "-" +
                this.propertyId +
                "-" +
                this.propertyTypeCode +
                "-" +
                this.subProperty;
    }

    private void throwExceptionIfIncompleteUpi() throws MalformedUpiTextException {
        // First, we need all the pieces in order to create a UPI Text
        ResoAllPiecesRequiredRule rule = new ResoAllPiecesRequiredRule();
        ArrayList<ValidationMessage> errors = rule.validate(this, false); // this will give us any missing pieces

        if (errors.size() > 0) {
            throw new MalformedUpiTextException(errors);
        }
    }

    /**
     * Hydrates a UPI object with a description, if complete upi is given
     * <p>
     * If UPI is incomplete, returns false.
     *
     * @param upiText     Hopefully complete UPI `US-36061-N-010237502R1-S-113`
     * @param description Human readable description
     * @return true for success, false for failure
     */
    public void parseUpi(String upiText, String description) throws MalformedUpiTextException {
        this.parseUpi(upiText);
        this.setDescription(description);
    }

    /**
     * Hydrates a UPI object with no description, if complete upi is given
     * <p>
     * If UPI is incomplete, returns false.
     *
     * @param upiText Hopefully complete UPI `US-36061-N-010237502R1-S-113`
     * @return true for success, false for failure
     */
    public void parseUpi(String upiText) throws MalformedUpiTextException {
        String[] upiPieces = upiText.split("-");

        // If we weren't given a full UPI, it can't be valid
        if (upiPieces.length < 6) {
            throw new MalformedUpiTextException(new ValidationMessage(500, "The given UPI is missing pieces"));
        }

        // Set the text whether its valid or not
        this.setUpiText(upiText);

        this.setCountryCode(upiPieces[0]); // The Country’s ALPHA-2 code, as published under ISO 3166.
        this.setSubCountryCode(upiPieces[1]); // // eg FIPS code or Int'l equivalent
        this.setSubCountyCode(upiPieces[2]); // A string defined and maintained by the subcountry region

        // @todo: these are switched in tims
        this.setPropertyTypeCode(upiPieces[4]); // PropertyType Code defined for any sub property type. These types are available in the UPI Specification.
        this.setPropertyId(upiPieces[3]); // Assigned from tax authority

        this.setSubProperty(upiPieces[5]);
    }

    public ArrayList<ValidationMessage> validate() {
        return this.validate(false);
    }

    public ArrayList<ValidationMessage> validate(boolean strict) {
        return this.validate(strict, this.getValidationRules());
    }

    public ArrayList<ValidationMessage> validate(boolean strict, ArrayList<ValidationRuleInterface> ruleSet) {
        ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();

        for (ValidationRuleInterface rule : ruleSet) {
            ArrayList<ValidationMessage> ruleMessages = rule.validate(this, strict);
            errors.addAll(ruleMessages);
        }


        this.setValid(errors.size() <= 0);
        this.setValidationMessages(errors);

        return errors;
    }


    /* Special Getter */
    public boolean isValid(boolean strict, ArrayList<ValidationRuleInterface> ruleSet) {
        this.validate(strict, ruleSet);
        return this.valid;
    }

    public boolean isValid(boolean strict) {
        return this.isValid(strict, this.getValidationRules());
    }

    public boolean isValid() {
        return this.isValid(false);
    }


    /* Regular Getters and Setters */

    /**
     * @param valid true or false
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return The full UPI text
     */
    public String getRawUpiText() {
        return this.upiText;
    }

    /**
     * @param upiText The full UPI text
     */
    public void setUpiText(String upiText) {
        this.upiText = upiText;
    }

    /**
     * @return The human-readable description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description The human-readable description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public ArrayList<ValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }

    /**
     * @param validationMessages
     */
    private void setValidationMessages(ArrayList<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    /**
     * @return
     */
    public String getCountryName() {
        return this.getCountryCode().getName();
    }

    /**
     * @return
     */
    public CountryCode getCountryCode() {
        return this.countryCode;
    }

    /**
     * @param countryCode
     */
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @param countryCode
     */
    public void setCountryCode(String countryCode) {
        CountryCode enumCode = CountryCode.getByCodeIgnoreCase(countryCode);

        if (enumCode == null) {
            enumCode = CountryCode.UNDEFINED;
            this.setUndefinedCountryCode(countryCode);
        }

        this.setCountryCode(enumCode);
    }

    /**
     * @return
     */
    public String getSubCountryCode() {
        return this.subCountryCode;
    }

    /**
     * @param subCountryCode
     */
    public void setSubCountryCode(String subCountryCode) {
        this.subCountryCode = subCountryCode;
    }

    /**
     * @return
     */
    public String getSubCountyCode() {
        return this.subCountyCode;
    }

    /**
     * @param subCountyCode
     */
    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    /**
     * @return
     */
    public String getPropertyId() {
        return this.propertyId;
    }

    /**
     * @param propertyId
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * @return
     */
    public PropertyTypeCode getPropertyTypeCode() {
        return this.propertyTypeCode;
    }

    public void setPropertyTypeCode(PropertyTypeCode propertyType) {
        this.propertyTypeCode = propertyType;
    }

    public void setPropertyTypeCode(String propertyTypeCode) {
        this.setPropertyTypeCode(PropertyTypeCode.getByCodeIgnoreCase(propertyTypeCode));
    }

    /**
     * @return
     */
    public String getSubProperty() {
        return subProperty;
    }

    /**
     * @param subProperty
     */
    public void setSubProperty(String subProperty) {
        this.subProperty = subProperty;
    }

    public String toString() {
        try {
            return this.toUpi();
        } catch (MalformedUpiTextException e) {
            return "";
        }
    }

    public String getUndefinedCountryCode() {
        return undefinedCountryCode;
    }

    public void setUndefinedCountryCode(String undefinedCountryCode) {
        this.undefinedCountryCode = undefinedCountryCode;
    }

    public ArrayList<ValidationRuleInterface> getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(ArrayList<ValidationRuleInterface> validationRules) {
        this.validationRules = validationRules;
    }
}
