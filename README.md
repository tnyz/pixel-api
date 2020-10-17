# pixel-api

A serverless pixel service API backed by NoSQL (Firestore).

# deployment

```
gcloud functions deploy analytics --entry-point=pixel.PixelService --runtime=java11 --trigger-http --source=$jar 
--set-env-vars PROJECT_ID=$project_id,DATASET=&firestore_collection
```
