package bg.deplan.Grohe.model.DTOs;

import java.time.LocalDate;

public record ArticleDTO (

        Long id,
        String brand,
        String artNum,
        String codeDeplan,
        String name,
        String description,
        String quantityForOrder,
        String artUrl,
        String imageUrl,
        String orderBy,
        int quantityInPallet,
        LocalDate date,
        String orderReason,
        String comment,
        String barcode
) {

}



