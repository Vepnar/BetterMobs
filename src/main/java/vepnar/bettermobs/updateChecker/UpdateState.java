package vepnar.bettermobs.updateChecker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum UpdateState {
    UPDATED("Up to date"),
    OUTDATED("Outdated"),
    FAILED("Can't check for updates"),
    DISABLED("Disabled"),
    NOT_CHECKED("Not checked");

    private final String DESCRIPTION;

    private static final Map<String,UpdateState> ENUM_MAP;

    UpdateState (String description) {
        this.DESCRIPTION = description;
    }

    public String getDescription() {
        return this.DESCRIPTION;
    }

    // Build an immutable map of String name to enum pairs.
    // Any Map impl can be used.

    static {
        Map<String,UpdateState> map = new ConcurrentHashMap<>();
        for (UpdateState instance : UpdateState.values()) {
            map.put(instance.getDescription().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static UpdateState get (String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
