package bg.deplan.Grohe.model.DTOs;

import bg.deplan.Grohe.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDTO(
        Long id,
        String brand,
        String orderName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,

        List<OrderItem> articleList)
{
}
