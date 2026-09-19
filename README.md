# Add2Num Web

## 1. Overview

Add2Num Web is a web application for adding two numbers.

The application provides a web interface where users can enter two numbers, execute the addition, view the calculation progress, and see the final result.

The calculation logic is provided by the Add2Num Core module developed in Task 1.

## 2. Objectives

The objectives of Task 2 are:

* Provide a web-based interface for adding two numbers.
* Reuse the Add2Num Core library from Task 1.
* Separate the web layer from the calculation logic.
* Display the calculation result.
* Display the calculation progress.
* Validate user input.
* Provide automated tests for the application.

## 3. Architecture

The application follows a layered architecture:

```text
+---------------------------+
|         Browser           |
|   HTML / Thymeleaf / UI   |
+-------------+-------------+
              |
              v
+---------------------------+
|        Controller         |
|      Spring MVC Layer     |
+-------------+-------------+
              |
              v
+---------------------------+
|         Service           |
|    Application Logic      |
+-------------+-------------+
              |
              v
+---------------------------+
|      Add2Num Core         |
|         Task 1            |
|   Addition Calculation    |
+---------------------------+
```

The web application is responsible for handling user requests, input validation, displaying calculation progress, and presenting the final result.

The actual addition algorithm is reused from Task 1.

## 4. Technology Stack

* Java
* Spring Boot
* Spring MVC
* Thymeleaf
* Bootstrap
* Maven
* JUnit
* Add2Num Core

## 5. Project Structure

```text
add2num-web/
├── pom.xml
├── README.md
├── docs/
│   ├── architecture.md
│   └── test-cases.md
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    └── test/
        └── java/
```

## 6. Dependency

Task 2 reuses the Add2Num Core library developed in Task 1.

The calculation logic is not duplicated in the web application.

This design provides:

* Code reuse
* Separation of concerns
* Reduced code duplication
* Easier maintenance
* Easier testing

## 7. Build

First, build and install the Add2Num Core module.

```bash
cd add2num-core
mvn clean install
```

Then build the Task 2 application.

```bash
cd add2num-web
mvn clean package
```

## 8. Run

Run the application using:

```bash
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/add2num-web-*.jar
```

After starting the application, open the application URL configured by the project.

For the default Spring Boot configuration:

```text
http://localhost:8080
```

## 9. Usage

1. Open the application in a web browser.
2. Enter the first number.
3. Enter the second number.
4. Execute the addition.
5. Check the calculation progress.
6. Check the final result.

## 10. Calculation Progress

The calculation is performed from the least significant digit to the most significant digit.

For each digit, the calculation considers:

* The current digit of the first number.
* The current digit of the second number.
* The carry from the previous calculation.

The application displays the calculation progress so that the user can understand how the final result is generated.

## 11. Input Validation

The application validates user input before performing the calculation.

Invalid input is rejected and an appropriate validation message is displayed to the user.

## 12. Testing

Run all automated tests using:

```bash
mvn test
```

The test cases cover the main addition scenarios and application behavior.

Detailed test cases are documented in:

```text
docs/test-cases.md
```

## 13. Design Decisions

### Reuse Task 1

The addition algorithm is implemented in Add2Num Core from Task 1.

Task 2 reuses this module instead of implementing the same algorithm again.

This keeps the calculation logic independent from the web layer.

### Separation of Responsibilities

The application separates responsibilities between:

* Controller: handles web requests.
* Service: handles application workflow.
* Core library: performs the addition.
* View: displays input, progress, result, and validation messages.

## 14. Error Handling

The application handles invalid user input before executing the calculation.

When invalid input is detected, the application displays an error message instead of executing the calculation.

## 15. Limitations

The current implementation focuses on the requirements defined for Task 2.

Authentication, authorization, database persistence, and other features outside the task requirements are not included.

## 16. Conclusion

Task 2 provides a web interface for the Add2Num application while reusing the calculation functionality implemented in Task 1.

The design separates the web application from the core calculation logic and provides input validation, calculation progress, result display, and automated testing.
