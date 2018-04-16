package org.reso.upi.property_type_code;

/**
 * Enumeration for Property Type Codes
 */
public enum PropertyTypeCode {
    R("Residential"),
    L("Land"),
    C("Commercial"), // @todo: All the property types?

    NON_STANDARD("Non Standard RESO Property Type");

    private final String description;

    private String nonStandardCode;
    private String nonStandardDescription;

    /* Constructors */
    /**
     * @param description Human readable description
     */
    PropertyTypeCode(String description) {
        this.description = description;
    }


    /* Factory getters */

    /**
     * Returns enum by text code
     * @param code single letter text code
     * @param caseSensitive normalize case?
     * @return enumeration
     */
    public static PropertyTypeCode getByCode(String code, boolean caseSensitive) {
        if (code == null) {
            return null;
        }

        String testCode = code;

        if (!caseSensitive) {
            testCode = code.toUpperCase();
        }

        try {
            return Enum.valueOf(PropertyTypeCode.class, testCode);

        } catch (IllegalArgumentException e) {
            return PropertyTypeCode.NON_STANDARD.setNonStandardCode(code);
        }
    }

    /**
     * @param code single letter code
     * @return ignore case
     */
    public static PropertyTypeCode getByCode(String code) {
        return PropertyTypeCode.getByCodeIgnoreCase(code);
    }

    /**
     * @param propertyTypeCode single letter code
     * @return ignoring case
     */
    public static PropertyTypeCode getByCodeIgnoreCase(String propertyTypeCode) {
        return PropertyTypeCode.getByCode(propertyTypeCode, false);
    }


    /* Factory for a NON-STANDARD Property Type */

    /**
     * Produces a NON_STANDARD enumb value
     * @param code non standard code
     * @param description human readable description
     * @return enum value
     */
    public static PropertyTypeCode getNonStandard(String code, String description) {
        return PropertyTypeCode.NON_STANDARD
                .setNonStandardCode(code)
                .setNonStandardDescription(description);
    }


    /* Getters and Setters */

    /**
     * @return human readable description.
     */
    public String getDescription() {
        switch (this) {
            case NON_STANDARD:
                return this.getNonStandardDescription();

            default:
                return this.description;
        }
    }

    /**
     * @return Standard code
     */
    public String getCode() {
        switch (this) {
            case NON_STANDARD:
                return this.getNonStandardCode();

            default:
                return super.toString();
        }
    }

    public String getNonStandardCode() {
        return this.nonStandardCode;
    }

    public PropertyTypeCode setNonStandardCode(String nonStandardCode) {
        this.nonStandardCode = nonStandardCode;
        return this;
    }

    public String getNonStandardDescription() {
        return this.nonStandardDescription;
    }

    public PropertyTypeCode setNonStandardDescription(String nonStandardDescription) {
        this.nonStandardDescription = nonStandardDescription;
        return this;
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
