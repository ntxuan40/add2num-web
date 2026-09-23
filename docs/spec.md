# Add2Num Web API Submodule - Technical Specification

## 1. Document control

| Field | Value |
| --- | --- |
| Product | Add2Num Web |
| Document | Technical Specification for API Submodule |
| Version | 1.0 |
| Status | Proposed baseline for architecture and implementation review |
| Source of truth | `docs/software-requirements.md` and `docs/api-spec.md` |
| Technology constraint | Java 22, Maven, Spring MVC, Thymeleaf, Server-Sent Events |
| Process context | AI-Native SDLC with CMMI Level 5-oriented measurement and improvement |

This document translates product requirements into a technical design. It does not claim that the current implementation already satisfies every requirement. Statements are labeled as follows:

- **CURRENT:** observed in the repository at specification time.
- **REQUIRED:** must be implemented or verified before the related requirement is accepted.
- **OPEN:** a decision or evidence is still missing and must be resolved through change control.

## 2. Technical scope

### 2.1 In scope

The API submodule provides the server-side workflow for adding two natural-number strings:

1. Receive `stn1` and `stn2` through `POST /api/add`.
2. Create a calculation job and return a `jobId`.
3. Execute the addition asynchronously through the core calculation module.
4. Publish progress, result, or error through `GET /api/progress/{jobId}` using SSE.
5. Validate input and protect resources before creating work.
6. Expose safe, stable HTTP/SSE behavior for the browser client.
7. Produce testable boundaries, operational metrics, and traceable release evidence.

The submodule must not add subtraction, multiplication, division, user accounts, class management, scoring, ranking, advertising, or student profiling because those features are outside the version-one scope.

### 2.2 Current implementation baseline

- **CURRENT:** `Add2NumController` is mapped to `/api`.
- **CURRENT:** `POST /api/add` accepts request parameters named `stn1` and `stn2` and returns a `StartResponse` containing `jobId`.
- **CURRENT:** `GET /api/progress/{jobId}` returns `text/event-stream` through `SseEmitter`.
- **CURRENT:** `Add2NumService` stores jobs in `ConcurrentHashMap`, executes work with `Executors.newCachedThreadPool()` and sends `progress`, `result`, or `error` events.
- **CURRENT:** `CalculationJob` stores progress, completion, result, error, and an emitter.
- **CURRENT:** the web module references `com.xuan.add2num.MyBigNumber` from `add2num-core`.
- **BLOCKED:** the repository baseline inspected for this document does not contain `add2num-core/pom.xml` or visible core source, so full Maven validation and core behavior cannot yet be verified.

### 2.3 Target state required before release

- **REQUIRED:** the core module is buildable and independently tested.
- **REQUIRED:** server-side validation occurs before job creation.
- **REQUIRED:** input length, concurrent work, SSE connections, TTL, and rate limits are bounded by configuration.
- **REQUIRED:** executor behavior is bounded and has a rejection policy.
- **REQUIRED:** job cleanup occurs after expiry and emitter disconnect.
- **REQUIRED:** error responses follow the documented safe error model.
- **REQUIRED:** trace ID, latency, job, error, and SSE metrics are available without sensitive data.
- **REQUIRED:** HTTPS, HSTS, CORS, CSRF, and deployment security controls are applied according to the deployment model.

## 3. Architecture and component responsibilities

The target dependency direction is:

```text
Browser / Thymeleaf
        |
        v
HTTP Controller / SSE Adapter
        |
        v
Application Service / Job Coordinator
        |
        v
Core Addition Algorithm
```

### 3.1 Browser and Thymeleaf view

- **Responsibility:** collect two numbers, start a calculation, subscribe to progress, and display a child-friendly explanation and result.
- **Must not:** calculate the authoritative result, bypass server validation, display stack traces, or expose technical terms such as `job`, `SSE`, or `exception` to children.
- **Traceability:** FR-001..006, FR-027, FR-030..036, NFR-030..033.

