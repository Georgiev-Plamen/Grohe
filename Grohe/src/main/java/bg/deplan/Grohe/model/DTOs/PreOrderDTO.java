package bg.deplan.Grohe.model.DTOs;

import bg.deplan.Grohe.model.Article;

import java.time.LocalDate;

public record PreOrderDTO (

        Long id,
        String artNum,
        Integer quantityForOrder,
        String orderBy,
        LocalDate date,
        String orderReason,
        String comment
){
}
