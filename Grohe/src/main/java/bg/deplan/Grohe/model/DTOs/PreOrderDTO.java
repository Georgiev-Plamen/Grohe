package bg.deplan.Grohe.model.DTOs;

import bg.deplan.Grohe.model.Article;

import java.time.LocalDate;
import java.util.List;

public record PreOrderDTO (

        String artNum,

        int quantityForOrder,
        String orderBy,
        LocalDate date,
        String orderReason,
        String comment
){
}
