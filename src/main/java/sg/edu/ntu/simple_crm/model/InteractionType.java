package sg.edu.ntu.simple_crm.model;

public enum InteractionType {
    CALL,
    EMAIL,
    MEETING,
    OTHER;

    public static InteractionType fromString(String value) {
        for (InteractionType type : InteractionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown interaction type: " + value);
    }
}
