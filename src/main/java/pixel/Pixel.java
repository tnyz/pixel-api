package pixel;

import java.util.HashMap;
import java.util.Map;

public class Pixel {
    public static final String TIMESTAMP_FIELD = "timestamp";
    public static final String EVENT_FIELD = "event";
    public static final String USER_ID_FIELD = "user";
    public static final String CLICK = "click";
    public static final String IMPRESSION = "impression";

    public final long timestamp;
    public final String user;
    public final String event;

    public Pixel(long timestamp, String user, String event) {
        this.timestamp = timestamp;
        this.user = user;
        this.event = event;
    }

    public Pixel() {
        timestamp = 0;
        user = null;
        event = null;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put(USER_ID_FIELD, user);
        data.put(TIMESTAMP_FIELD, timestamp);
        data.put(EVENT_FIELD, event);
        return data;
    }

    public boolean isValid() {
        return timestamp > 0 && user != null && hasEvent();
    }

    public boolean isClick() {
        assert event != null;
        return event.equals(CLICK);
    }

    public boolean isImpression() {
        assert event != null;
        return event.equals(IMPRESSION);
    }

    public boolean hasEvent() {
        return event != null && (isImpression() || isClick());
    }
}
