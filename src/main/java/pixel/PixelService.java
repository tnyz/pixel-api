package pixel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PixelService implements HttpFunction {

    public void service(HttpRequest request, HttpResponse response) {
        switch (request.getMethod()) {
            case "POST":
                writePixel(request, response);
                break;
            case "GET":
                getSummary(request, response);
                break;
        }
    }

    private void getSummary(HttpRequest request, HttpResponse response) {
        try {
            long minTimestamp = request.getFirstQueryParameter("timestamp")
                    .map(Long::parseLong).orElseGet(System::currentTimeMillis);
            response.getWriter().write(pixelSummaryResponse(minTimestamp));
        } catch (Exception e) {
            response.setStatusCode(400);
        }
    }

    private void writePixel(HttpRequest request, HttpResponse response) {
        Pixel pixel = extractPixel(request);
        if (!pixel.isValid()) {
            response.setStatusCode(400, new IllegalArgumentException().getMessage());
            return;
        }

        try {
            ApiFuture<DocumentReference> result = FirestoreConfig.pixelCollection().add(pixel.toMap());
            result.get();
            response.setStatusCode(204);
        } catch (Exception e) {
            response.setStatusCode(400);
        }
    }

    private Pixel extractPixel(HttpRequest request) {
        try {
            Map<String, List<String>> params = request.getQueryParameters();
            return new Pixel(Long.parseLong(params.get(Pixel.TimestampField).get(0)),
                    params.get(Pixel.UserIdField).get(0),
                    params.get(Pixel.EventField).get(0));
        } catch (Exception e) {
            return new Pixel();
        }
    }

    private long epochMilliSeoncdsOneHourOffset(long timestamp) {
        return timestamp - 60 * 60 * 1000;
    }

    private String pixelSummaryResponse(long minTimestampMillis) throws ExecutionException, InterruptedException, IOException {
        List<Pixel> pixels = FirestoreConfig.pixelCollection()
                .whereGreaterThan(Pixel.TimestampField, epochMilliSeoncdsOneHourOffset(minTimestampMillis))
                .get().get().toObjects(Pixel.class);
        int userCount = (int) pixels.stream().map(p -> p.user).distinct().count();
        int clickCount = (int) pixels.stream().filter(Pixel::isClick).count();
        int impressionCount = (int) pixels.stream().filter(Pixel::isImpression).count();
        String[] results = new String[]{
                "unique_users," + userCount,
                "clicks," + clickCount,
                "impressions," + impressionCount
        };
        return String.join("\n", results);
    }
}
