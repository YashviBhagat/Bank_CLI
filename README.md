# Bank of CLI

A terminal-based banking application built with a layered architecture that supports secure accounts, deposits, withdrawals, transaction history, and atomic money transfers.

---

## Features

- **Secure access** — register and log in with a unique 4-digit account ID and a PIN
- **Balance management** — check your current balance at any time
- **Transaction engine**
  - **Deposit** — add funds to an account
  - **Withdraw** — remove funds, with overdraw prevention
  - **Transfer** — move money between two accounts as a single atomic operation
- **Audit trail** — view your recent transaction history
- **Logging** — every action is recorded to a log file (INFO for successes, ERROR for failures)

---

## Architecture

The application follows a strict three-layer architecture. Each layer has a single responsibility and only communicates with the layer directly below it.

| Layer | Responsibility |
|-------|----------------|
| **API Layer** | Terminal menus, reads user input, prints output |
| **Service Layer** | Banking rules — validation, overdraw checks, atomic transfers |
| **Repository Layer** | All SQL access via raw JDBC; maps rows to Java objects |
| **Database** | PostgreSQL (runs in Docker) — stores accounts and transactions |

A request flows down through the layers; data returns up as `Account` and `Transaction` objects.

---

## Tech Stack

- **Language:** Java 17
- **Build tool:** Maven
- **Database:** PostgreSQL (via Docker)
- **Database access:** JDBC
- **Testing:** JUnit 5, Mockito
- **Logging:** SLF4J + Logback
- **Version control:** Git & GitHub

---

## Project Structure

```
com.bankcli
├── api/          # BankREPL — terminal menu and user interaction
├── business/     # AccountService, TransactionService — banking rules
├── repository/   # AccountDAO, TransactionDAO, ConnectionFactory — data access
├── domain/       # Account, Transaction — data objects
└── App.java      # Entry point — wires the layers together
```

---

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker

### 1. Start the database

The PostgreSQL database runs in a Docker container:

```bash
docker compose up -d
```

This creates the `bankcli` database and user automatically.

### 2. Set up the schema

Create the tables (run once):

```bash
docker exec -it bankcli-db psql -U bankcli -d bankcli -f /path/to/schema.sql
```

*(Or run the `CREATE TABLE` statements from `schema.sql` manually.)*

### 3. Configure the connection

Create `src/main/resources/db.properties`:

```properties
DB_URL=jdbc:postgresql://localhost:5432/bankcli
DB_USER=bankcli
DB_PASSWORD=bankcli
```

### 4. Build and run

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.bankcli.App"
```

---

## Usage

Once running, follow the terminal menu:

```
=== Bank of CLI ===
1. Register
2. Login
3. Exit
```

After logging in:

```
1. Check Balance
2. Withdraw
3. Deposit
4. Transfer
5. History
6. Logout
```

---

## Testing

The project follows a "2-Test Rule" — each service and repository method has at least one positive test (proving it works) and one negative test (proving it handles errors gracefully).

Run the tests with:

```bash
mvn test
```

---

## Author

Yashvi Bhagat