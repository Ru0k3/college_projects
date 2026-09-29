# 📘 Online Examination System

A modular Java-based Online Examination System designed using object-oriented principles and layered architecture. The project is structured for scalability, maintainability, and real-world backend design practices.

---

# 📁 Project Folder Structure

```
online_examination_system/
├── auth
├── exams
├── gui
├── main
├── questions
└── users
```

---

# 🧠 Architecture Overview

The system follows a **layered modular design**, where each package has a clear responsibility:

| Layer     | Purpose                                           |
| --------- | ------------------------------------------------- |
| auth      | Handles login and authentication logic            |
| users     | Stores user entities like Admin, Faculty, Student |
| questions | Defines question types (MCQ, Descriptive, etc.)   |
| exams     | Manages exam creation, evaluation, and results    |
| gui       | Contains UI screens (Login, Dashboards)           |
| main      | Entry point of the application                    |

---

# 🔐 auth

```
auth/
└── Authentication.java
```

### Purpose:

Handles login validation and authentication logic.

### Responsibility:

* User login verification
* Security checks (future expansion: hashing, JWT, etc.)

---

# 👤 users

```
users/
├── Admin.java
├── Faculty.java
├── Student.java
└── Users.java
```

### Purpose:

Defines all system user types.

### Responsibility:

* Base `Users` class for shared properties
* Specialized roles:

  * Admin → system control
  * Faculty → question & exam management
  * Student → exam participation

---

# ❓ questions

```
questions/
├── Question.java
├── MCQQuestion.java
└── DescriptiveQuestion.java
```

### Purpose:

Defines different types of exam questions.

### Responsibility:

* Base `Question` class (inheritance root)
* MCQ and Descriptive implementations
* Supports extensibility for new question types

---

# 🧮 exams

```
exams/
├── Exam.java
├── Answer.java
├── EvaluationSystem.java
└── Result.java
```

### Purpose:

Handles exam lifecycle and evaluation.

### Responsibility:

* Creating and storing exams
* Collecting answers
* Evaluating student performance
* Generating results

---

# 🖥️ gui

```
gui/
├── LoginFrame.java
├── AdminDashboard.java
├── FacultyDashboard.java
└── StudentDashboard.java
```

### Purpose:

User interface layer of the system.

### Responsibility:

* Login screen
* Role-based dashboards
* Navigation between modules

---

# 🚀 main

```
main/
└── Main.java
```

### Purpose:

Entry point of the application.

### Responsibility:

* Starts the application
* Loads login screen or initial UI

---

# 🏗️ Design Principles Used

* ✅ Object-Oriented Programming (OOP)
* ✅ Encapsulation (private fields + getters/setters)
* ✅ Inheritance (Question → MCQ/Descriptive, Users → roles)
* ✅ Polymorphism (future exam evaluation logic)
* ✅ Modular package separation

---

# 📌 Future Improvements

* Database integration (MySQL / PostgreSQL)
* Secure authentication (hashed passwords)
* Timer-based exams
* Auto evaluation engine
* REST API (Spring Boot upgrade)
* Web-based frontend (React / Angular)

---

# 💡 Goal of this Project

This project is designed as a **real-world simulation of an exam platform**, similar to systems used in universities and online testing platforms.

---