### 3.2 `Add2NumController`

- **Responsibility:** map HTTP requests to application service operations and map service outcomes to documented HTTP/SSE responses.
- **Must not:** contain digit arithmetic, direct executor management, direct job-map manipulation, or presentation-only business rules.
- **Required boundary:** controller accepts the public parameter names `stn1` and `stn2`, preserves the endpoint paths, and delegates validation/workflow to the service or a validation component.
- **Traceability:** FR-003, FR-020..024, NFR-040, NFR-043.

### 3.3 Input validation component

- **Responsibility:** validate non-empty ASCII digit strings, configured maximum length, and the supported natural-number rules before job creation.
- **Output:** a typed validation outcome or a domain validation exception that can be mapped to the documented safe error response.
- **Must not:** log full input values or create a job for rejected input.
- **Traceability:** FR-002..006, NFR-021..022.

### 3.4 `Add2NumService` / job coordinator

- **Responsibility:** create and track job state, submit bounded work, call the core algorithm, forward progress, finalize exactly once, and clean up resources.
- **Required state discipline:** state changes must be visible across worker and request threads; terminal state must be idempotent.
- **Traceability:** FR-013, FR-020..027, NFR-002..003, NFR-011, NFR-042.

### 3.5 `CalculationJob`

- **Responsibility:** represent the minimum state required to resume a late SSE subscriber and deliver a terminal outcome.
- **Required target fields:** `jobId`, status, progress, result or safe error code, creation time, expiry time, and emitter lifecycle state.
- **Privacy constraint:** do not add student identity or full input values unless a separately approved requirement and data-protection design exist.
- **Traceability:** FR-025..026, NFR-022, NFR-024.

### 3.6 Core addition module

- **Responsibility:** add two validated numeric strings from right to left, propagate carry, return a numeric string, and report monotonic progress.
- **Dependency rule:** core must not depend on Spring MVC, HTTP, Thymeleaf, SSE, or web classes.
- **Performance rule:** target complexity is `O(max(n, m))` with no conversion to bounded primitive numeric types.
- **Traceability:** FR-010..015, NFR-001, NFR-012.

### 3.7 Error and observability boundary

- **Responsibility:** map expected domain failures to safe public errors and unexpected failures to generic errors while retaining controlled internal diagnostics.
- **Must not:** return exception messages, stack traces, secrets, tokens, full inputs, or infrastructure paths to clients.
- **Traceability:** FR-005, FR-024, NFR-022, NFR-041.

## 4. Target package structure

The package structure below follows the existing Java package and separates web concerns from core concerns. It is a target design, not a claim that every package currently exists.

```text
add2num-core/
  pom.xml
  src/main/java/com/xuan/add2num/
    MyBigNumber.java
    AdditionProgress.java                 # OPEN: only if core needs a typed progress model
  src/test/java/com/xuan/add2num/
    MyBigNumberTest.java

add2num-web-app/
  src/main/java/com/xuan/add2num/web/
    Add2numWebApplication.java
    controller/
      Add2NumController.java
      PageController.java
      ApiExceptionHandler.java            # REQUIRED for standardized errors
      dto/
        StartResponse.java                # May remain a record
        ApiErrorResponse.java             # REQUIRED
    service/
      Add2NumService.java
      InputValidationService.java         # REQUIRED unless validation is clearly owned elsewhere
    progress/
      CalculationJob.java
      JobStatus.java                      # REQUIRED typed lifecycle state
      JobStore.java                       # REQUIRED abstraction if storage/cleanup is separated
    config/
      CalculationProperties.java          # REQUIRED externalized limits
      ExecutorConfig.java                 # REQUIRED bounded executor
      OpenApiConfig.java                  # REQUIRED only when Swagger/OpenAPI is introduced
    observability/
      TraceIdFilter.java                  # REQUIRED if not provided by platform
      CalculationMetrics.java             # REQUIRED for NFR-041
  src/main/resources/
    application.properties
    templates/index.html
  src/test/java/com/xuan/add2num/web/
    controller/Add2NumControllerTest.java
    service/Add2NumServiceTest.java
    progress/CalculationJobTest.java
```

