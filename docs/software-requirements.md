# Software Requirements Specification (SRS)
# Phần mềm dạy học sinh tiểu học làm phép cộng

## 1. Thông tin kiểm soát tài liệu

| Trường | Giá trị |
| --- | --- |
| Tên sản phẩm | Add2Num Web |
| Tài liệu | Software Requirements Specification |
| Phiên bản | 1.0 |
| Trạng thái | Baseline đề xuất để review |
| Chủ sở hữu | Product Owner và Nhóm phát triển |
| Phạm vi | Web UI, API, service, add2num-core và kiểm thử |
| Tiêu chuẩn quản trị | CMMI-DEV Level 5 định hướng cải tiến dựa trên dữ liệu |

Tài liệu này là đặc tả yêu cầu và kế hoạch kiểm soát chất lượng theo thực hành CMMI Level 5. CMMI là khung cải tiến quy trình; tài liệu không tuyên bố tổ chức hoặc sản phẩm đã được thẩm định/chứng nhận CMMI.

## 2. Mục đích và mục tiêu sản phẩm

Add2Num Web giúp học sinh tiểu học luyện phép cộng các số tự nhiên thông qua nhập hai số, quan sát tiến trình cộng từ phải sang trái và xem kết quả. Phần mềm phải ưu tiên tính đúng, phản hồi dễ hiểu, an toàn dữ liệu trẻ em và khả năng đo lường kết quả học tập.

Mục tiêu sản phẩm:

- Học sinh hiểu được vai trò của từng chữ số và phép nhớ (carry), không chỉ nhận một kết quả cuối.
- Học sinh có thể thực hành độc lập trên máy tính hoặc thiết bị di động.
- Giáo viên/phụ huynh có thể dùng phần mềm làm công cụ luyện tập, nhưng sản phẩm không tự suy đoán năng lực hoặc gắn nhãn học sinh.
- Nhóm phát triển có thể truy vết mỗi yêu cầu đến thiết kế, mã nguồn, kiểm thử và bằng chứng phát hành.
- Chất lượng được cải tiến bằng dữ liệu thực tế, phân tích nguyên nhân gốc và thử nghiệm cải tiến có kiểm soát.

## 3. Đối tượng liên quan và vai trò

| Vai trò | Nhu cầu và trách nhiệm |
| --- | --- |
| Học sinh | Nhập số, xem hướng dẫn, theo dõi từng bước, nhận phản hồi tích cực và dễ hiểu |
| Giáo viên/phụ huynh | Giao bài, quan sát việc sử dụng ở mức được cho phép, hỗ trợ học sinh |
| Product Owner | Xác định mục tiêu học tập, ưu tiên backlog và chấp nhận yêu cầu |
| Giáo viên/Chuyên gia giáo dục | Xác nhận nội dung, ngôn ngữ và độ phù hợp lứa tuổi |
| Nhóm phát triển | Thiết kế, lập trình, kiểm thử, bảo mật và bảo trì |
| QA/Independent Reviewer | Xác minh độc lập, kiểm soát chất lượng và bằng chứng truy vết |
| Vận hành | Triển khai, giám sát, sao lưu, phản ứng sự cố và quản lý cấu hình |
| Phụ trách bảo vệ dữ liệu | Phê duyệt việc thu thập, lưu giữ và xử lý dữ liệu trẻ em |

## 4. Phạm vi

### 4.1 Trong phạm vi

- Giao diện web nhập hai số tự nhiên và hiển thị phép cộng.
- Kiểm tra dữ liệu đầu vào ở client và server.
- Cộng các số có thể dài hơn miền của `int`/`long` bằng xử lý chuỗi chữ số.
- Hiển thị tiến trình từ chữ số hàng đơn vị đến hàng cao nhất.
- API tạo job và kênh SSE trả progress, result hoặc error theo [api-spec.md](api-spec.md).
- Unit test, integration test, accessibility test, security test và regression test.
- Metric kỹ thuật, dữ liệu sử dụng tối thiểu và cơ chế cải tiến quy trình.

### 4.2 Ngoài phạm vi phiên bản đầu

