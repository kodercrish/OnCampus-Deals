# OnCampus-Deals

**GitHub Repository:** [https://github.com/kodercrish/OnCampus-Deals](OnCampus-Deals)

## Project Overview

**OnCampus Deals** is a robust, microservices-based marketplace application tailored for university campuses. It provides a secure and convenient platform for students to buy, sell, and trade items with their peers. The project enables communication among users, robust listing of items and easy discovery of items.

---

## 🏗️ Architecture

The project follows a modern decoupled/microservice architecture with a Spring Boot Microservices backend and a React Native frontend.

### Backend (Microservices)
The backend is built using **Java 21** and **Spring Boot 3.5.7**, organized into distinct services to ensure scalability and maintainability.

| Service | Directory | Description |
| :--- | :--- | :--- |
| **ApiGateway** | `backend/ApiGateway` | The single entry point for all client requests. Handles jwt authentication and routing to internal microservices using **Spring Cloud Gateway**. |
| **Identity Service** | `backend/Identity` | Manages user registration, authentication, and security using **JWT (JSON Web Tokens)**. |
| **Listing Service** | `backend/Listing` | Handles the core marketplace logic: creating, editing, fetching, and deleting product listings. Integrates with **Cloudinary** for image storage. |
| **Search Service** | `backend/Search` | Provides advanced search and filtering capabilities (by keyword, category, price range). |
| **Chat Service** | `backend/Chat` | Facilitates messaging between buyers and sellers. |

**Backend Tech Stack:**
*   **Language:** Java 21
*   **Framework:** Spring Boot 3.5.7, Spring Cloud
*   **Database:** MySQL (Production), H2 (Testing)
*   **Security:** Spring Security, JWT
*   **Build Tool:** Gradlew

### Frontend (Mobile Application)
The frontend is a cross-platform mobile application built with **React Native** and **Expo**.

**Frontend Tech Stack:**
*   **Framework:** React Native (Expo SDK 54)
*   **Language:** TypeScript
*   **State Management:** Redux Toolkit
*   **Data Fetching:** RTK Query
*   **Navigation:** React Navigation (Native Stack & Bottom Tabs)

---

## 🚀 Features

*   **User Authentication**: Secure Signup and Login functionality to verify user identities.
*   **Browse Feed**: A dynamic feed displaying active listings from across the campus.
*   **Advanced Search**: Filter items by category, price, or search by keywords to find specific deals.
*   **Create Listings**: Sellers can easily post items with titles, descriptions, prices, and images.
*   **listings Management**: Add listings, and mark items as "Sold" or "Unavailable".
*   **Chat**: Integrated chat feature allows buyers to contact sellers directly within the app.
---

## 🛠️ Setup Instructions

### Prerequisites
1.  **Java Development Kit (JDK) 21**
2.  **Node.js** (LTS version recommended) and **npm**
3.  **MySQL Server** (running locally)
4.  **Expo CLI** (`npm install -g expo-cli`)
5.  **Expo Go mobile app** (install from playstore)

### Backend Setup

1.  **Configure Database**:
    *   Ensure your MySQL server is running.
    *   Create a total of three databases (case sensitive) : **OCD_identity_db**, **OCD_listing_db** and **OCD_chat_db**
2.  **Environment Variables**:
    *   Check `src/main/resources/application-example.properties` in each service (Identity, Listing, etc.).
    *   Update database credentials (`spring.datasource.username`, `spring.datasource.password`).
    *   For the **Listing Service**, ensure you have your Cloudinary credentials configured.
3.  **Run Services**:
    *   One method is to open separate terminals for each service you want to start. Navigate to the service directory and run:

    ```bash
    # Example for Identity Service
    cd backend/identity
    ./gradlew bootRun
    ```

    *Repeat for Listing, Search, Chat, and finally __ApiGateway__.*

    ##### OR

    *   Second method (Recommended) is to run the shell script from the root directory by running the command :

    ```bash
    # make the file executable
    chmod +x start-all.sh

    # Start all servers
    ./start-all.sh
    ```

    After running the application, make sure you close all the servers either by killing all the processes by "kill -9 [PID]" or by running the below shell script:

    ```bash
    # make the file executable
    chmod +x stop-all.sh

    # close all servers
    ./stop-all.sh
    ```

    *   You can view the logs of each microservice inside the logs folder created in the root directory

### Frontend Setup

1.  **Navigate to Frontend**:
    ```bash
    cd frontend
    ```
2.  **Install Dependencies**:
    ```bash
    npm install
    ```
3.  **Configure API Endpoint**:
    *   Ensure the `API_BASE` in your env points to your running ApiGateway (e.g., `http://localhost:8080`, or in our case you will have to use a shared ip with your mobile).
4.  **Start the App**:
    *   Ensure that the port starts on port 8086 (as we have wrote the application.properties according to that in the backend)
    ```bash
    npx expo start --port 8086
    ```
5.  **Run on Device**:
    *   Scan the QR code displayed on the terminal after running the frontend from the Expo Go app downloaded in you mobile phone
    *   The shared ip will also be visible so you can update the backend application properties and also the frontend env to use that url for shared servers