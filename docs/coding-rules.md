# Coding Rules - Add2Num Web

## 1. Mục đích và phạm vi

Tài liệu này quy định cách thiết kế, viết, kiểm tra và bàn giao phần mềm dạy học sinh tiểu học thực hiện phép cộng các số tự nhiên. Phạm vi gồm `add2num-core`, `add2num-web-app`, giao diện Thymeleaf, API Spring MVC và các bài kiểm thử.

Mục tiêu chất lượng là phần mềm dễ học, kết quả đúng, phản hồi dễ hiểu, có thể truy vết từ yêu cầu đến mã nguồn và kiểm thử. CMMI Level 5 được áp dụng như mô hình quản trị cải tiến dựa trên dữ liệu; tài liệu này không tuyên bố dự án đã được đánh giá/chứng nhận CMMI.

## 2. Nguyên tắc bắt buộc

1. **Đúng trước, nhanh sau:** mọi thay đổi phải giữ đúng phép cộng, xử lý số dài hơn kiểu nguyên thủy và không làm mất carry.
2. **Tách trách nhiệm:** controller chỉ nhận/trả HTTP; service điều phối job; core thực hiện phép cộng; view chỉ hiển thị và nhận tương tác.
3. **Tính xác định:** cùng đầu vào hợp lệ phải cho cùng kết quả, không phụ thuộc thread, locale hoặc trạng thái trình duyệt.
4. **Thiết kế cho trẻ em:** lỗi phải dùng ngôn ngữ ngắn, tích cực, không lộ stack trace; tiến trình phải dễ hiểu và kết quả phải đọc được.
5. **Không sửa trực tiếp nhánh chính:** mọi thay đổi đi qua branch, pull request, review và kiểm thử tự động.

## 3. Quy ước Java và Spring

- Dùng Java 22 theo `pom.xml`; mã nguồn UTF-8 và định dạng nhất quán.
- Tên class dùng `PascalCase`, method/biến dùng `camelCase`, hằng số dùng `UPPER_SNAKE_CASE`.
- Không dùng tên biến một ký tự, wildcard import hoặc magic number nếu có thể đặt tên hằng số.
- Dùng constructor injection; không dùng field injection.
- Method nên có một trách nhiệm rõ ràng. Controller không chứa thuật toán cộng hoặc truy cập trực tiếp vào `ConcurrentHashMap` job.
- DTO request/response phải có hợp đồng rõ ràng. Không trả entity nội bộ hoặc exception trực tiếp ra API.
- Không nuốt exception. Log ở biên xử lý với correlation/job ID, nhưng không log dữ liệu học sinh hoặc toàn bộ số đầu vào nếu không cần thiết.
- Executor phải có giới hạn tài nguyên, chính sách từ chối và cơ chế dọn job hết hạn trước khi dùng production. Không tạo thread pool không giới hạn cho request công khai.

## 4. Quy tắc thuật toán cộng

- Core nhận chuỗi số tự nhiên đã được chuẩn hóa và trả chuỗi kết quả.
- Từ chối chuỗi rỗng, khoảng trắng không được cho phép theo hợp đồng, ký tự ngoài `0-9`, dấu âm hoặc dữ liệu vượt giới hạn cấu hình.
- Thực hiện từ chữ số phải sang trái: `digit1 + digit2 + carry`; carry cuối cùng phải được thêm vào kết quả.
- Không chuyển sang `int`, `long` hoặc `double` để tính số có thể vượt miền biểu diễn.
- Bỏ số 0 ở đầu theo quy tắc đã công bố, nhưng vẫn giữ một chữ số `0` cho giá trị bằng không.
- Độ phức tạp mục tiêu là `O(max(len(a), len(b)))`; bộ nhớ phụ trợ là `O(max(len(a), len(b)))`.
- Callback tiến trình chỉ nhận giá trị trong `[0, 100]`, tăng đơn điệu và kết thúc ở `100` khi thành công.

## 5. Kiểm thử và chất lượng mã

Mỗi thay đổi phải có bằng chứng phù hợp:

| Loại thay đổi | Kiểm tra tối thiểu |
| --- | --- |
| Core/thuật toán | unit test số 0, số khác độ dài, carry liên tiếp, số rất dài, dữ liệu lỗi |
| Controller/API | test trạng thái HTTP, schema, dữ liệu lỗi và job không tồn tại |
| SSE/progress | test thứ tự sự kiện, kết thúc, lỗi và client kết nối muộn |
| View | test render, nhãn lỗi, khả năng sử dụng bàn phím và hiển thị mobile |
| Security/config | dependency scan, secret scan, kiểm tra giới hạn request và log |

Bộ dữ liệu phải bao gồm test biên và property-based test: với mọi `a`, `b` hợp lệ, kết quả phải bằng phép cộng tham chiếu trong miền kiểm thử; tính giao hoán phải đúng (`a+b=b+a`).

Không merge khi có test đỏ, lỗi build, cảnh báo bảo mật nghiêm trọng hoặc thiếu traceability ID. Mục tiêu kiểm soát nội bộ: 100% yêu cầu chức năng có test, 100% defect có regression test và không có lỗi Critical/High chưa được chấp nhận bằng văn bản.

## 6. Review, truy vết và đo lường

Pull request phải liên kết requirement ID, mô tả rủi ro, test đã chạy và kết quả. Ít nhất một reviewer độc lập với người viết phải duyệt; thay đổi core/security cần reviewer chuyên môn thứ hai.

Theo dõi hàng sprint/tháng: tỷ lệ build xanh, defect escape rate, thời gian sửa defect, tỷ lệ test pass, coverage theo module, tỷ lệ review đúng hạn và số lỗi theo nguyên nhân gốc. Baseline lấy từ tối thiểu ba chu kỳ; mục tiêu cải tiến phải là SMART và được đánh giá bằng biểu đồ xu hướng, không chỉ bằng cảm nhận.

## 7. Definition of Done

- Requirement, API và test được cập nhật.
- Mã đã format, build và test sạch trên Java/Maven được quy định.
- Có review và traceability.
- Không có secret, dữ liệu cá nhân hoặc log nhạy cảm.
- Tài liệu vận hành, giới hạn và thông báo người dùng đã được cập nhật nếu hành vi thay đổi.
- Kết quả kiểm thử được lưu cùng bản build/release để có thể audit.