- Phép trừ, nhân, chia, phân số hoặc số âm.
- Chấm điểm chính thức, xếp hạng, quảng cáo hoặc hồ sơ hành vi chi tiết.
- Đăng nhập tài khoản, quản lý lớp và báo cáo giáo viên đầy đủ.
- Thu thập thông tin nhận diện trẻ em khi chưa có yêu cầu, phê duyệt pháp lý và thiết kế quyền riêng tư tương ứng.

## 5. Giả định và ràng buộc

- Học sinh đã được giới thiệu số tự nhiên và ký hiệu phép cộng.
- Input phiên bản đầu chỉ gồm chuỗi chữ số ASCII `0-9`; không nhận số âm.
- Kết quả được trả dưới dạng chuỗi để không phụ thuộc giới hạn kiểu số nguyên.
- Ứng dụng dùng Spring MVC/Thymeleaf, service bất đồng bộ và SSE như kiến trúc hiện tại.
- Mọi giới hạn độ dài, số job đồng thời, TTL và rate limit phải là cấu hình có thể kiểm soát, không phải giá trị ẩn trong code.
- Khi có xung đột giữa tính năng và an toàn trẻ em, an toàn, riêng tư và tính đúng được ưu tiên.

## 6. Quy trình và tiêu chí chất lượng theo CMMI Level 5

Mỗi yêu cầu phải có ID duy nhất, nguồn gốc, tiêu chí nghiệm thu, mức ưu tiên, rủi ro và liên kết đến thiết kế, mã nguồn, test case và bằng chứng phát hành. Thay đổi yêu cầu phải được đánh giá tác động trước khi đưa vào baseline.

CMMI Level 5 được áp dụng qua chu trình:

1. Đo baseline về defect, build, hiệu năng, accessibility, bảo mật và chỉ số sử dụng.
2. Phân tích xu hướng và nguyên nhân gốc bằng dữ liệu từ tối thiểu ba chu kỳ khi đủ dữ liệu.
3. Chọn một cải tiến có mục tiêu SMART, người chịu trách nhiệm và thời hạn.
4. Thử nghiệm ở phạm vi nhỏ hoặc theo nhóm kiểm soát phù hợp.
5. Đo trước/sau, xác nhận hiệu quả thống kê hoặc theo ngưỡng đã phê duyệt.
6. Chuẩn hóa cải tiến hiệu quả vào quy trình và cập nhật baseline.

Mục tiêu kiểm soát tối thiểu:

- 100% yêu cầu mức Must có ít nhất một test xác minh.
- 100% defect đã đóng có kiểm tra hồi quy phù hợp.
- 0 lỗi Critical/High chưa có biện pháp xử lý được phê duyệt khi phát hành.
- 100% release có kết quả build, test, dependency scan và security review được lưu.
- Defect escape rate, thời gian sửa lỗi và tỷ lệ lỗi lặp lại được theo dõi theo xu hướng.

## 7. Yêu cầu chức năng

### 7.1 Nhập và chuẩn hóa dữ liệu

| ID | Yêu cầu | Ưu tiên |
| --- | --- | --- |
| FR-001 | Hệ thống phải cho phép nhập số thứ nhất và số thứ hai vào hai trường riêng biệt. | Must |
| FR-002 | Hệ thống phải chỉ chấp nhận chuỗi không rỗng gồm các chữ số `0-9`, trong giới hạn độ dài cấu hình. | Must |
| FR-003 | Hệ thống phải kiểm tra dữ liệu ở server ngay cả khi client đã kiểm tra. | Must |
| FR-004 | Hệ thống phải từ chối input có chữ cái, dấu âm, ký tự điều khiển, số thập phân, khoảng trắng không được quy định hoặc vượt giới hạn. | Must |
| FR-005 | Khi input không hợp lệ, hệ thống phải hiển thị thông báo bằng tiếng Việt, ngắn gọn, không đổ lỗi cho học sinh và không tạo calculation job. | Must |
| FR-006 | Hệ thống phải xử lý số 0 và số có các chữ số 0 ở đầu theo một quy tắc nhất quán được công bố trong giao diện hoặc đặc tả. | Should |

### 7.2 Thực hiện phép cộng

