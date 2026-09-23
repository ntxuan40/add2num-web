# Security Rules - Add2Num Web

## 1. Mục đích

Tài liệu quy định kiểm soát bảo mật cho ứng dụng dạy phép cộng. Dữ liệu đầu vào có thể đến từ trẻ em nên ưu tiên bảo vệ an toàn, riêng tư và tính sẵn sàng. Các kiểm soát phải được kiểm tra trong pipeline và có người chịu trách nhiệm, bằng chứng, thời hạn xử lý.

## 2. Phân loại dữ liệu

- **Public:** mã nguồn đã phát hành, tài liệu công khai.
- **Internal:** log kỹ thuật, metric tổng hợp, cấu hình không nhạy cảm.
- **Sensitive:** job ID gắn với phiên học, dữ liệu lớp/học sinh, địa chỉ IP, token, thông tin xác thực.

Không lưu tên, email, ngày sinh hoặc thông tin nhận diện trẻ em trong job nếu không có yêu cầu nghiệp vụ, cơ sở pháp lý và phê duyệt bảo vệ dữ liệu. Dữ liệu nhạy cảm phải có TTL tối thiểu cần thiết, quyền truy cập theo nguyên tắc least privilege và audit log.

## 3. Bảo vệ request và API

- Production bắt buộc HTTPS, HSTS và cookie `Secure`, `HttpOnly`, `SameSite` nếu có session.
- Validate server-side: chỉ cho phép `0-9`, giới hạn độ dài mỗi số, giới hạn số job đồng thời và giới hạn tốc độ theo IP/session/user.
- Từ chối body quá lớn trước khi xử lý; trả `413` hoặc `429` phù hợp.
- Không dùng `jobId` làm secret hoặc cơ chế phân quyền. Khi có tài khoản, kiểm tra job thuộc về đúng user/lớp.
- Cấu hình CORS theo allowlist cụ thể; không dùng `*` cùng credentials.
- Bảo vệ CSRF cho các request state-changing nếu ứng dụng dùng cookie/session.
- SSE phải giới hạn số kết nối, xử lý disconnect, timeout hợp lý và dọn emitter/job; không để stream vô hạn tạo cạn tài nguyên.
- Không phản chiếu trực tiếp input vào HTML. Thymeleaf phải dùng escaping mặc định; không dùng unescaped HTML với dữ liệu người dùng.

## 4. Lỗi, log và bí mật

- Client chỉ nhận thông báo có thể hành động; server log lỗi chi tiết kèm `traceId`.
- Không log số đầy đủ, token, cookie, authorization header, dữ liệu học sinh hoặc stack trace trong response.
- Secret không được đặt trong source, `application.properties` commit vào repository, image hoặc log. Dùng secret store/environment injection.
- Log phải chống log injection, có timestamp, mức độ, trace/job ID đã được hạn chế và retention được phê duyệt.
- Thông báo lỗi không phân biệt quá chi tiết giữa job tồn tại/không tồn tại khi điều đó tạo khả năng dò quét.

## 5. Dependency và chuỗi cung ứng

Mỗi build phải chạy dependency vulnerability scan, secret scan và kiểm tra license theo chính sách tổ chức. Pin version dependency, review thay đổi lock/POM, dùng repository tin cậy và tạo SBOM cho release. Lỗ hổng Critical/High phải chặn release trừ khi có risk acceptance được phê duyệt, thời hạn và biện pháp giảm thiểu.

## 6. Kiểm thử bảo mật

Tối thiểu phải kiểm thử: input fuzzing số dài/ký tự Unicode, request thiếu trường, body lớn, rate limit, CORS/CSRF, XSS tại form/kết quả, truy cập job ID của phiên khác, SSE disconnect/reconnect, thread exhaustion, dependency scan và kiểm tra secret trong lịch sử Git.

Mọi phát hiện có severity, bằng chứng tái hiện, chủ sở hữu, SLA và regression test. Mục tiêu đo lường: không có secret bị commit mới, 100% release có scan bằng chứng, 100% lỗi đã đóng có xác nhận và xu hướng thời gian khắc phục giảm theo baseline.

## 7. Phản ứng sự cố và cải tiến CMMI Level 5

Khi phát hiện sự cố: cô lập ảnh hưởng, bảo toàn log, đánh giá phạm vi, thông báo theo quy định, khắc phục, xác minh và lập báo cáo nguyên nhân gốc. Không đổ lỗi cá nhân; dùng 5 Whys hoặc fishbone để tìm lỗi quy trình.

Hàng chu kỳ, nhóm phân tích dữ liệu scan, lỗi runtime, defect escape và sự cố SSE để chọn một cải tiến có thể đo được. Thử nghiệm cải tiến ở phạm vi nhỏ, so sánh với baseline/control group nếu phù hợp, kiểm tra hiệu quả và chuẩn hóa vào quy trình khi kết quả đạt mục tiêu. Các quyết định, metric và bằng chứng phải có thể audit.
