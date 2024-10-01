package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record ArticleDTO (
        String artNum,
        int quantityForOrder,

        String orderBy,
        LocalDate date,
        String orderReason,
        String comment) {

}