| ID | Yêu cầu | Ưu tiên |
| --- | --- | --- |
| FR-010 | Hệ thống phải tính đúng tổng của hai số tự nhiên hợp lệ, kể cả khi số chữ số vượt miền kiểu nguyên thủy. | Must |
| FR-011 | Thuật toán phải xử lý từ hàng đơn vị sang hàng cao hơn và truyền carry chính xác. | Must |
| FR-012 | Kết quả phải được biểu diễn dưới dạng chuỗi số, không bị làm tròn, tràn số hoặc mất chữ số. | Must |
| FR-013 | Cùng một cặp input hợp lệ phải cho cùng một kết quả bất kể số lần chạy hoặc thứ tự thời gian của các job. | Must |
| FR-014 | Hệ thống phải tách thuật toán cộng khỏi controller và giao diện để có thể kiểm thử độc lập. | Must |
| FR-015 | Hệ thống nên cung cấp diễn giải từng cột: hai chữ số, carry hiện tại, chữ số kết quả và carry kế tiếp. | Should |

### 7.3 Tiến trình và kết quả

| ID | Yêu cầu | Ưu tiên |
| --- | --- | --- |
| FR-020 | Khi request hợp lệ, hệ thống phải tạo một `jobId` duy nhất và trả về cho client. | Must |
| FR-021 | Client phải có thể mở `/api/progress/{jobId}` để nhận sự kiện SSE. | Must |
| FR-022 | Sự kiện `progress` phải là số nguyên từ 0 đến 100, không giảm và không vượt giới hạn. | Must |
| FR-023 | Khi thành công, hệ thống phải phát đúng một sự kiện `result`, hiển thị tổng và đóng stream. | Must |
| FR-024 | Khi lỗi, hệ thống phải phát sự kiện `error` với thông báo an toàn và đóng stream. | Must |
| FR-025 | Hệ thống phải xử lý client kết nối sau khi job đã hoàn thành bằng cách gửi trạng thái cuối trong TTL. | Should |
| FR-026 | Job đã hết TTL phải được dọn khỏi bộ nhớ hoặc kho lưu trữ theo chính sách lưu giữ. | Must |
| FR-027 | Client phải hiển thị trạng thái đang tính, hoàn tất, lỗi và thao tác thử lại; không lặp request vô hạn. | Must |

### 7.4 Tính năng học tập và khả năng sử dụng

| ID | Yêu cầu | Ưu tiên |
| --- | --- | --- |
| FR-030 | Giao diện phải giải thích rằng phép cộng được thực hiện từ phải sang trái bằng ngôn ngữ phù hợp học sinh tiểu học. | Must |
| FR-031 | Giao diện phải làm nổi bật chữ số hoặc cột đang được xử lý mà không che khuất input, carry hoặc kết quả. | Should |
| FR-032 | Sau mỗi bài, học sinh phải có thể thực hiện bài mới mà không cần tải lại toàn bộ trang. | Should |
| FR-033 | Thông báo lỗi phải đặt gần trường gây lỗi, có nhãn rõ ràng và không chỉ dùng màu để truyền đạt trạng thái. | Must |
| FR-034 | Người dùng phải thao tác được bằng bàn phím; focus order, label và trạng thái điều khiển phải rõ ràng. | Must |
| FR-035 | Giao diện phải đáp ứng màn hình di động và không yêu cầu thao tác kéo ngang cho luồng chính. | Must |
| FR-036 | Sản phẩm không được hiển thị quảng cáo, nội dung gây xao nhãng hoặc thông báo mang tính phán xét học sinh. | Must |

## 8. Yêu cầu phi chức năng

### 8.1 Tính đúng và tin cậy

| ID | Yêu cầu đo được |
| --- | --- |
| NFR-001 | Với bộ test oracle được phê duyệt, tỷ lệ kết quả đúng phải là 100%. |
| NFR-002 | Mọi lỗi xử lý phải chuyển sang trạng thái kết thúc rõ ràng, không để job chạy vô hạn hoặc stream không được giải phóng. |
| NFR-003 | Progress phải đơn điệu và sự kiện kết thúc phải xuất hiện không quá một lần cho mỗi job. |

### 8.2 Hiệu năng và khả năng mở rộng