**OPEN:** decide whether DTOs remain nested records or move to separate packages. The decision must preserve the public JSON contract and be recorded in an architecture decision record.

## 5. API request and response flow

### 5.1 Start calculation: `POST /api/add`

1. Client sends `application/x-www-form-urlencoded` with `stn1` and `stn2`.
2. Request filter establishes or propagates a non-sensitive `traceId`.
3. Controller delegates both values to the application service.
4. Validation component checks non-empty ASCII digits and configured length before any job is created.
5. If invalid, the error handler returns HTTP `400` using the documented safe error schema.
6. If valid, the service generates a unique `jobId`, creates a `PENDING` job, and submits bounded work.
7. Controller returns HTTP `200` with `{ "jobId": "..." }`.
8. Worker transitions the job to `RUNNING`, calls core addition, publishes progress, and transitions to `COMPLETED` or `FAILED`.

**CURRENT gap:** the current controller delegates directly to `startCalculation`; the visible service does not show the required server-side validation boundary or standardized error handler. These are **REQUIRED** before accepting FR-002..005 and NFR-021..022.

### 5.2 Subscribe to progress: `GET /api/progress/{jobId}`

1. Client opens an SSE connection with the returned `jobId`.
2. Controller asks the service to register the emitter.
3. Service looks up the job without exposing the internal job store.
4. Unknown job returns the documented not-found behavior. The exact behavior for an SSE response must be resolved under the open item below.
5. Known job receives current progress. If the job is already terminal, the client receives the terminal event and the stream closes.
6. While running, progress events are published in non-decreasing order from `0` to `100`.
7. On success, exactly one `result` event is sent and the stream closes.
8. On failure, exactly one safe `error` event is sent and the stream closes.
9. Disconnect, timeout, terminal completion, and TTL expiry release emitter/job resources.

### 5.3 Event contract

```text
event: progress
data: 42

```

`progress` data is an integer in `[0, 100]`.

```text
event: result
data: 579

```

`result` data is the exact decimal result string.

```text
event: error
data: Không thể thực hiện phép tính.

```

`error` data is safe for a child-facing client. Internal exception details remain server-side.

## 6. Calculation job lifecycle

### 6.1 State model

```text
PENDING -> RUNNING -> COMPLETED
                    \-> FAILED
```

- `PENDING`: validated request has created a job, but worker execution has not started.
- `RUNNING`: core calculation is executing and may publish progress.
- `COMPLETED`: result is available, final progress is `100`, and one result event may be sent.
- `FAILED`: safe error state is available and one error event may be sent.
- Expiry is a retention action after a terminal state, not a user-visible calculation state.

### 6.2 Invariants

1. Invalid input never creates a job.
2. Every created job has one unique ID.
3. A job has at most one terminal transition.
4. Progress is an integer in `[0, 100]` and never decreases.
5. Successful completion stores the result and progress `100` before terminal notification.
6. Failed completion stores a safe error code/message and does not expose exception text.
7. A late subscriber receives the latest state within TTL.
8. Expired jobs and disconnected emitters are eligible for cleanup.
9. A rejected submission does not leave a permanently `PENDING` job.

**CURRENT gap:** the visible implementation has `completed`, `result`, and `error` fields but no explicit status, timestamps, TTL cleanup, or bounded submission policy. These controls are **REQUIRED**.

## 7. Validation rules

### 7.1 Input rules

