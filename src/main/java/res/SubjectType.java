package res;

public enum SubjectType {
    LECT, PRAC;
    public static SubjectType convertFromString(String name) {
        SubjectType type = switch (name.toUpperCase()) {
            case "LECT":
                yield SubjectType.LECT;
            case "PRAC":
                yield  SubjectType.PRAC;
            default:
                throw new IllegalStateException("Unexpected value: " + name.toUpperCase());
        };
        return type;
    }
}
