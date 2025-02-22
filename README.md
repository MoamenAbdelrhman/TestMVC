# 🍽️ Meal Categories App - MVC (Model-View-Controller)

This project implements **Model-View-Controller (MVC)** architecture in an Android app.  
It fetches meal categories from **TheMealDB API** and allows users to **add/remove favorites**.

---

## 📌 Features
- ✅ Fetch meal categories from **TheMealDB API**
- ✅ Display categories in **RecyclerView**
- ✅ Add categories to **Favorites (Room Database)**
- ✅ Remove categories from Favorites
- ✅ Implements **MVC Architecture**

---

## ❌ Drawbacks of MVC
- **Activities contain too much logic**, making them **hard to maintain**.
- **No separation between UI and business logic**.
- **Difficult to test**.

---

---
## 🚀 Installation Guide
### **🔹 Clone the Repository**
git clone https://github.com/your-repo/TestMVC.git
cd TestMVC

🔹 Open in Android Studio
Open Android Studio
Click "Open an Existing Project"
Select TestMVC
Sync Gradle and Run! 🚀

⚙️ Tech Stack
Kotlin 🟡
Retrofit (API requests) 🌐
Room Database (Local storage) 🏡

📌 API Used
This project uses TheMealDB API to fetch meal categories.
Base URL:
https://www.themealdb.com/api/json/v1/1/
