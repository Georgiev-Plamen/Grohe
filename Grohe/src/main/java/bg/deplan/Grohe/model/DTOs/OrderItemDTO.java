package bg.deplan.Grohe.model.DTOs;


import bg.deplan.Grohe.model.Article;

import java.time.LocalDate;

public record OrderItemDTO(

        Long orderId,
        Article article,
        String quantity,
        String comments,
        String orderBy,
        String orderReason,
        LocalDate dateOfOrder,
        LocalDate dateOfDelivery,
        Long userId,
        int position
) {
}
