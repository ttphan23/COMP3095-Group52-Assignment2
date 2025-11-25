1. Get an Access Token (Mandatory for all protected endpoints)
Pick the login request:

Student Login → for testing goals & event registration

Staff Login → for testing resource creation

In Postman:

Open the Login – Get Token (student/staff) request.

Go to the Body tab.

Confirm username/password are correct.

Hit Send.

You will receive a JSON response:
{
"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}

Copy ONLY the value of access_token

(Do not include quotes " ")

2. Where to put the token

For every subsequent request, you must insert the token here:

In Postman:

Open the request you want to call
(e.g., Create Goal, Register Event, Create Resource, etc.)

Go to the Authorization tab.

Choose Bearer Token as the type.

Paste your copied token into the Token field.

✔ DO NOT paste it into Headers manually
✔ DO NOT include the word “Bearer ” yourself
Postman adds it automatically.

3. Send the request normally

Once the token is set correctly:

Students can create goals

Students can register for events

Staff can create resources

Unauthorized actions will correctly give 403 Forbidden

Just hit Send — the Gateway + Keycloak handle the rest.

4. If you get 401 Unauthorized

Your token probably expired.

Just:

Go back to the Login – Get Token request

Request a new token

Paste it again into Bearer Token field

Then retry.