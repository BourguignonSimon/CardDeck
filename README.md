# CardDeck

CardDeck is a Jetpack Compose Android MVP that lets facilitators create anonymous group sessions where participants select up to three emotion cards. Responses are stored in Firebase Firestore and visualized in a facilitator group view.

## Tech Stack

- Kotlin + Jetpack Compose
- Hilt for dependency injection
- Firebase Authentication (anonymous sign-in)
- Firebase Firestore for sessions and responses

## Getting Started

1. Create a Firebase project and enable Anonymous Authentication and Firestore.
2. Download your `google-services.json` file and place it in `app/google-services.json`.
3. Run the project with Android Studio Ladybug or newer (AGP 8.5+, Kotlin 1.9.24).

## Modules

- `app` – Single Android app module containing UI, domain, data, and DI layers structured for clarity.

## Notes

- Session codes are generated randomly (6 alphanumeric characters) and verified for uniqueness before creating a Firestore document.
- Emotion cards are defined locally in `EmotionProvider` and used both by the selection screen and the aggregated group view.
