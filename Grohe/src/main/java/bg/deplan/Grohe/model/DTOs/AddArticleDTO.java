package bg.deplan.Grohe.model.DTOs;

public record AddArticleDTO (
        Long id,
        String brand,
        String codeDeplan,
        String artNum,
        String name,
        String description,
        String imgUrl,
        String artUrl,
        String barcode,
        Integer quantityInPallet
) {
}
