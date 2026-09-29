# lib.mg-API

A robust backend API for a library management system, built with Java and Spring Boot. This application provides a comprehensive set of features for managing users, books, borrowing, and wishlists, secured with JWT-based authentication.

## Features

- **Authentication**: Secure user registration, login, and session management using JSON Web Tokens (JWT) with refresh token support.
- **User Management**: Full CRUD (Create, Read, Update, Delete) operations for users with role-based access control (Admin, User).
- **Book Catalog**: Manage a detailed book catalog, including CRUD operations.
- **Advanced Book Search**: A powerful search endpoint to filter books by a general query, title, author, genre, or ISBN, with support for pagination and sorting.
- **Borrowing System**: Functionality for users to borrow and return books, which automatically updates book availability.
- **Wishlist**: Allows users to add and remove books from their personal wishlist.
- **Health Check**: A simple endpoint to verify the API's operational status.

## Technologies Used

- **Backend**: Java 17, Spring Boot 3
- **Database**: PostgreSQL, Spring Data JPA (Hibernate)
- **Security**: Spring Security, JSON Web Tokens (JWT)
- **Build Tool**: Gradle
- **Deployment**: Docker, Render
- **Utilities**: Lombok, ModelMapper

## Getting Started

### Prerequisites

- Java JDK 17 or later
- Gradle
- A running PostgreSQL instance

### Local Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/reddevil212/lib.mg-api.git
    cd lib.mg-api
    ```

2.  **Configure Environment Variables:**
    Create a `.env` file in the root directory by copying the example file:
    ```sh
    cp .env.example .env
    ```
    Update the `.env` file with your specific configuration, such as your database credentials and a secure JWT secret.

    ```dotenv
    # Database Configuration
    SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>:5432/<DB_NAME>
    SPRING_DATASOURCE_USERNAME=<USERNAME>
    SPRING_DATASOURCE_PASSWORD=<PASSWORD>

    # JWT Security Configuration
    JWT_SECRET=<YOUR_32_BYTE_MIN_SECRET_KEY>
    JWT_EXPIRATION_MS=86400000
    JWT_REFRESH_EXPIRATION_MS=604800000

    # Server Port
    PORT=8080
    ```
    *Note: The application uses environment variables to configure database connections and JWT settings. The `.env` file is for local development and is ignored by Git.*

3.  **Run the application:**
    Use the Gradle wrapper to build and run the Spring Boot application.
    ```sh
    ./gradlew bootRun
    ```
    The API will be available at `http://localhost:8080`.

## API Endpoints

All endpoints require a `Bearer` token in the `Authorization` header, except for `/`, `/health`, and `/auth/**`.

### Health Check

- `GET /`: Checks if the server is up and running.

### Authentication (`/auth`)

- `POST /auth/register`: Register a new user.
- `POST /auth/login`: Authenticate a user and receive an access token and refresh token.
- `POST /auth/refresh`: Obtain a new access token using a valid refresh token.
- `POST /auth/logout/{userId}`: Log out a user by invalidating their refresh token.

### Users (`/users`)

- `GET /users`: Get a list of all users.
- `GET /users/{id}`: Get a specific user by their ID.
- `POST /users`: Create a new user.
- `PUT /users/{id}`: Update an existing user's details.
- `DELETE /users/{id}`: Delete a user.

### Books (`/books`)

- `GET /books`: Get a list of all books (defaults to a limit of 10).
- `GET /books/{id}`: Get a specific book by its ID.
- `POST /books`: Add a new book to the catalog.
- `PUT /books/{id}`: Update an existing book's details.
- `DELETE /books/{id}`: Remove a book from the catalog.
- `GET /books/search`: Search for books with various criteria.
    - **Query Parameters**: `query`, `title`, `author`, `genre`, `isbn`, `page`, `size`, `sortBy`, `sortDir`.

### Borrows (`/borrows`)

- `POST /borrows/user/{userId}/book/{bookId}`: Allow a user to borrow a book.
- `POST /borrows/{borrowId}/return`: Mark a borrowed book as returned.
- `GET /borrows/user/{userId}`: Get a list of books currently borrowed by a user.

### Wishlist (`/wishlist`)

- `POST /wishlist/user/{userId}/book/{bookId}`: Add a book to a user's wishlist.
- `DELETE /wishlist/user/{userId}/book/{bookId}`: Remove a book from a user's wishlist.
- `GET /wishlist/user/{userId}`: Get all items in a user's wishlist.

## Deployment

This project is configured for easy deployment on [Render](https://render.com/) using Docker. The repository includes:

- `Dockerfile`: A multi-stage Dockerfile that builds the application and creates a lean, production-ready image.
- `render.yaml`: A service configuration file for Render's "Blueprint" infrastructure-as-code feature.

When deploying, ensure you set the required environment variables (`SPRING_DATASOURCE_*`, `JWT_*`, etc.) in your hosting service's configuration. The `SPRING_JPA_HIBERNATE_DDL_AUTO` variable is set to `update` in the `render.yaml` to handle schema migrations on deployment.
