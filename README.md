# Spaced Learning Reminder

A full-stack application for managing LeetCode problem reminders using spaced repetition learning technique.

## Tech Stack

- Frontend: React.js
- Backend: Spring Boot
- Database: MySQL
- Containerization: Docker

## Prerequisites

- Docker and Docker Compose installed on your system
- Java 21 (if running without Docker)
- Node.js 18+ (if running without Docker)
- MySQL 8.0 (if running without Docker)

## Running with Docker (Recommended)

1. Clone the repository:
```bash
git clone <repository-url>
cd spaced_learning_reminder
```

2. Build and start the containers:
```bash
docker-compose up --build
```

This will start three containers:
- MySQL database on port 3306
- Spring Boot backend on port 8080
- React frontend on port 80

3. Access the application:
- Frontend: http://localhost
- Backend API: http://localhost:8080
- MySQL: localhost:3306

## Running without Docker

### Backend (Spring Boot)

1. Navigate to the project root:
```bash
cd spaced_learning_reminder
```

2. Start the Spring Boot application:
```bash
./gradlew bootRun
```

### Frontend (React)

1. Navigate to the client directory:
```bash
cd client
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

## Configuration

The application uses the following environment variables:

### Backend Configuration
- `SPRING_DATASOURCE_URL`: JDBC URL for MySQL connection
- `SPRING_DATASOURCE_USERNAME`: MySQL username
- `SPRING_DATASOURCE_PASSWORD`: MySQL password
- `SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT`: Hibernate dialect

### MySQL Configuration
- `MYSQL_ROOT_PASSWORD`: Root password for MySQL
- `MYSQL_DATABASE`: Database name

## Troubleshooting

1. If port 8080 is already in use:
```bash
# Find the process using port 8080
lsof -i :8080

# Kill the process
kill <PID>
```

2. If Docker containers fail to start:
```bash
# Stop all containers
docker-compose down

# Remove all containers and volumes
docker-compose down -v

# Rebuild and start
docker-compose up --build
```

## Development

- The backend API is documented using Swagger UI at http://localhost:8080/swagger-ui.html
- The frontend is built using React with Material-UI components
- The database schema is automatically updated using Hibernate's `spring.jpa.hibernate.ddl-auto=update`

# spaced_learning_reminder
This app helps you redo leetcode problems with spaced repetition there by helping you recollect the intution and approach easily. The current repetition interval is 7/21/90/180/365 days. 

<img width="1195" alt="Screen Shot 2021-06-16 at 10 01 37 PM" src="https://user-images.githubusercontent.com/14796586/122334423-8c850c00-ceee-11eb-8d96-8bfdeecccbbe.png">

Future enhancements
* Customize repetition interval.
* Host the code on some public cloud or use Firebase for backend maybe ?
* Redo data model to support users and user preference settings.
* Add login and OAuth


