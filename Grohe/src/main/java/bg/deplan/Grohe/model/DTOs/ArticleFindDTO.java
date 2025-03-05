package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record ArticleFindDTO(
        String brand,
        LocalDate dateOfDelivery,
        LocalDate dateOfOrder,
        String orderBy,
        String orderReason,
        String quantity,
        long articleId,
        long orderID
) {
}
