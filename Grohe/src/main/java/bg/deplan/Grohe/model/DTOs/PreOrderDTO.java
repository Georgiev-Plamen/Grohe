package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record PreOrderDTO (
        String article,
        int quantity,
        String orderBy,
        LocalDate date,
//        String status,
        String orderReason,
        String comment
){
}