| ID | Yêu cầu đo được |
| --- | --- |
| NFR-010 | Với kích thước input trong giới hạn cấu hình, p95 thời gian tạo job phải được xác định bằng baseline và không vượt ngưỡng release đã phê duyệt. |
| NFR-011 | Hệ thống phải giới hạn số job và SSE connection đồng thời; khi quá tải phải trả lỗi có thể xử lý thay vì cạn thread/bộ nhớ. |
| NFR-012 | Thuật toán phải có độ phức tạp mục tiêu `O(max(n, m))` và không chuyển toàn bộ số dài sang kiểu số nguyên giới hạn. |

### 8.3 Bảo mật và riêng tư

| ID | Yêu cầu đo được |
| --- | --- |
| NFR-020 | Production phải dùng HTTPS, HSTS và cấu hình CORS theo allowlist. |
| NFR-021 | Input phải có giới hạn kích thước, rate limit và validation server-side. |
| NFR-022 | Không đưa stack trace, secret, token, dữ liệu nhận diện trẻ em hoặc số input đầy đủ vào response/log không cần thiết. |
| NFR-023 | Dependency scan, secret scan và security regression test phải hoàn tất trước release. |
| NFR-024 | `jobId` không được dùng thay cho xác thực/phân quyền nếu hệ thống sau này lưu dữ liệu theo học sinh hoặc lớp. |

### 8.4 Khả năng sử dụng và tiếp cận

| ID | Yêu cầu đo được |
| --- | --- |
| NFR-030 | 100% trường nhập có label chương trình đọc màn hình và thông báo lỗi liên kết đúng trường. |
| NFR-031 | Luồng nhập, bắt đầu, theo dõi và xem kết quả phải dùng được bằng bàn phím. |
| NFR-032 | Nội dung hướng dẫn phải được giáo viên hoặc chuyên gia giáo dục review trước release. |
| NFR-033 | Văn bản hiển thị phải phù hợp lứa tuổi, ngắn, không dùng thuật ngữ kỹ thuật như `job`, `SSE` hoặc `exception`. |

### 8.5 Bảo trì và quan sát

| ID | Yêu cầu đo được |
| --- | --- |
| NFR-040 | Mỗi yêu cầu Must phải truy vết đến test case và bằng chứng chạy test. |
| NFR-041 | Log/metric phải có trace ID, mã lỗi, latency, job đang chạy, job thất bại và số kết nối SSE ở mức không chứa dữ liệu nhạy cảm. |
| NFR-042 | Có thể cấu hình giới hạn input, TTL, rate limit và executor mà không sửa thuật toán core. |
| NFR-043 | Pull request phải có review độc lập; thay đổi core hoặc security cần review chuyên môn bổ sung. |

## 9. Giao diện ngoài và hợp đồng API

- `POST /api/add` nhận `stn1`, `stn2` dưới dạng form-urlencoded và trả `jobId` khi input hợp lệ.
- `GET /api/progress/{jobId}` trả `text/event-stream` với các event `progress`, `result` hoặc `error`.
- Chi tiết mã HTTP, schema lỗi, vòng đời job và quy tắc tương thích nằm trong [api-spec.md](api-spec.md).
- Giao diện trình duyệt phải dùng thông báo thân thiện; chi tiết kỹ thuật chỉ xuất hiện trong log nội bộ có kiểm soát.

## 10. Mô hình dữ liệu tối thiểu

| Đối tượng | Trường tối thiểu | Lưu ý |
| --- | --- | --- |
| CalculationJob | `jobId`, `status`, `progress`, `result`, `errorCode`, `createdAt`, `expiresAt` | Không lưu danh tính học sinh nếu không cần |
| CalculationRequest | `stn1`, `stn2`, `traceId` | Input phải được validate trước khi tạo job |
| CalculationEvent | `jobId`, `eventType`, `progress/result`, `occurredAt` | Không ghi toàn bộ input vào telemetry mặc định |

## 11. Tiêu chí nghiệm thu tổng thể

Một phiên bản được chấp nhận khi tất cả điều kiện sau đúng:

