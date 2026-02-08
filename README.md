# Spring-Boot-Project-Assignment
# 🏋️‍♂️ IRON MUSCLE BEAST - Gym Management System

Welcome to **IRON MUSCLE BEAST**, a comprehensive web-based Gym Management System designed to streamline gym operations, manage member memberships, track payments, and monitor attendance in real-time.

Built with **Spring Boot** and **Thymeleaf**, this application features a dark, aggressive "Beast Mode" UI tailored for the fitness industry.

## 🚀 Key Features

### 👮‍♂️ Admin Panel
* **Dashboard:** Real-time overview of total members, daily attendance, and monthly income.
* **Member Management:** Add, edit, delete, and view detailed member profiles (Physical stats, Medical info).
* **Payment Tracking:** Record monthly payments and view payment history.
* **Attendance Logs:** View daily attendance records with time and member details.

### 👤 User (Member) Dashboard
* **Personalized Dashboard:** View workout streak, BMI/Physical stats, and Payment Status (Paid/Overdue).
* **Digital ID:** Mobile-friendly digital member card with QR code.
* **Profile Management:** Update personal details (Weight, Height, Emergency Contacts).

### 🖥️ Kiosk / Check-In Screen
* **Fast Check-In:** Members can check in using their Phone Number or ID.
* **Auto-Refresh:** The screen automatically resets after 5 seconds for the next member.
* **Visual Feedback:** Distinct success (Green) and error (Red) messages.

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
* **Frontend:** HTML5, CSS3, Bootstrap 5, Thymeleaf
* **Database:** MySQL
* **Build Tool:** Maven

## ⚙️ Installation & Setup

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/YourUsername/iron-muscle-beast.git](https://github.com/YourUsername/iron-muscle-beast.git)
    ```
2.  **Configure Database**
    * Create a MySQL database named `gym_db`.
    * Update `src/main/resources/application.properties` with your MySQL username and password.
3.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```
4.  **Access the App**
    * Browser: `http://localhost:8080`
    * Admin Login: (Use the credentials you set up)


---
*Powered by Iron Muscle System*