| Rule | Required behavior | Status |
| --- | --- | --- |
| Parameter names | Preserve `stn1` and `stn2` | CURRENT |
| Presence | Both values are required and non-empty | REQUIRED |
| Character set | ASCII digits `0-9` only | REQUIRED |
| Sign/decimal | Reject negative signs and decimal separators | REQUIRED |
| Whitespace | Reject unless a separately approved normalization rule exists | REQUIRED |
| Length | Enforce configurable maximum per input | REQUIRED |
| Leading zeroes | Define one consistent normalization rule; retain `0` for zero | OPEN |
| Job creation | Do not create a job for invalid input | REQUIRED |
| Client checks | Client-side checks improve UX only and never replace server validation | REQUIRED |

### 7.2 Progress rules

- Core callback values must be clamped or rejected if outside `[0, 100]`; silently emitting invalid values is not acceptable.
- Progress must be monotonic.
- Successful computation emits `100` before the terminal result event.
- Progress events must not contain input values, stack traces, or sensitive metadata.

## 8. Error handling

### 8.1 Public error categories

| Condition | HTTP/event behavior | Public message policy |
| --- | --- | --- |
| Missing or invalid `stn1`/`stn2` | HTTP `400` | Short Vietnamese validation message |
| Input exceeds configured limit | HTTP `400` or `413`, per approved contract | Explain the permitted limit without infrastructure details |
| Rate/concurrency limit exceeded | HTTP `429` | Ask user to retry later |
| Unknown `jobId` | HTTP `404` or SSE error/close behavior | Must be resolved consistently |
| Core/domain calculation failure | SSE `error`; HTTP start request remains successful if failure is asynchronous | Generic safe message |
| Unexpected server failure | HTTP `500` or SSE `error` | Generic safe message plus `traceId` where appropriate |
| Client disconnect | No noisy public error | Release resources and record a metric |

### 8.2 Error mapping requirements

- **REQUIRED:** introduce one centralized mapping boundary such as `ApiExceptionHandler` for synchronous API failures.
- **REQUIRED:** define an error response containing `code`, `message`, and `traceId` where the HTTP contract requires it.
- **REQUIRED:** map internal exceptions to stable public codes; do not expose `Exception.getMessage()` directly.
- **REQUIRED:** log internal diagnostic details with controlled access and redaction.
- **OPEN:** confirm whether the SSE unknown-job case returns HTTP `404` before stream creation or an SSE `error` event after stream creation. `api-spec.md` currently describes both as possible and therefore needs one final contract.

## 9. Threading and resource management

### 9.1 Required model

- Request threads validate and enqueue work; they must not perform unbounded arithmetic inline.
- A bounded executor processes calculation jobs.
- The executor must define core/max pool size, queue capacity, keep-alive policy, thread naming, shutdown behavior, and rejection behavior through configuration.
- Rejection must return a controlled overload response and must not leave a job in `PENDING` forever.
- Shared job state must use thread-safe publication and atomic/idempotent terminal transitions.
- SSE sending must handle `IOException`, client disconnect, timeout, and completion.
- A cleanup mechanism must remove expired jobs and detached emitters.
- Application shutdown must stop accepting new work, complete or cancel pending work according to policy, close emitters, and shut down the executor.

### 9.2 Current gap

The current code uses `Executors.newCachedThreadPool()` and `new SseEmitter(0L)`. This does not demonstrate bounded work or a finite SSE timeout, so it is **NOT ACCEPTED for production** until limits, cleanup, and shutdown behavior are implemented and tested.

### 9.3 Performance constraints

- Core target complexity: `O(max(len(stn1), len(stn2)))`.
- No conversion of large input to `int`, `long`, or `double`.
- p95 latency, maximum input length, concurrent jobs, and concurrent SSE connections must have approved baseline values before release.
- Load testing must demonstrate behavior at the configured limits and overload response under saturation.

## 10. Security controls

### 10.1 Request and transport

- **REQUIRED:** HTTPS in production, HSTS, safe response headers, and CORS allowlist.
- **REQUIRED:** server-side validation, body/input limits, rate limiting, and bounded concurrency.
- **REQUIRED:** CSRF protection if authentication/session cookies are introduced.
- **REQUIRED:** Thymeleaf escaping and no unescaped reflection of input into HTML.
- **OPEN:** authentication and authorization are outside version-one scope. If jobs become associated with students/classes, authorization and job ownership checks must be designed before storing that data.

