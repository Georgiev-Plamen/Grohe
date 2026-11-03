package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record PreOrderExcelDTO (
    String brand,
    String artNum,
    String quantityForOrder,
    String artNumDeplan,
    String artNameDeplan,
    LocalDate date,
    String comment
){

}
