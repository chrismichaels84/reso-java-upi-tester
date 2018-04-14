package org.reso.upi.property_type_code;

public enum PropertyTypeCode {
    R("Real and Taxable Property Type"),
    S("Coop, apartment, etc Property type"),
    T("Temporary Property Type"),

    NON_STANDARD("Non Standard RESO Property Type");

    private final String description;

    private String nonStandardCode;
    private String nonStandardDescription;

    /* Constructors */
    PropertyTypeCode(String description) {
        this.description = description;
    }


    /* Factory getters */
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

    public static PropertyTypeCode getByCode(String code) {
        return PropertyTypeCode.getByCodeIgnoreCase(code);
    }

    public static PropertyTypeCode getByCodeIgnoreCase(String propertyTypeCode) {
        return PropertyTypeCode.getByCode(propertyTypeCode, false);
    }


    /* Factory for a NON-STANDARD Property Type */
    public static PropertyTypeCode getNonStandard(String code, String description) {
        return PropertyTypeCode.NON_STANDARD
                .setNonStandardCode(code)
                .setNonStandardDescription(description);
    }


    /* Getters and Setters */
    public String getDescription() {
        switch (this) {
            case NON_STANDARD:
                return this.getNonStandardDescription();

            default:
                return this.description;
        }
    }

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
