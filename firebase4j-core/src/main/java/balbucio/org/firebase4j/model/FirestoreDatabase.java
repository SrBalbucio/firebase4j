package balbucio.org.firebase4j.model;

import lombok.Getter;

import java.util.Arrays;

public class FirestoreDatabase {

    private String name;
    private String uid;
    private String createTime;
    private String updateTime;
    private String deleteTime = null;
    private String locationId;
    private Type type = Type.UNSPECIFIED;
    private ConcurrencyMode concurrencyMode = ConcurrencyMode.UNSPECIFIED;
    private String versionRetentionPeriod;
    private String earliestVersionTime;
    private String keyPrefix;
    private DeleteProtectionState deleteProtectionState = DeleteProtectionState.UNSPECIFIED;
    private String previousId = null;


    public enum ConcurrencyMode {
        UNSPECIFIED("CONCURRENCY_MODE_UNSPECIFIED"),
        OPTIMISTIC("OPTIMISTIC"),
        PESSIMISTIC("PESSIMISTIC"),
        OPTIMISTIC_WITH_ENTITY_GROUPS("OPTIMISTIC_WITH_ENTITY_GROUPS");

        @Getter
        private String raw;

        ConcurrencyMode(String raw) {
            this.raw = raw;
        }

        public static ConcurrencyMode fromRaw(String raw) {
            return Arrays.stream(values()).filter(v -> v.getRaw().equalsIgnoreCase(raw)).findFirst().orElse(UNSPECIFIED);
        }

    }

    public enum Type {
        UNSPECIFIED("DATABASE_TYPE_UNSPECIFIED"),
        NATIVE("FIRESTORE_NATIVE"),
        DATASTORE("DATASTORE_MODE");

        @Getter
        private String raw;

        Type(String raw) {
            this.raw = raw;
        }

        public static Type fromRaw(String raw) {
            return Arrays.stream(values()).filter(v -> v.getRaw().equalsIgnoreCase(raw)).findFirst().orElse(UNSPECIFIED);
        }

    }
}
