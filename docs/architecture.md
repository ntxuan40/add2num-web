# Add2Num Web - Architecture

## 1. Overview

The Add2Num Web application follows a layered architecture.

The main objective is to separate presentation, web request handling, application logic, and the core addition algorithm.

## 2. Architecture Diagram

```text
                    +----------------+
                    |    Browser     |
                    | HTML/Thymeleaf |
                    +-------+--------+
                            |
                            | HTTP Request
                            v
                    +----------------+
                    |   Controller   |
                    |  Spring MVC    |
                    +-------+--------+
                            |
                            v
                    +----------------+
                    |    Service     |
                    | Application    |
                    |    Logic       |
                    +-------+--------+
                            |
                            | Call
                            v
                    +----------------+
                    | Add2Num Core   |
                    |    Task 1      |
                    |                |
                    | Addition Logic |
                    +----------------+
```

## 3. Components

### 3.1 Browser / View

The browser provides the user interface.

The user can:

* Enter the first number.
* Enter the second number.
* Execute the calculation.
* View validation messages.
* View calculation progress.
* View the final result.

Thymeleaf is used to render the server-side HTML.

### 3.2 Controller

The Controller is responsible for:

* Receiving HTTP requests.
* Receiving user input.
* Calling the application service.
* Returning the appropriate view.

The Controller does not contain the core addition algorithm.

### 3.3 Service

The Service layer handles the application workflow.

Responsibilities include:

* Validating input.
* Preparing the calculation.
* Calling Add2Num Core.
* Handling calculation progress.
* Returning the calculation result to the presentation layer.

### 3.4 Add2Num Core

Add2Num Core contains the addition algorithm implemented in Task 1.

Task 2 reuses this module instead of duplicating the calculation algorithm.

This provides a clear separation between:

```text
Web Application
      |
      v
Application Workflow
      |
      v
Core Calculation
```

## 4. Calculation Flow

The normal calculation flow is:

```text
User
 |
 | Enter two numbers
 v
Browser
 |
 | Submit
 v
Controller
 |
 v
Service
 |
 | Validate input
 |
 | Call Add2Num Core
 v
Add2Num Core
 |
 | Perform addition
 |
 | Generate calculation result
 v
Service
 |
 v
Controller
 |
 v
Thymeleaf View
 |
 v
Browser
```

## 5. Calculation Algorithm

The core addition algorithm processes numbers from right to left.

For each position:

```text
digit1 + digit2 + carry
```

The result digit is calculated and the carry is propagated to the next position.

The process continues until all digits have been processed and no carry remains.

The algorithm supports numbers whose length is greater than the range of primitive numeric types by processing the numbers as digit sequences.

## 6. Separation of Concerns

The application follows these responsibilities:

| Component    | Responsibility        |
| ------------ | --------------------- |
| Browser      | User interaction      |
| Thymeleaf    | HTML rendering        |
| Controller   | HTTP request handling |
| Service      | Application workflow  |
| Add2Num Core | Addition algorithm    |
| Test         | Verification          |

This structure makes the application easier to maintain and test.

## 7. Dependency Direction

The dependency direction is:

```text
Web
 |
 v
Service
 |
 v
Add2Num Core
```

The core calculation logic does not depend on the web layer.

Therefore, the core module can be reused by other applications if required.