### 10.2 Data and logging

- Do not store student identity, full input, tokens, cookies, or authorization headers unless explicitly required and approved.
- Do not put full input values, sensitive identifiers, or stack traces in normal logs or metrics.
- Use a non-sensitive trace ID and job ID for correlation.
- Apply TTL to job data and document retention/deletion evidence.
- Secrets must come from environment/secret management, not committed configuration.

### 10.3 Security verification

Before release, run input fuzzing, oversized request tests, XSS checks, rate-limit tests, CORS/CSRF checks where applicable, SSE disconnect tests, thread-exhaustion tests, dependency scan, secret scan, and regression tests for every fixed vulnerability.

Traceability: NFR-020..024, FR-002..005, FR-026, NFR-011, NFR-022.

## 11. Observability requirements

### 11.1 Logs

Structured logs should include timestamp, severity, operation, outcome, trace ID, job ID where available, duration, and safe error code. They must exclude full numbers, student identity, tokens, cookies, and stack traces in public responses.

### 11.2 Metrics

Minimum metrics:

- HTTP request count by route and outcome.
- HTTP latency, including p50/p95/p99 where available.
- Validation failures by safe reason code.
- Jobs created, running, completed, failed, rejected, and expired.
- Active SSE connections and disconnects.
- Executor queue depth, active threads, rejection count, and task duration.
- Core calculation duration by configured input-size bucket, without recording the number itself.
- Defect escape rate, test pass rate, security findings, and mean time to repair for process improvement.

### 11.3 Traces and alerts

- Propagate a trace ID from HTTP request to job execution and terminal event logs.
- **REQUIRED:** alert on sustained executor saturation, job growth without cleanup, abnormal failure rate, SSE connection growth, and p95 latency breach.
- **OPEN:** choose the organization-standard metrics/tracing backend and retention period; do not add a vendor dependency until approved.

Traceability: NFR-010..011, NFR-040..042.

## 12. Test strategy

### 12.1 Test layers

| Layer | Scope | Required evidence |
| --- | --- | --- |
| Core unit | Digit addition, carry, zero, different lengths, very long strings, invalid data | Unit test report |
| Core property | Oracle equality and commutativity for generated valid inputs | Property-test report |
| Service unit | Job lifecycle, callback, failure, late subscriber, cleanup | Unit test report |
| Controller/API | Parameters, status, response schema, invalid input, unknown job | Mock MVC/integration report |
| SSE | Event names, ordering, monotonic progress, terminal event, close, disconnect | SSE test report |
| Resource/load | Bounded queue, rejection, concurrent jobs, cleanup and overload | Load/resource report |
| Security | Input fuzzing, rate limit, CORS/CSRF where applicable, XSS, logs/secrets | Security scan and test report |
| Accessibility/UI | Keyboard, labels, focus, responsive view, child-friendly messages | Accessibility/usability report |
| Regression | Every fixed defect gets a permanent regression test | Defect record and test link |

### 12.2 Acceptance gates

A release candidate is not accepted until:

1. All Must requirements have at least one passing verification.
2. Correctness oracle results are 100% for the approved test set.
3. Invalid input creates no job.
4. Terminal event occurs no more than once per job.
5. No critical/high security finding remains without approved risk acceptance.
6. Build, tests, dependency scan, secret scan, and required review evidence are stored.
7. Education/content review and accessibility checks pass.

## 13. Traceability to FR and NFR requirements

