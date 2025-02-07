package bg.deplan.Grohe.model.DTOs;



import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PreOrderDTO (

        Long id,
        String brand,
        String artNum,
        String quantityForOrder,
        String orderBy,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.MM.yy") // Match your frontend format
        LocalDate date,

        String orderReason,
        String comment
){
}
