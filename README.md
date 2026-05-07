# Hospital Appointment Scheduling API

A secured, production-quality REST API for scheduling and managing hospital appointments — built with Spring Boot 3, Spring Security, and JWT authentication.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Lombok**
- **Jakarta Validation**
- **Maven**

---

## Features

### Security
- JWT authentication — stateless, no sessions
- BCrypt password encoding — passwords never stored as plain text
- Data isolation — each user accesses only their own appointments
- Ownership verification on all update and delete operations
- Duplicate email prevention on registration

### API
- Full CRUD for appointments
- Nested visit tracking per appointment
- Input validation with meaningful error messages
- Future date enforcement on appointment scheduling
- Email format validation on contact details

---

## API Endpoints

### Auth (Public — no token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login — returns JWT token |

### Appointments (JWT token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/appointments` | Get your appointments |
| POST | `/appointments` | Schedule new appointment |
| PUT | `/appointments/{id}` | Update your appointment |
| DELETE | `/appointments/{id}` | Cancel your appointment |

### Visits (JWT token required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/appointment/{appointmentId}/visit` | Get all visits for an appointment |
| POST | `/appointment/{appointmentId}/visit` | Log a new visit |
| PUT | `/appointment/{appointmentId}/visit/{id}` | Update a visit |
| DELETE | `/appointment/{appointmentId}/visit/{id}` | Delete a visit |

---

## How To Use

### Step 1 — Register
```json
POST /auth/register
{
    "name": "John Smith",
    "email": "john@gmail.com",
    "password": "pass123"
}
```

Response:
```
"User registered successfully"
```

### Step 2 — Login
```json
POST /auth/login
{
    "email": "john@gmail.com",
    "password": "pass123"
}
```

Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Step 3 — Use the token

Add to every request header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Step 4 — Schedule an appointment
```json
POST /appointments
Authorization: Bearer your_token

{
    "dateTime": "2026-06-15T10:30:00",
    "doctor": "Dr. Sarah Johnson",
    "service": "General Checkup",
    "email": "john@gmail.com",
    "phone": "5061234567"
}
```

### Step 5 — Log a visit
```json
POST /appointment/1/visit
Authorization: Bearer your_token

{
    "duration": "30 minutes",
    "price": 150,
    "notes": "Blood pressure checked, all normal"
}
```

---

## Validation Rules

### Appointment
- `dateTime` — required, must be a future date and time
- `doctor` — required, cannot be blank
- `service` — required, cannot be blank
- `email` — required, must be valid email format
- `phone` — required, cannot be blank

### Visit
- `duration` — required, cannot be blank
- `price` — required, must be a number
- `notes` — required, cannot be blank

---

## Security Design

```
HTTP Request
     ↓
JwtAuthFilter — validates token, sets user in SecurityContext
     ↓
SecurityFilterChain — checks authentication
     ↓
Controller — receives request, calls service
     ↓
Service — business logic + ownership verification
     ↓
Repository — database operations (MySQL)
     ↓
HTTP Response
```

Every appointment and visit operation verifies the resource belongs to the currently authenticated user before proceeding.

---

## Setup & Installation

### Prerequisites

- Java 17+
- MySQL 8+
- Maven

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/hospital-appointment-scheduling.git
cd hospital-appointment-scheduling
```

**2. Create MySQL database**
```sql
CREATE DATABASE hospitaldb;
```

**3. Configure application.properties**
```properties
spring.application.name=hospital-appointment-scheduling
spring.datasource.url=jdbc:mysql://localhost:3306/hospitaldb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
jwt.secret=YOUR_SECRET_KEY_HERE
```

**4. Run the project**
```bash
mvn spring-boot:run
```

Tables are created automatically on first run.

---

## Project Structure

```
src/main/java/org/example/hospitalappointmentschedulingapp/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AppointmentController.java
│   ├── AuthController.java
│   └── VisitController.java
├── filter/
│   └── JwtAuthFilter.java
├── model/
│   ├── Appointment.java
│   ├── User.java
│   └── Visit.java
├── repository/
│   ├── AppointmentRepository.java
│   ├── UserRepository.java
│   └── VisitRepository.java
└── service/
    ├── AppointmentService.java
    ├── AuthService.java
    ├── JwtService.java
    └── VisitService.java
```

---

## Key Technical Decisions

- **Stateless JWT** — no server-side sessions, scales horizontally
- **Constructor injection** — testable, immutable dependencies
- **`SecurityContextHolder`** — get current user anywhere without passing it around
- **`@ManyToOne` with `FetchType.LAZY`** — avoid unnecessary data loading
- **`@JsonIgnore`** on relationships — prevent infinite serialization loops
- **`CascadeType.ALL` + `orphanRemoval`** — deleting appointment removes all its visits automatically
- **`@Future`** validation — appointments cannot be scheduled in the past
- **Ownership verification** — every mutating operation checks the resource belongs to the current user
- **`orElseThrow`** — always throw meaningful exceptions, never return null