1. Toàn bộ test Must pass, gồm test đúng/sai, carry, số dài, input lỗi, concurrency cơ bản và SSE.
2. Mọi yêu cầu Must trong ma trận truy vết có thiết kế, implementation reference và test evidence.
3. Không còn defect Critical/High chưa được xử lý hoặc risk acceptance hợp lệ.
4. Kiểm thử accessibility đạt tiêu chí đã nêu; người review giáo dục chấp nhận nội dung hướng dẫn.
5. Build reproducible, dependency/secret scan hoàn tất và artifact có version.
6. Có hướng dẫn vận hành, rollback, giám sát, TTL/rate limit và xử lý sự cố.
7. Product Owner ký chấp nhận sau khi xem báo cáo test và các metric release.

## 12. Ma trận truy vết yêu cầu

| Requirement group | Thiết kế/mã nguồn cần liên kết | Kiểm thử tối thiểu |
| --- | --- | --- |
| FR-001..006 | View, validation service, controller | UI validation, API validation |
| FR-010..015 | `add2num-core`, service | Unit/property test core |
| FR-020..027 | Controller, job service, SSE emitter | Integration/concurrency/SSE test |
| FR-030..036 | Thymeleaf template, accessibility behavior | UI, keyboard, responsive, education review |
| NFR-001..012 | Core, executor, configuration | Correctness, load, resource-limit test |
| NFR-020..024 | Web security/configuration, logging | Security scan and abuse-case test |
| NFR-030..043 | UI standards, CI/CD, observability | Accessibility, review, pipeline evidence |

Ma trận phải được duy trì trong pull request và cập nhật khi requirement, thiết kế hoặc test thay đổi. Không được đánh dấu “đã hoàn tất” chỉ dựa trên việc mã đã compile.

## 13. Quản lý thay đổi và baseline

Mọi change request phải ghi lý do, requirement bị ảnh hưởng, tác động đến học tập, bảo mật, hiệu năng, API, dữ liệu, test và kế hoạch rollback. Product Owner quyết định ưu tiên; QA xác nhận phương án kiểm thử; phụ trách bảo vệ dữ liệu duyệt các thay đổi liên quan dữ liệu trẻ em.

Baseline được tạo tại mỗi release. Thay đổi breaking API, thay đổi thuật toán hoặc thay đổi dữ liệu lưu giữ phải có review kiến trúc và cập nhật [architecture.md](architecture.md), [api-spec.md](api-spec.md), [test-cases.md](test-cases.md) và tài liệu security liên quan.

## 14. Rủi ro và biện pháp kiểm soát

| Rủi ro | Tác động | Kiểm soát |
| --- | --- | --- |
| Sai carry hoặc tràn số | Kết quả học sai | Core độc lập, property test, số rất dài và regression test |
| Input quá lớn tạo cạn tài nguyên | Mất sẵn sàng | Giới hạn độ dài, rate limit, bounded executor, load test |
| Lộ dữ liệu trẻ em | Vi phạm riêng tư | Data minimization, TTL, access control, log redaction và review bảo vệ dữ liệu |
| SSE disconnect hoặc job tồn đọng | Trải nghiệm sai/tràn bộ nhớ | Timeout, cleanup, TTL, metric và test reconnect |
| Nội dung không phù hợp lứa tuổi | Giảm hiệu quả học | Review chuyên gia giáo dục, usability test và đo phản hồi |
| Lỗi lặp lại sau release | Tăng chi phí sửa | Root-cause analysis, regression test, trend review và process improvement |

## 15. Bằng chứng cần lưu cho mỗi release

- Requirement baseline và ma trận truy vết.
- Biên bản review, change request và quyết định chấp nhận rủi ro.
- Kết quả build, unit/integration/UI/security/accessibility test.
- Dependency scan, secret scan, SBOM nếu tổ chức yêu cầu.
- Báo cáo metric, defect và nguyên nhân gốc.
- Artifact đã version, release note, hướng dẫn rollback và phê duyệt Product Owner.

## 16. Definition of Done cho yêu cầu

Một yêu cầu chỉ hoàn thành khi:

- Tiêu chí nghiệm thu được viết rõ và đã được Product Owner xác nhận.
- Thiết kế, mã nguồn và test đã liên kết bằng requirement ID.
- Test tự động và kiểm tra thủ công cần thiết đã pass.
- Tài liệu/API/security/architecture được cập nhật nếu bị ảnh hưởng.
- Code review, security review và education review đã hoàn tất theo loại thay đổi.
- Có bằng chứng build/release và không còn blocker hoặc defect Critical/High chưa được chấp nhận.
