# Gymmer API Requirements

This document outlines the API endpoints required to support the Gymmer Android application.

## Base URL
`https://api.gymmer.com/v1`

## Authentication
All requests except Login and Register require a `Bearer` token in the `Authorization` header.

### Endpoints
- `POST /auth/login`: Authenticate user and return JWT.
- `POST /auth/register`: Register a new Trainee or Trainer.
- `POST /auth/refresh`: Refresh expired JWT.

---

## User & Trainee Profile
- `GET /user/profile`: Fetch current user details.
- `PUT /user/profile`: Update profile info (avatar, bio, goals).
- `GET /user/dashboard`: Fetch trainee-specific dashboard data (check-in status, daily target, active sessions).
- `POST /user/check-in`: Log a gym check-in.

---

## Workouts & Exercises
- `GET /exercises`: List all exercises with filters (category, difficulty).
- `GET /exercises/{id}`: Detail of a specific exercise including video URL.
- `GET /workouts`: Fetch recommended workout plans.
- `GET /workouts/{id}`: Workout details and exercise list.
- `POST /workouts/log`: Log a completed workout session.

---

## Community & Social
- `GET /community/feed`: Fetch latest posts from the community.
- `POST /community/posts`: Create a new post (text/image).
- `POST /community/posts/{id}/like`: Toggle like on a post.
- `GET /community/posts/{id}/comments`: Fetch comments for a post.
- `POST /community/posts/{id}/comments`: Add a comment.
- `GET /community/leaderboard`: Fetch top performers based on points.

---

## Trainer Specific
- `GET /trainer/dashboard`: Overview for trainers (active trainees, upcoming sessions).
- `GET /trainer/trainees`: List of assigned trainees.
- `GET /trainer/trainees/{id}/progress`: Detailed attendance and workout logs for a specific trainee.
- `POST /trainer/trainees/{id}/plan`: Assign a workout or nutrition plan to a trainee.

---

## Business & Admin (GymOps)
- `GET /business/insights`: High-level metrics (Annual Revenue, Retention Rate, Member Growth).
- `GET /business/pulse`: Real-time capacity and activity across different gym branches.
- `GET /business/revenue-kinetics`: Historical revenue data for charting.
- `GET /business/defaulters`: List of members with pending payments.

---

## Nutrition
- `GET /nutrition/today`: Fetch today's meal plan.
- `POST /nutrition/log`: Log water or calorie intake.

---

## Messaging
- `GET /chat/conversations`: List of active chats.
- `GET /chat/{userId}/messages`: Message history with a specific user.
- `POST /chat/{userId}/messages`: Send a message.

---

## Media & Assets
- Support for multipart/form-data for image uploads (avatars, community posts).
- Video streaming support for exercise demonstrations (HLS/Dash preferred for production).
