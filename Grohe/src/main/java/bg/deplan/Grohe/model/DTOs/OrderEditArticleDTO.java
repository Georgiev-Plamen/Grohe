package bg.deplan.Grohe.model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record OrderEditArticleDTO(
        Long orderId,
        Long articleID,
        int index,
        String brand,
        String artNum,
        String quantity,
        String orderBy,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Match your frontend format
        LocalDate dateOfOrder,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Match your frontend format
        LocalDate dateOfDelivery,

        String orderReason,
        String comment
) {
}
