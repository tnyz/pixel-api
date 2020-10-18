package pixel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PixelServiceTest {
    PixelService service = new PixelService();
    @Mock
    HttpRequest request;
    @Mock
    HttpResponse response;
    @Mock
    CollectionReference pixelCollection;

    @Test
    public void testInvalidPost() {
        Map<String, List<String>> queryParameters = new HashMap<>();
        when(request.getMethod()).thenReturn("POST");
        when(request.getQueryParameters()).thenReturn(queryParameters);

        try (MockedStatic<FirestoreConfig> firestore = mockStatic(FirestoreConfig.class)) {
            firestore.when(FirestoreConfig::pixelCollection).thenReturn(pixelCollection);
            service.service(request, response);
            verifyNoInteractions(pixelCollection);
            verify(response).setStatusCode(400);
        }
    }

    @Test
    public void testPost() {
        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("timestamp", Collections.singletonList("100"));
        queryParameters.put("user", Collections.singletonList("user"));
        queryParameters.put("event", Collections.singletonList("click"));

        when(request.getMethod()).thenReturn("POST");
        when(request.getQueryParameters()).thenReturn(queryParameters);
        when(pixelCollection.add(Mockito.any())).thenReturn(Mockito.mock(ApiFuture.class));

        try (MockedStatic<FirestoreConfig> firestore = mockStatic(FirestoreConfig.class)) {
            firestore.when(FirestoreConfig::pixelCollection).thenReturn(pixelCollection);
            service.service(request, response);
            verify(pixelCollection).add(Mockito.any());
            verify(response).setStatusCode(204);
        }
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException, IOException {

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Query query = Mockito.mock(Query.class);
        ApiFuture<QuerySnapshot> apiFuture = Mockito.mock(ApiFuture.class);
        QuerySnapshot snapshot = Mockito.mock(QuerySnapshot.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getFirstQueryParameter("timestamp")).thenReturn(Optional.of("1"));
        when(response.getWriter()).thenReturn(writer);
        when(pixelCollection.whereGreaterThan((String) Mockito.any(), Mockito.any())).thenReturn(query);
        when(query.get()).thenReturn(apiFuture);
        when(apiFuture.get()).thenReturn(snapshot);
        when(snapshot.toObjects(Pixel.class)).thenReturn(Collections.emptyList());

        try (MockedStatic<FirestoreConfig> firestore = mockStatic(FirestoreConfig.class)) {
            firestore.when(FirestoreConfig::pixelCollection).thenReturn(pixelCollection);
            service.service(request, response);
            verify(snapshot).toObjects(Pixel.class);
            verify(writer).write("unique_users,0\nclicks,0\nimpressions,0");
        }
    }
}
