package bg.deplan.Grohe.model.DTOs;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderTitleDTO(

        Long id,
        String brand,
        String name,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Match your frontend format
        LocalDate date
){
}
