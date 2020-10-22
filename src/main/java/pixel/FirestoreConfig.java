package pixel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.IOException;

public class FirestoreConfig {
    private static final String projectId = System.getenv("PROJECT_ID");

    private static final String collection = System.getenv("DATASET");

    private static Firestore connectFirestore() throws IOException {
        FirestoreOptions firestoreOptions = projectId == null ?
                FirestoreOptions.getDefaultInstance() :
                FirestoreOptions.getDefaultInstance()
                        .toBuilder()
                        .setProjectId(projectId)
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

        return firestoreOptions.getService();
    }

    public static CollectionReference pixelCollection() throws IOException {
        Firestore db = connectFirestore();
        return db.collection(collection);
    }
}
