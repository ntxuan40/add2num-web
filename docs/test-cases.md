# Add2Num Web - Test Cases

## 1. Purpose

This document describes the test scenarios used to verify the Add2Num Web application.

The tests verify calculation correctness, carry handling, input validation, and application behavior.

## 2. Functional Test Cases

| ID   | Scenario            |              Input 1 |              Input 2 |           Expected Result |
| ---- | ------------------- | -------------------: | -------------------: | ------------------------: |
| TC01 | Basic addition      |                  123 |                  456 |                       579 |
| TC02 | Addition with carry |                  999 |                    1 |                      1000 |
| TC03 | Multiple carries    |                 9999 |                 9999 |                     19998 |
| TC04 | Zero                |                    0 |                  123 |                       123 |
| TC05 | Same numbers        |                12345 |                12345 |                     24690 |
| TC06 | Different length    |                  123 |                 9999 |                     10122 |
| TC07 | Large numbers       | Large numeric string | Large numeric string | Correct calculated result |
| TC08 | Empty first input   |                Empty |                  123 |          Validation error |
| TC09 | Empty second input  |                  123 |                Empty |          Validation error |
| TC10 | Invalid characters  |                  abc |                  123 |          Validation error |

## 3. Calculation Progress

The calculation progress is verified by checking that the application processes the addition from the least significant digit to the most significant digit.

For example:

```text
  123
+ 456
-----
  579
```

The calculation starts from:

```text
3 + 6
```

Then:

```text
2 + 5 + carry
```

Then:

```text
1 + 4 + carry
```

The final result is:

```text
579
```

## 4. Carry Test

Carry propagation is an important part of the calculation.

Example:

```text
  999
+   1
-----
 1000
```

The test verifies that the carry is correctly propagated through multiple digits.

## 5. Validation Test

The application must reject invalid input.

Examples:

```text
Input 1: abc
Input 2: 123
```

Expected:

```text
Validation error
```

Another example:

```text
Input 1:
Input 2: 123
```

Expected:

```text
Validation error
```

## 6. Automated Test

Automated tests can be executed using:

```bash
mvn test
```

A successful test execution should complete without test failures.

## 7. Test Result

Before submission, execute:

```bash
mvn clean test
```

Confirm that all tests pass successfully.

The final test result should be recorded based on the actual test execution.
