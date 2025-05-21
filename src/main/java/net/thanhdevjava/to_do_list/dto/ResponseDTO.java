package net.thanhdevjava.to_do_list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;

    // Tạo response thành công
    public static <T> ResponseDTO<T> success(String message, T data) {
        return ResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errorCode(null)
                .build();
    }

    // Tạo response lỗi
    public static <T> ResponseDTO<T> error(String message, String errorCode) {
        return ResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errorCode(errorCode)
                .build();
    }
}
