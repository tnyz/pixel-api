package pixel;

import java.util.HashMap;
import java.util.Map;

public class Pixel {
    public static final String TimestampField = "timestamp";
    public static final String EventField = "event";
    public static final String UserIdField = "user";
    public static final String Click = "click";
    public static final String Impression = "impression";

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
        data.put(UserIdField, user);
        data.put(TimestampField, timestamp);
        data.put(EventField, event);
        return data;
    }

    public boolean isValid() {
        return timestamp > 0 && user != null && hasEvent();
    }

    public boolean isClick() {
        assert event != null;
        return event.equals(Click);
    }

    public boolean isImpression() {
        assert event != null;
        return event.equals(Impression);
    }

    public boolean hasEvent() {
        return event != null && (isImpression() || isClick());
    }
}
