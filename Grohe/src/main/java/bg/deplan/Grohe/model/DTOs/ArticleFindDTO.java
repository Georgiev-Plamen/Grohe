package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record ArticleFindDTO(
        Long id,
        String brand,
        LocalDate dateOfDelivery,
        LocalDate dateOfOrder,
        String orderBy,
        String orderReason,
        String quantity,
        String comment,
        String article,
        String orderName
) {
}