| Requirement IDs | Technical design section | Primary implementation area | Verification |
| --- | --- | --- | --- |
| FR-001..006 | Section 7 | Controller, validation component, view | API validation and UI tests |
| FR-010..015 | Sections 3.6, 6 | `add2num-core`, service callback | Core unit/property tests |
| FR-020..024 | Sections 5, 6, 8 | `Add2NumController`, job coordinator, SSE adapter | API/SSE integration tests |
| FR-025..027 | Sections 5, 6, 9 | Job store, emitter lifecycle, client view | Late-subscription, cleanup, UI tests |
| FR-030..036 | Sections 3.1, 12 | Thymeleaf templates and browser behavior | Accessibility and education review |
| NFR-001..003 | Sections 6, 12 | Core and job state transitions | Correctness and concurrency tests |
| NFR-010..012 | Sections 9, 11 | Executor, configuration, core algorithm | Load/resource/performance tests |
| NFR-020..024 | Section 10 | Transport, validation, logging, deployment | Security tests and scans |
| NFR-030..033 | Sections 3.1, 12 | View and content | Accessibility/usability review |
| NFR-040..043 | Sections 8, 11, 14 | CI, review, observability, traceability | Release evidence audit |

Status rule: a row is not `Verified` merely because the corresponding class exists. It requires a passing test or approved review evidence linked to the requirement ID.

## 14. Open questions and assumptions

### 14.1 Open questions

| ID | Question | Decision owner | Impact if unresolved |
| --- | --- | --- | --- |
| OPEN-001 | What are the maximum lengths for `stn1` and `stn2`? | Product Owner / Architect | Validation, memory, performance, load tests |
| OPEN-002 | Should leading zeroes be normalized before calculation, and how should they be displayed? | Product Owner / Education reviewer | FR-006, UI and result expectations |
| OPEN-003 | Does unknown `jobId` return HTTP `404` before SSE creation or an SSE `error` event? | API owner | Client behavior and contract tests |
| OPEN-004 | What exact p95 latency, concurrency, TTL, and rate-limit thresholds are approved? | Product Owner / Operations | NFR-010, NFR-011 and capacity plan |
| OPEN-005 | Which metrics/tracing backend and retention period are approved? | Operations / Security | NFR-041 and deployment design |
| OPEN-006 | What is the shutdown policy for pending/running jobs? | Operations / Architect | Executor lifecycle and data consistency |
| OPEN-007 | Is authentication intentionally excluded from version one, including deployment behind a trusted gateway? | Product Owner / Security | Job ownership and threat model |
| OPEN-008 | Should the API return HTTP `200` or `202 Accepted` when a job is created? | API owner | API compatibility and client contract |
| OPEN-009 | What exact child-facing wording and visual explanation are approved by an education reviewer? | Education reviewer | FR-030..036 and usability acceptance |
| OPEN-010 | When and where should completed jobs be deleted, and is in-memory storage sufficient for the deployment topology? | Architect / Operations | FR-025..026, restart behavior, scaling |

### 14.2 Assumptions

- ASSUMP-001: version one handles only non-negative natural numbers represented as ASCII digit strings.
- ASSUMP-002: no student identity is required for the basic calculation workflow.
- ASSUMP-003: the browser can consume standard SSE events.
- ASSUMP-004: the core algorithm remains a reusable module independent of the web module.
- ASSUMP-005: a deployment gateway may provide some transport controls, but application-level validation and resource limits remain mandatory.
- ASSUMP-006: CMMI Level 5 language describes the process and measurement intent, not a certification claim.

## 15. Change control and completion criteria

Any change to an endpoint, parameter name, event name, validation rule, job state, retention rule, security control, or performance limit requires:

1. A change request with reason and affected FR/NFR IDs.
2. Impact analysis for code, API, UI, tests, security, data, and operations.
3. Review by the responsible owner; core/security changes require independent specialist review.
4. Updates to `software-requirements.md`, `api-spec.md`, this document, tests, and traceability evidence when affected.
5. A passing focused validation before broader validation.

The specification is ready for implementation when OPEN items affecting API behavior, limits, data retention, or security have approved decisions. The submodule is ready for release only when all Must requirements are verified, the `add2num-core` module builds, and the evidence package is complete.
