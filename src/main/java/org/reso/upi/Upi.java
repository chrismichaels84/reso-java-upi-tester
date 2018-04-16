package org.reso.upi;

import org.reso.upi.country_code.CountryCode;
import org.reso.upi.property_type_code.PropertyTypeCode;
import org.reso.upi.validation.ValidationMessage;
import org.reso.upi.validation.ValidationRuleInterface;
import org.reso.upi.validation.rules.ResoAllPiecesRequiredRule;
import org.reso.upi.validation.rules.ResoValidCountryCodeRule;
import org.reso.upi.validation.rules.ResoValidPropertyTypeCodeRule;

import java.util.ArrayList;

/**
 * Base UPI class that implements and UpiInterfaces
 */
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
     * Creates an Empty UPI instance
     */
    public Upi() {
        this.setValidationRules(getDefaultValidationRules());
    }

    /**
     * Parse a UPI text on construction, but does not validate
     *
     * @param upiText valid upi text
     */
    public Upi(String upiText) throws MalformedUpiTextException {
        this();
        this.parseUpi(upiText);
    }

    /**
     * Parse a UPI on construction
     *
     * @param upiText     valid upi text
     * @param description human readable description
     */
    public Upi(String upiText, String description) throws MalformedUpiTextException {
        this(upiText);
        this.setDescription(description);
    }

    /**
     * Give all the parameters, including description
     *
     * @param countryCode      CountryCode enumeration
     * @param subCountryCode   sub country code
     * @param subCountyCode    Sub county code
     * @param propertyId       Property identifier
     * @param propertyTypeCode Type of property enum
     * @param subProperty      Individual property unit number, etc
     * @param description      human readable description
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
     * @param countryCode      CountryCode enumeration
     * @param subCountryCode   sub country code
     * @param subCountyCode    Sub county code
     * @param propertyId       Property identifier
     * @param propertyTypeCode Type of property enum
     * @param subProperty      Individual property unit number, etc
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
     * @return Valid upi string
     * @throws MalformedUpiTextException if upi text is incomplete
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

    /**
     * Hydrates a UPI object with a description, if complete upi is given
     * If UPI is incomplete, returns false.
     *
     * @param upiText     Hopefully complete UPI `US-36061-N-010237502R1-S-113`
     * @param description Human readable description
     */
    public void parseUpi(String upiText, String description) throws MalformedUpiTextException {
        this.parseUpi(upiText);
        this.setDescription(description);
    }

    /**
     * Hydrates a UPI object with no description, if complete upi is given
     * If UPI is incomplete, returns false.
     *
     * @param upiText Hopefully complete UPI `US-36061-N-010237502R1-S-113`
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

    /**
     * Validate a built UPI instance against the default rules in non-strict mode
     *
     * @return the validation messages arraylist
     */
    public ArrayList<ValidationMessage> validate() {
        return this.validate(false);
    }

    /**
     * Validate a built UPI instance against the default rules
     *
     * @return the validation messages arraylist
     */
    public ArrayList<ValidationMessage> validate(boolean strict) {
        return this.validate(strict, this.getValidationRules());
    }

    /**
     * Validate a built UPI instance against a given ruleset
     *
     * @return the validation messages arraylist
     */
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

    /**
     * Validates the current instance and returns true or false
     */
    public boolean isValid(boolean strict, ArrayList<ValidationRuleInterface> ruleSet) {
        this.validate(strict, ruleSet);
        return this.valid;
    }

    /**
     * Validates the current instance and returns true or false
     * given strict mode
     */
    public boolean isValid(boolean strict) {
        return this.isValid(strict, this.getValidationRules());
    }

    /**
     * Validates the current instance and returns true or false
     * default rule set in non-strict mode
     */
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
     * Does not validate, just returns already validated messages (or null)
     *
     * @return the current vaidation messages
     */
    public ArrayList<ValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }

    /**
     * @param validationMessages Validation message
     */
    private void setValidationMessages(ArrayList<ValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    /**
     * @return country name
     */
    public String getCountryName() {
        return this.getCountryCode().getName();
    }

    /**
     * @return country code
     */
    public CountryCode getCountryCode() {
        return this.countryCode;
    }

    /**
     * @param countryCode country code
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
     * @param countryCode country code
     */
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return sub country code
     */
    public String getSubCountryCode() {
        return this.subCountryCode;
    }

    /**
     * @param subCountryCode sub country code
     */
    public void setSubCountryCode(String subCountryCode) {
        this.subCountryCode = subCountryCode;
    }

    /**
     * @return sub county code
     */
    public String getSubCountyCode() {
        return this.subCountyCode;
    }

    /**
     * @param subCountyCode sub county code
     */
    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    /**
     * @return property id
     */
    public String getPropertyId() {
        return this.propertyId;
    }

    /**
     * @param propertyId property id
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * @return property type code
     */
    public PropertyTypeCode getPropertyTypeCode() {
        return this.propertyTypeCode;
    }

    /**
     * @param propertyTypeCode Code as a string
     */
    public void setPropertyTypeCode(String propertyTypeCode) {
        this.setPropertyTypeCode(PropertyTypeCode.getByCodeIgnoreCase(propertyTypeCode));
    }

    /**
     * @param propertyType Property type code enum
     */
    public void setPropertyTypeCode(PropertyTypeCode propertyType) {
        this.propertyTypeCode = propertyType;
    }

    /**
     * @return sub property
     */
    public String getSubProperty() {
        return subProperty;
    }

    /**
     * @param subProperty sub property
     */
    public void setSubProperty(String subProperty) {
        this.subProperty = subProperty;
    }

    /**
     * @return Country code if country code is `UNDEFINED`
     */
    public String getUndefinedCountryCode() {
        return undefinedCountryCode;
    }

    /**
     * @param undefinedCountryCode Country code if country code is `UNDEFINED`
     */
    public void setUndefinedCountryCode(String undefinedCountryCode) {
        this.undefinedCountryCode = undefinedCountryCode;
    }

    /**
     * @return current validation rules
     */
    public ArrayList<ValidationRuleInterface> getValidationRules() {
        return validationRules;
    }

    /**
     * @param validationRules Current validation rules
     */
    public void setValidationRules(ArrayList<ValidationRuleInterface> validationRules) {
        this.validationRules = validationRules;
    }

    /**
     * Clears the current UPI object
     */
    public void clear() {
        this.setValidationRules(getDefaultValidationRules());

        this.countryCode = null;
        this.subCountryCode = null;
        this.subCountyCode = null;
        this.propertyId = null;
        this.propertyTypeCode = null;
        this.subProperty = null;

        this.valid = false;
        this.upiText = null;
        this.validationMessages = null;
        this.undefinedCountryCode = null;
    }

    /**
     * @return ArrayList of default rules
     */
    private ArrayList<ValidationRuleInterface> getDefaultValidationRules() {
        ArrayList<ValidationRuleInterface> rules = new ArrayList<ValidationRuleInterface>();

        rules.add(new ResoAllPiecesRequiredRule());
        rules.add(new ResoValidCountryCodeRule());
        rules.add(new ResoValidPropertyTypeCodeRule());
        return rules;
    }

    /**
     * @throws MalformedUpiTextException if the UPI Text is incomplete
     */
    private void throwExceptionIfIncompleteUpi() throws MalformedUpiTextException {
        // First, we need all the pieces in order to create a UPI Text
        ResoAllPiecesRequiredRule rule = new ResoAllPiecesRequiredRule();
        ArrayList<ValidationMessage> errors = rule.validate(this, false); // this will give us any missing pieces

        if (errors.size() > 0) {
            throw new MalformedUpiTextException(errors);
        }
    }

    /**
     * @return the built UPI text
     */
    public String toString() {
        try {
            return this.toUpi();
        } catch (MalformedUpiTextException e) {
            return "";
        }
    }
}
