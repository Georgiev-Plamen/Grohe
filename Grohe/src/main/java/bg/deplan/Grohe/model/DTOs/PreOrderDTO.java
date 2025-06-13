package bg.deplan.Grohe.model.DTOs;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PreOrderDTO (

        Long id,
        String brand,
        String artNum,
        String quantityForOrder,
        String name,
        String orderBy,

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Match your frontend format
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Match your frontend format
        LocalDate date,

        String orderReason,
        String comment,
        boolean isHold
){
}
