package balbucio.org.firebase4j.model;

import lombok.Getter;

import java.util.Arrays;

public enum DeleteProtectionState {

    UNSPECIFIED("DELETE_PROTECTION_STATE_UNSPECIFIED"),
    DISABLED("DELETE_PROTECTION_DISABLED"),
    ENABLED("DELETE_PROTECTION_ENABLED");

    @Getter
    private String raw;

    DeleteProtectionState(String raw) {
        this.raw = raw;
    }

    public static DeleteProtectionState fromRaw(String raw) {
        return Arrays.stream(values()).filter(v -> v.getRaw().equalsIgnoreCase(raw)).findFirst().orElse(UNSPECIFIED);
    }
}
