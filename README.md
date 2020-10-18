# pixel-api

A serverless pixel service API backed by NoSQL (Firestore).

GET: https://us-central1-narrative-292719.cloudfunctions.net/analytics?timestamp=1

POST: "https://us-central1-narrative-292719.cloudfunctions.net/analytics?timestamp=1&user=user&event=click"

# deployment

```
gcloud functions deploy analytics --entry-point=pixel.PixelService --runtime=java11 --trigger-http --source=$jar 
--set-env-vars PROJECT_ID=$project_id,DATASET=&firestore_collection
```
