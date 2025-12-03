Goal of this Flow

This collection demonstrates the full Part 2 async pipeline:

1️⃣ Create a resource
2️⃣ Create an event
3️⃣ Create a goal (Postman automatically saves goalId)
4️⃣ Mark the goal as completed → publishes Avro Kafka event
5️⃣ Get recommendations → event-service consumes the Kafka event

If everything works, you’ll receive an event recommendation based on the goal category (“mindfulness”).

🚀 Step-by-Step Instructions
STEP 1 — Create Resource

Request:
1) Create Resource (wellness-resource-service)

This creates a mindfulness resource so event-service can recommend it later.

Just click Send.

Expected Response (sample):

{
"resourceId": 2,
"title": "Mindfulness Guide",
"description": "..",
"category": "mindfulness",
"url": "http://ex"
}

STEP 2 — Create Event

Request:
2) Create Event (event-service)

This adds an event with category = "mindfulness".

Click Send.

Expected:

{
"eventId": 2,
"title": "Meditation Circle",
...
"category": "mindfulness"
}

STEP 3 — Create Goal (Saves goalId automatically)

Request:
3) Create Goal (goal-tracking-service) — saves goalId

Click Send.

Postman will:

✔ Parse the response
✔ Extract "goalId"
✔ Save it to the collection’s variable {{goalId}}
✔ Display a green test saying goalId saved

Expected output contains:

"goalId": "some_id_here"

STEP 4 — Complete Goal (Triggers Kafka Event)

Request:
4) Complete Goal (publishes Kafka event)

This calls:

PATCH /goals/{{goalId}}/complete

Click Send.

This will:

✔ Update the goal status to "completed"
✔ Publish an Avro serialized GoalCompletedEvent to Kafka
✔ event-service consumes it and stores recommendations internally

Expected:

{
"status": "completed",
"category": "mindfulness"
}

STEP 5 — Get Recommendations (Kafka Consumer Output)

Request:
5) Get Recommendations (event-service consumes Kafka)

This returns recommended events for this goal, based on category.

Click Send.

Expected response:

[
{
"eventId": 2,
"title": "Meditation Circle",
"category": "mindfulness"
}
]

This proves the full async pipeline works.