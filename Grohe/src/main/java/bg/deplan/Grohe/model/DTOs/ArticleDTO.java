package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record ArticleDTO (

        Long id,
        String artNum,
        String quantityForOrder,
        String artUrl,
        String orderBy,
        LocalDate date,
        String orderReason,
        String comment) {

}



