package accountservice.admin;

public enum AccessOperation {
    LOCK(false, "locked"),
    UNLOCK(true, "unlocked");

    public final boolean boolValue;
    public final String stringValue;

    AccessOperation(boolean boolValue, String stringValue) {
        this.boolValue = boolValue;
        this.stringValue = stringValue;
    }
}
