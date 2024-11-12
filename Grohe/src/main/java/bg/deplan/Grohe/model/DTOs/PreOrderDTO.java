package bg.deplan.Grohe.model.DTOs;

import bg.deplan.Grohe.model.Article;

import java.time.LocalDate;
import java.util.List;

public record PreOrderDTO (
        String orderName,
        LocalDate date,
        List<Article> articleList){
}
