package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record OrderDTO(
        String artNum,
        int quantityForOrder,
        String orderBy,
        LocalDate date,
        String orderReason,
        String comment
){
}
