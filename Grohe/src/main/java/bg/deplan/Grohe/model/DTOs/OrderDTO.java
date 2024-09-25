package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record OrderDTO(
        String artNum,
        int orderQuantity,
        String orderBy,
        LocalDate date,
//        String status,
        String orderReason,
        String comment
){
}
