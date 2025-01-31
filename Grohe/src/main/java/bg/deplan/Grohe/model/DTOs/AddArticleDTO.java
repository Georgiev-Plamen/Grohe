package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record AddArticleDTO (
        String brand,
        String artNum,
        String name,
        String description,
        String imgUrl,
        String barcode,
        int quantityInPallet
) {
}
