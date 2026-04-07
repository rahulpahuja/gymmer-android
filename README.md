# Gymmer - Gym Management & Workout Tracking App

Gymmer is a comprehensive Android application designed for gym owners, trainers, and members. It provides a seamless experience for managing gym operations, tracking workouts, and monitoring business growth.

## 🚀 Key Features

### 🔐 Security
- **Root Detection**: Enhanced security by preventing the app from running on rooted devices to protect sensitive user and business data.

### 👤 User Features
- **Member Dashboard**: A centralized hub for members to view their progress and daily activities.
- **Workout Tracking**:
    - Browse exercises by category.
    - Detailed exercise instructions and metadata.
    - Integrated **Video Player** for guided workout sessions.
- **Attendance Management**: View personal attendance logs and history.
- **Profile & Wallet**:
    - Manage personal profile information.
    - In-app wallet for managing subscriptions and payments.
- **QR Scanning**: Built-in scanner for quick check-ins or equipment information.

### 👨‍🏫 Trainer & Admin Features
- **Trainer Studio**: Dedicated space for trainers to manage their sessions and content.
- **Trainee Management**: Track and manage individual trainees' progress.
- **Member Onboarding**: Streamlined process for registering new members into the gym system.
- **Business Insights**: Detailed analytics and reporting for gym owners to monitor business performance.
- **Payment Tracking**: Monitor payment defaulters and manage gym revenue efficiently.

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern declarative UI)
- **Navigation**: Compose Navigation
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Storage**: Room Database & DataStore
- **Networking**: Retrofit (implied by DTOs and NetworkManager)
- **Notifications**: Firebase Cloud Messaging (FCM)
- **Dependency Injection**: (Likely Hilt/Dagger based on standard modern practices)

## 📁 Project Structure
- `ui/`: Contains all Compose-based UI components, screens, and themes.
- `data/`: Handles data logic, including networking, local database, and preferences.
- `navigation/`: Manages the app's navigation graph and screen routing.

## 🚧 Potential Roadmap / Missing Features
Based on the current architecture, here are some areas for future development:

- **Offline Workout Persistence**: Expand the local database (`AppDatabase`) by adding Room tables for `WorkoutLog`, `Exercise`, and `UserSession`. This will allow users to track their progress and log workouts even without an active internet connection.
- **Nutrition & Diet Tracking**: Add a module for meal logging or daily calorie/macro tracking to complement the physical workout features for a more holistic health approach.
- **Social & Gamification**: 
    - **Community Feed**: A space where members can share achievements and stay motivated.
    - **Leaderboards**: Friendly competition among trainees to boost engagement.
- **Communication & Feedback**: Integrate a simple chat system or a "Feedback" loop where trainers can leave notes on specific workout logs for their trainees.
- **Wearable Integration**: Add **Wear OS** support for real-time heart rate monitoring and automatic rep counting—a significant "pro" feature for modern fitness apps.
- **Class Booking & Scheduling**: A "Class Booking" system within the Dashboard or a dedicated Schedule screen for members to book slots for group classes (Yoga, HIIT, etc.).
- **Enhanced Analytics**: More granular data visualization for member progress, such as weight tracking graphs and strength progression over time.
