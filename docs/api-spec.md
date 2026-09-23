# API Specification - Add2Num Web

## 1. Tổng quan

API phục vụ bài học cộng hai số tự nhiên. Base path là `/api`. API hiện dùng HTTP và SSE; production phải đặt sau HTTPS. `jobId` là định danh ngẫu nhiên, không được dùng để suy ra danh tính học sinh.

## 2. Quy tắc chung

- `Content-Type` request form: `application/x-www-form-urlencoded`.
- Encoding: UTF-8.
- Thành công dùng HTTP 2xx; lỗi đầu vào dùng `400`, job không tồn tại dùng `404`, quá tải dùng `429`, lỗi bất ngờ dùng `500`.
- Response lỗi chuẩn: `{ "code": "VALIDATION_ERROR", "message": "...", "traceId": "..." }`.
- `message` dành cho người dùng phải ngắn, không chứa stack trace, tên class, SQL hoặc đường dẫn máy chủ.
- Server nên trả `Cache-Control: no-store` cho dữ liệu bài học và đặt giới hạn kích thước/độ dài input.

## 3. Bắt đầu phép cộng

### `POST /api/add`

Tạo phép tính bất đồng bộ và trả về ID để theo dõi.

Request:

```text
stn1=123&stn2=456
```

| Trường | Kiểu | Bắt buộc | Quy tắc |
| --- | --- | --- | --- |
| `stn1` | string | Có | Chuỗi chữ số `0-9`, không rỗng, trong giới hạn cấu hình |
| `stn2` | string | Có | Chuỗi chữ số `0-9`, không rỗng, trong giới hạn cấu hình |

Response `200 OK`:

```json
{ "jobId": "550e8400-e29b-41d4-a716-446655440000" }
```

Response lỗi mẫu:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Vui lòng nhập hai số tự nhiên hợp lệ.",
  "traceId": "trace-123"
}
```

## 4. Theo dõi tiến trình

### `GET /api/progress/{jobId}`

Mở kết nối `text/event-stream`. Client phải xử lý sự kiện theo thứ tự và dùng `EventSource` hoặc thư viện SSE tương đương.

Sự kiện tiến trình:

```text
event: progress
data: 42
```

`data` là số nguyên từ `0` đến `100`, không giảm. Khi hoàn tất thành công:

```text
event: result
data: 579
```

Khi thất bại:

```text
event: error
data: Không thể thực hiện phép tính.
```

Kết nối được đóng sau `result` hoặc `error`. Nếu `jobId` không tồn tại, server gửi `error` rồi đóng kết nối; hợp đồng HTTP chuẩn hóa được khuyến nghị là `404` trước khi mở stream. Client phải hiển thị trạng thái lỗi và cho phép thử lại, không tự động lặp vô hạn.

## 5. Trạng thái và vòng đời job

Trạng thái logic: `PENDING -> RUNNING -> COMPLETED` hoặc `PENDING/RUNNING -> FAILED`. Job hoàn tất phải có đúng một sự kiện kết thúc. Job chưa có client SSE vẫn phải lưu trạng thái cuối trong thời gian TTL cấu hình; job hết TTL phải bị xóa.

API không được xem `jobId` là cơ chế ủy quyền. Nếu bài học gắn với lớp/học sinh trong tương lai, phải bổ sung xác thực, phân quyền và kiểm tra quyền sở hữu job.

## 6. Tương thích và observability

Thay đổi breaking phải tăng version API, cập nhật tài liệu và có kế hoạch migration. Mỗi request/response cần `traceId` trong log; metric tối thiểu gồm số request, latency, lỗi theo mã, job đang chạy, job thất bại và số kết nối SSE.

Các tiêu chí chấp nhận API: kết quả đúng cho test oracle, input lỗi không kích hoạt job, progress đơn điệu, không rò rỉ lỗi nội bộ và request vượt giới hạn bị từ chối trước khi tiêu thụ tài nguyên đáng kể.
