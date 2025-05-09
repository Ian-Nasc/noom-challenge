#  Sleep Logger API

A RESTful API for logging and retrieving sleep data, developed for potential integration with the Noom web interface.

## 📋 Functional Requirements

The API supports:

1. **Creating a sleep log for the last night**, including:
    - Date of sleep (derived from start datetime)
    - Time in bed interval (start and end)
    - Total time in bed (in minutes)
    - Sleep quality (one of: `BAD`, `OK`, `GOOD`)

2. **Fetching the most recent sleep log for a user**

3. **Fetching 30-day sleep averages for a user**, including:
    - Date range of the data
    - Average time in bed (minutes)
    - Average time going to bed and waking up
    - Frequency of sleep qualities reported

---

## 🏗️ Tech Stack

- **Java 21**
- **Spring Boot**
- **PostgreSQL**
- **Flyway** (DB migrations)
- **JUnit** (unit testing)

---
## Notes to take
- I changed the requirement #1.1: "The date of the sleep (today)" to "Date of sleep (derived from start datetime)" for the sake of the testing part to be easier.

---

## 🧾 Database Schema

Managed via Flyway, the database includes two main tables:

### `users`

| Field | Type |
|-------|------|
| `id` | Long |
| `name` | String |

### `sleep_log`

| Field | Type |
|-------|------|
| `id` | Long |
| `sleep_date` | LocalDate |
| `time_in_bed_start` | LocalTime |
| `time_in_bed_end` | LocalTime |
| `total_time_in_bed` | Long |
| `sleep_quality` | Enum (`BAD`, `OK`, `GOOD`) |
| `user_id` | Foreign Key to `users` |

---

## 🔗 API Endpoints

### 🔹 Create a User

**POST** `/api/users`

Create a new user.

#### Request Body:

```json
{
  "name": "Ian",
}
```

### 🔹 Create a Sleep Log

**POST** `/api/sleep-logs`

Create a new sleep log.

#### Request Body:

```json
{
  "userId": 3,
  "timeInBedStart": "2025-05-17T22:00:00",
  "timeInBedEnd": "2025-05-18T06:00:00",
  "sleepQuality": "GOOD"
}
```

### 🔹 GET last sleeplog

**GET** `/api/sleep-logs/last/{userId}`

GET last sleep log from a given user.

#### EXAMPLE of response:

```json
{
    "sleepDate": "2025-05-17",
    "timeInBedStart": "22:00:00",
    "timeInBedEnd": "06:00:00",
    "totalTimeInBedMinutes": 480,
    "sleepQuality": "GOOD"
}

