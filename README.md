# Restaurant Management System

A command-line **Restaurant Management System** written in **Java (JDBC + OOP)** that models typical restaurant workflows: customer registration/login, table reservations (with auto-release), order management, menu management, billing, employee roles and more.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Prerequisites](#prerequisites)
5. [Repository structure / Folder tree](#repository-structure--folder-tree)
6. [Database setup](#database-setup)
7. [Configuration](#configuration)
8. [Run the application](#run-the-application)
9. [Design & Architecture](#design--architecture)
   
---

## Project Overview

A modular, layered Java application that simulates the core operations of a restaurant: customers book tables and place orders, waiters manage orders, chefs prepare items, managers/admins view sales and manage the menu. The system demonstrates multithreading (for timed reservation release), DAO/repository patterns and separation between models, repositories, services, and the command interface.

## Features

* Customer registration & login with email validation
* Table booking with 20-minute auto-release if not occupied
* Order creation, update, and bill generation
* Role-based employee handling: MANAGER, CHEF, WAITER, ADMIN
* Admin CRUD for menu (food items)
* Pending-item tracking for chefs and order status updates
* Daily sales reporting and billing/payment marking
* Thread-safe reservation handling using synchronization and ScheduledExecutorService

## Tech Stack

* Java 17+
* JDBC (PostgreSQL) for persistence
* PostgreSQL
* SLF4J (logging)
* Build: Maven (or Gradle if present)

## Prerequisites

* Java JDK 17 or newer
* PostgreSQL
* Git
* Recommended IDE: IntelliJ IDEA (or Eclipse / VS Code with Java support)

---

## Repository structure / Folder tree

## Project Structure (tree)

```
.
├── .idea
├── src
│   ├── main
│   │   ├── java/tech/zeta
│   │   │   ├── commandInterface   # Command interfaces / entry points
│   │   │   ├── exception          # Custom exceptions
│   │   │   ├── model              # Domain/data models (entities/DTOs)
│   │   │   ├── repository         # Data access layer (DAOs)
│   │   │   ├── service            # Business logic / service layer
│   │   │   └── utils              # Utility helpers
│   │   └── resources              # Config, application.properties, static resources
│   └── test/java/tech/zeta/repository  # Unit tests for repositories
├── .gitignore
└── pom.xml
```


---

## Database setup

1. Start PostgreSQL and create a database (e.g. `restaurant_db`).
2. From `src/main/resources/` run the `db_script.sql` to create tables and seed data.

Example (psql):

```bash
psql -U your_username -d restaurant_db -f src/main/resources/db_script.sql
```

## Configuration

Edit `src/main/resources/application.properties` and set your DB connection details:

```
database_url=jdbc:postgresql://localhost:5432/your_database_name
username=your_username
password=your_password
```

---

## Instructions to Run the Project

### 1. Clone GitHub Repository

Open your terminal/command prompt and run:

```bash
git clone https://github.com/Anees02/Restaurant-Management-System.git
cd Restaurant-Management-System
```

### 2. Database Setup

1. Navigate to the `src/main/resources/` folder.
2. Copy and execute the `db_script.sql` file in your PostgreSQL Terminal or PG Admin.

   * This will create all the required tables and schema.

### 3. Configure Database Connection

In the same `resources` folder, open the `application.properties` file and update it with your PostgreSQL credentials:

```
database_url = jdbc:postgresql://localhost:5432/your_database_name
username = your_username
password = your_password
```

### 4. Run the Application

1. Open the project in your IDE (IntelliJ IDEA / Eclipse / VSCode with Java support).
2. Locate the **Main** class in the package:

   ```
   tech.zeta.commandInterface.Main
   ```
3. Run the `Main` file directly (click the Play ▶ button in IntelliJ).
4. The command-line interface will launch and allow you to interact with the system.

---

## Design & Architecture (high level)

* **Layers:** Model → Repository (DAO) → Service → Command Interface (CLI)
* **Patterns:** Singleton (DB connection), DAO/Repository, synchronized blocks and ScheduledExecutorService for timed operations
* **Errors & Validation:** Custom exceptions (e.g., `CustomerNotFoundException`), email validation via regex, SLF4J logging


