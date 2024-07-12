# Catapult

Catapult is a mobile application for cat enthusiasts that provides comprehensive information about various cat breeds, allows users to test their knowledge with quizzes, and showcases user leaderboard scores. This project is part of the "Mobile Application Development" course.

## Features

### Create Local Account
- On first launch, users must create a local account to use the application. If an account already exists, the app will skip account creation and open the main screen directly.
- Required fields include:
  - Full Name
  - Nickname (letters, numbers, and underscores only)
  - Email (valid email address)

### Cat Breeds Catalog
- Displays a list of cat breeds with a search option.
- Users can view detailed information about each breed by clicking on a breed from the list.
- Includes a photo gallery for each breed and a full-screen photo viewer with swipe navigation.

### Knowledge Quiz About Cats
- The quiz consists of 20 questions displayed one at a time for a better user experience.
- Questions are randomly generated based on the type and data available from CatsApi.
- The quiz has three main categories, each with several question types. More details are in the Quiz Documentation section.
- Users can choose a category before starting the quiz if multiple categories are supported.
- The app records the score for each question and displays the final result after the quiz.
- The quiz is time-limited to 5 minutes. If time runs out, unanswered questions are scored with 0 points.
- Users can publish their results on the global leaderboard using the Leaderboard API.
- The quiz cannot be resumed once canceled and clicking the back button during the quiz prompts a confirmation dialog to cancel the quiz.

### Leaderboard Screen
- Displays all previous results published on the global leaderboard list, sorted from best to worst.
- Each list item includes:
  - Global ranking
  - Player's nickname
  - Game result
  - Total number of quizzes played by the player (published only)

### Account Details
- Profile/Account screen displaying current account information.
- Includes:
  - All account details (from the Create Local Account requirement)
  - Chronological history of all quiz results (published and unpublished)
  - Best result of the user (including all quizzes played, published, and unpublished)
  - Best position of the account on the global leaderboard list

### Edit Account Details
- Screen where users can change their account details and save them locally. 
- Fields validation is required as during account creation.
- Invalid information cannot be saved.

## Technical Requirements
- Must use the following technologies:
  - Kotlin
  - Coroutines
  - Flow
  - Compose
  - MVI architecture
  - Jetpack Navigation
  - Retrofit
  - OkHttp
  - KotlinX Serialization
- Additional requirements:
  - Jetpack Room for database operations
  - Jetpack DataStore for storing simple structures
  - Hilt DI for dependency management
- Local data caching to enhance user experience and avoid unnecessary API calls during the quiz.

## Quiz Technical Details
- Randomization & Dynamism:
  - Questions and answers are dynamically created based on the type and data stored in the local database.
  - No repeated cat images during a quiz.
- Scoring:
  - Formula: UBP = BTO * 2.5 * (1 + (PVT + 120) / MVT)
  - Scores are capped at 100 before being sent to the Leaderboard API.
- Visual Requirements:
  - Use Material Design 3 components.
  - Edge-to-edge functionality.
  - Dark theme support.
  - Mandatory animations and transitions during question changes.
