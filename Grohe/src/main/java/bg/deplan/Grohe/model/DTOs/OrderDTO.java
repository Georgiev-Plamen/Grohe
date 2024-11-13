package bg.deplan.Grohe.model.DTOs;

import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.OrderItem;

import java.time.LocalDate;
import java.util.List;

public record OrderDTO(
        String orderName,
        LocalDate date,
        List<OrderItem> articleList)
{
}
