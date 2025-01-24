package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record PreOrderExcelDTO (
    String artNum,
    Integer quantityForOrder,
    LocalDate date,
    String comment
){

}
