# Requirements Traceability Matrix

## 1. Document control

| Field | Value |
| --- | --- |
| Product | Add2Num Web |
| Document | Requirements Traceability Matrix |
| Version | 1.0 |
| Status | Proposed baseline for review |
| Source requirements | [software-requirements.md](software-requirements.md) |
| Technical design | [spec.md](spec.md) |
| API contract | [api-spec.md](api-spec.md) |

## 2. Status rules

Allowed statuses are:

- **Planned:** requirement is approved or designed but implementation/evidence is not complete.
- **Implemented:** relevant source code and/or test code exists, but release-level verification evidence is incomplete.
- **Verified:** passing test or approved review evidence is recorded. This status must not be assigned from source inspection alone.
- **Blocked:** verification or implementation is prevented by a known dependency, missing module, unresolved contract, or missing decision.
- **Not Applicable:** explicitly approved as outside the current release scope.

No requirement is marked `Verified` in this baseline because the Maven test suite cannot currently execute: the parent POM references `add2num-core`, but `add2num-core/pom.xml` is missing. `docs/openapi.yaml` is also absent; [api-spec.md](api-spec.md) is the available API contract.

## 3. Traceability matrix

| Requirement ID | Design section | Source file | Test case | Status | Evidence |
| --- | --- | --- | --- | --- | --- |
| FR-001 | [spec.md](spec.md#31-browser-and-thymeleaf-view), [spec.md](spec.md#51-start-calculation-post-apiadd) | [Add2NumController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/Add2NumController.java), `templates/index.html` | `Add2NumControllerTest.addReturnsJobId` | Implemented | Controller and API test source exist; test execution is blocked by missing core POM. |
| FR-002 | [spec.md](spec.md#71-input-rules) | `InputValidationService.java` is required but absent; [Add2NumController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/Add2NumController.java) has no server validator | `TC08`, `TC09`, `Add2NumControllerTest.addRejectsEmptyInput` | Blocked | Acceptance test exists, but production validation component is absent. |
| FR-003 | [spec.md](spec.md#52-subscribe-to-progress-get-apiprogressjobid) | `InputValidationService.java` is required but absent | `Add2NumControllerTest.addRejectsEmptyInput`, API validation tests | Blocked | Server-side validation is specified but not implemented. |
| FR-004 | [spec.md](spec.md#71-input-rules) | `InputValidationService.java` is required but absent | `TC10`, `Add2NumControllerTest.addRejectsInvalidCharacters` | Blocked | Invalid-character acceptance test exists; no validation implementation is present. |
| FR-005 | [spec.md](spec.md#82-error-mapping-requirements) | `ApiExceptionHandler.java` and `ApiErrorResponse.java` are required but absent | `TC08`, `TC09`, controller validation tests | Blocked | Safe Vietnamese error schema and no-job guarantee are not implemented. |
| FR-006 | [spec.md](spec.md#71-input-rules), [spec.md](spec.md#14-open-questions-and-assumptions) | Core implementation absent; leading-zero rule remains OPEN-002 | `TC04`, `MyBigNumberTest.addsZeroAndLeadingZeroValues` | Blocked | Behavior is unresolved and core source/POM is missing. |
| FR-010 | [spec.md](spec.md#36-core-addition-module) | `add2num-core/src/main/java/com/xuan/add2num/MyBigNumber.java` is absent | `TC01`, `TC04`, `TC06`, `TC07`, `MyBigNumberTest` | Blocked | Core module source is missing. |
| FR-011 | [spec.md](spec.md#36-core-addition-module) | `MyBigNumber.java` is absent | `TC02`, `TC03`, `MyBigNumberTest.addsMultipleCarries` | Blocked | Carry algorithm cannot be verified until core is buildable. |
| FR-012 | [spec.md](spec.md#36-core-addition-module) | `MyBigNumber.java` is absent | `TC07`, `MyBigNumberTest.addsVeryLargeNumbersWithoutOverflow` | Blocked | Large-number implementation is missing. |
| FR-013 | [spec.md](spec.md#62-invariants) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `Add2NumServiceTest.handlesConcurrentCalculationRequests` | Implemented | Service creates independent job IDs; execution evidence is unavailable. |
| FR-014 | [spec.md](spec.md#32-add2numcontroller), [spec.md](spec.md#36-core-addition-module) | [Add2NumController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/Add2NumController.java), [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `MyBigNumberTest`, controller tests | Implemented | Web/controller and core boundary is designed; core source is missing. |
| FR-015 | [spec.md](spec.md#36-core-addition-module) | No step-event model or implementation | Calculation progress section in [test-cases.md](test-cases.md) | Planned | Current contract exposes percentage progress only; column-by-column event schema is not defined. |
| FR-020 | [spec.md](spec.md#51-start-calculation-post-apiadd) | [Add2NumController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/Add2NumController.java), [StartResponse.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/dto/StartResponse.java), [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `Add2NumControllerTest.addReturnsJobId` | Implemented | Endpoint and DTO exist; Maven test execution is blocked. |
| FR-021 | [spec.md](spec.md#52-subscribe-to-progress-get-apiprogressjobid) | [Add2NumController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/Add2NumController.java) | `Add2NumControllerTest.progressRegistersEmitterForExistingJob` | Implemented | SSE endpoint exists and test source exists; no runtime evidence. |
| FR-022 | [spec.md](spec.md#53-event-contract) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `MyBigNumberTest.reportsMonotonicProgressFromZeroToOneHundred`, SSE service tests | Blocked | Progress depends on missing core implementation and has not been executed. |
| FR-023 | [spec.md](spec.md#62-invariants) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `Add2NumServiceTest.publishesProgressAndResultEvents` | Implemented | Result send/complete path exists; exact event verification is not yet evidenced. |
| FR-024 | [spec.md](spec.md#82-error-mapping-requirements) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | `Add2NumServiceTest.publishesErrorEventForCalculationFailure` | Implemented | Error path exists, but safe-message mapping and test execution are incomplete. |
| FR-025 | [spec.md](spec.md#62-invariants) | `JobStore.java`, timestamps and TTL fields are required but absent; [CalculationJob.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/progress/CalculationJob.java) has no TTL | Late-subscriber service test planned | Blocked | Late subscriber behavior is partially present but TTL is not implemented. |
| FR-026 | [spec.md](spec.md#62-invariants) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) retains jobs in `ConcurrentHashMap` | `Add2NumServiceTest.removesCompletedJobsAfterCleanupPolicy` | Blocked | Cleanup test exists; current service has no cleanup policy. |
| FR-027 | [spec.md](spec.md#31-browser-and-thymeleaf-view) | `templates/index.html`, client behavior | UI retry/state test planned | Planned | Browser behavior requires UI test evidence not present in current test suite. |
| FR-030..FR-036 | [spec.md](spec.md#31-browser-and-thymeleaf-view), [spec.md](spec.md#12-test-strategy) | `templates/index.html`, [PageController.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/controller/PageController.java) | Accessibility, keyboard, responsive and education-review tests planned | Planned | UI source exists, but required browser/accessibility evidence is absent. |
| NFR-001 | [spec.md](spec.md#122-acceptance-gates) | Core implementation absent | Core unit/property tests | Blocked | 100% oracle correctness cannot be established without buildable core. |
| NFR-002 | [spec.md](spec.md#62-invariants) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) | Service failure and cleanup tests | Blocked | Current implementation lacks explicit lifecycle/cleanup guarantees. |
| NFR-003 | [spec.md](spec.md#62-invariants) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java), [CalculationJob.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/progress/CalculationJob.java) | SSE ordering and terminal-event tests | Blocked | No atomic terminal transition or passing evidence exists. |
| NFR-010..NFR-012 | [spec.md](spec.md#93-performance-constraints) | [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java), core module absent | Load/resource tests, large-number tests | Blocked | No approved p95 baseline; executor is unbounded cached pool; core is missing. |
| NFR-020..NFR-024 | [spec.md](spec.md#10-security-controls) | No security configuration or centralized error handler; [application.properties](../add2num-web-app/src/main/resources/application.properties) only has application name | Security scan and abuse-case tests planned | Planned | Security requirements are documented but deployment/configuration evidence is absent. |
| NFR-030..NFR-033 | [spec.md](spec.md#31-browser-and-thymeleaf-view) | `templates/index.html` | Accessibility and usability tests planned | Planned | UI accessibility and education review have no recorded evidence. |
| NFR-040 | [spec.md](spec.md#13-traceability-to-fr-and-nfr-requirements) | This matrix, PR/release process | Matrix and release audit | Implemented | Traceability artifact exists; release evidence is not yet available. |
| NFR-041 | [spec.md](spec.md#11-observability-requirements) | No `TraceIdFilter` or metrics implementation found | Observability tests planned | Planned | Required logs/metrics/tracing are specified but not implemented. |
| NFR-042 | [spec.md](spec.md#9-threading-and-resource-management) | No `CalculationProperties` or bounded `ExecutorConfig` found; [Add2NumService.java](../add2num-web-app/src/main/java/com/xuan/add2num/web/service/Add2NumService.java) uses `newCachedThreadPool()` | Configuration and overload tests planned | Blocked | Required configurable limits are absent and no approved values exist. |
| NFR-043 | [spec.md](spec.md#15-change-control-and-completion-criteria) | Review process and pull request evidence | Independent review evidence | Planned | Process requirement cannot be verified from source files alone. |

## 4. API contract evidence

`docs/openapi.yaml` was requested as a source artifact but is not present in the workspace. The current traceability basis is [api-spec.md](api-spec.md), which defines:

- `POST /api/add` with `stn1` and `stn2`.
- `GET /api/progress/{jobId}` with `text/event-stream`.
- `progress`, `result`, and `error` events.
- HTTP error categories and the unresolved unknown-job behavior.

The API contract must not be marked `Verified` until an OpenAPI contract or an approved replacement is available and contract tests pass.

## 5. Evidence required to promote status

- Promote `Blocked` core rows only after `add2num-core/pom.xml`, production core source, and core tests build successfully.
- Promote validation rows only after server validation and safe error mapping are implemented.
- Promote SSE rows only after event name/payload/order/close assertions pass.
- Promote cleanup rows only after TTL and detached-emitter cleanup tests pass.
- Promote performance rows only after approved limits, load results, and p95 evidence are stored.
- Promote UI rows only after accessibility and education-review evidence is attached.
- Use `Verified` only when the evidence is linked to a reproducible test or approved review record.
