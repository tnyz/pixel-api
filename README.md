# pixel-api

A serverless pixel service API backed by NoSQL (Firestore).

# demo

GET: https://us-central1-narrative-292719.cloudfunctions.net/analytics?timestamp=1

POST: "https://us-central1-narrative-292719.cloudfunctions.net/analytics?timestamp=1&user=user&event=click"

# deployment

```
gcloud functions deploy $endpoint --entry-point=pixel.PixelService --runtime=java11 --trigger-http --source=$jar 
--set-env-vars PROJECT_ID=$project_id,DATASET=&firestore_collection
```

[More about deployment](https://cloud.google.com/functions/docs/concepts/java-deploy#deploy_from_a_jar)
