# BudgetQuest

BudgetQuest is a gamified personal budget tracker for Android built with Kotlin, MVVM, Room, and Navigation Component.

## Team Members
- `ST10446898`
- `ST10436179`
- `ST10445385`
- `ST1045679`
- `ST10446898`

## Features
- Secure register/login with SHA-256 hashed passwords and security-question reset
- 10-screen flow including dashboard, add/edit expense, graphs, profile, goals, and category management
- Room-backed local storage for users, categories, expenses, limits, goals, and badges
- Gamification: XP/levels and badges (First Entry, Week Warrior, Budget Hero)
- Receipt photo attachment via camera/gallery using `ActivityResultContracts`
- Bottom navigation with 5 tabs and child profile flows

## Setup
1. Install Android Studio (JDK 17+)
2. Open this project folder
3. Sync Gradle
4. Run on API 26+ physical device or emulator

## Demo Video
- [Watch demo on YouTube](https://youtu.be/NvN7KHka2zE?si=5-48Ifirfwk0joWu)

## Screen Guide (with mockup references)

### 1) Login
- Lets existing users sign in and move to the main app.
- Includes quick demo account access and forgot-password flow.
![Login]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404717/supabase_blog_users/version_2_2_ytg50q.png)

### 2) Register
- Creates a new user profile with validation and security question.
- Seeds default categories after successful account creation.
![Register]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404717/supabase_blog_users/version_2_1_hbfv8h.png)

### 3) Dashboard (Home)
- Shows monthly spend overview, XP level, quick stats, and recent expenses.
- Main entry point for adding a new expense.
![Dashboard]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404718/supabase_blog_users/version_2_3_xselyx.png)

### 4) Add / Edit Expense
- Captures amount, date/time, category, description, and optional receipt photo.
- Supports both creating a new expense and editing an existing one.
![Add/Edit Expense]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404717/supabase_blog_users/version_2_5_cyelxw.png)

### 5) Expense List (by period)
- Displays expenses in the selected date range.
- Supports pagination (`Load more`) and quick open for editing.
![Expense List](docs/screenshots/screen_05_expense_list.png)

### 6) Category Totals
- Summarizes spending per category with progress/limit context.
- Helps identify overspending categories quickly.
![Category Totals]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404718/supabase_blog_users/version_2_7_qf2wnd.png)

### 7) Spending Graph
- Visualizes spending trends with colorful line charts over time.
- Useful for spotting category-level spending patterns.
![Spending Graph](docs/screenshots/screen_07_graph.png)

### 8) Budget Goals
- Lets users set monthly minimum/maximum budget goals.
- Includes per-category limits for stricter control.
![Budget Goals]( https://res.cloudinary.com/dezmcxbye/image/upload/v1777404718/supabase_blog_users/version_2_8_h5j8gk.png)

### 9) Category Manager
- Create, edit, and remove categories used in expenses and limits.
- Supports color/emoji identity per category.
![Category Manager](https://res.cloudinary.com/dezmcxbye/image/upload/v1777404717/supabase_blog_users/version_2_4_amfxfk.png)


