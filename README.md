# Expense Tracker 💰📱

A modern, native Android application built with **Jetpack Compose** and **Room Database** to help users track, manage, and analyze their daily expenses effortlessly.

---

## ✨ Key Features

- 📊 **Expense Category Pie Chart**: View a visual breakdown of your monthly spending by category with percentages and formatted amounts in Indian Rupees (`₹`).
- 📅 **Flexible Date Selection**: Backdate missed expenses or edit the date for existing/upcoming transactions using the integrated Material 3 Date Picker.
- ✏️ **Edit & Delete Expenses**: Easily modify titles, amounts, dates, or categories of existing expenses without affecting backend database integrity.
- ➕ **Custom Categories**: Filter expenses using default categories or create custom categories on the fly.
- 📆 **Monthly Spending Overview**: Seamlessly navigate between months to track total monthly expenditure and review historical logs.
- 📤 **CSV Export**: Export your monthly expense reports into a `.csv` file directly via Android's Storage Access Framework (SAF).
- 🎨 **Modern Material 3 Design**: Smooth UI transitions, adaptive app icons, responsive layout with FAB overhang prevention, and clean dark mode aesthetic.

---

## 🛠️ Tech Stack & Architecture

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 Design
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Navigation:** [Navigation Compose](https://developer.android.com/guide/navigation) with smooth horizontal slide animations
- **Reactive Data Streams:** Kotlin Coroutines & `StateFlow`
- **Build System:** Gradle (Kotlin DSL) with Version Catalogs

---

## 📂 Project Structure

```
com.expense.tracker/
├── data/
│   ├── dao/           # Room DAOs (ExpenseDao, CategoryDao)
│   ├── db/            # AppDatabase instance
│   ├── model/         # Room Entities (Expense, Category)
│   └── repository/    # ExpenseRepository
├── ui/
│   ├── components/    # Reusable UI components (ExpenseItem, MonthSelector, TotalSpentCard)
│   ├── screens/       # MainScreen, PieChartScreen, AddExpenseBottomSheet
│   └── theme/         # Material 3 Color palette, Typography, Theme
├── utils/             # DateUtils, CsvExporter
└── viewmodel/         # ExpenseViewModel
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio:** 2024.1.1 (Ladybug) or newer recommended
- **JDK Version:** 17
- **Minimum SDK:** API 26 (Android 8.0)
- **Target SDK:** API 35 (Android 15)

### Building the App
1. Clone the repository:
   ```bash
   git clone git@github.com:Siddhu2/expenseAppMobile.git
   cd expenseAppMobile
   ```
2. Open the project in **Android Studio**.
3. Allow Gradle to sync dependencies.
4. Connect an Android physical device or start an emulator.
5. Run the app by clicking **Run 'app'** or executing:
   ```bash
   ./gradlew :app:installDebug
   ```

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
