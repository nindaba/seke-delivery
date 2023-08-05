package bi.seke.schema.common;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ErrorResponseDTO {
    @NonNull
    private Integer status;
    @NonNull
    private String method;
    @NonNull
    private String path;
    @NonNull
    private String error;
    private LocalDateTime timestamp = LocalDateTime.now();

}
