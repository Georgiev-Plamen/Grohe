package bg.deplan.Grohe.model.DTOs;

public record OrderEditArticleDTO(
        Long id,
        String brand,
        String codeDeplan,
        String artNum,
        String name,
        String description,
        String imgUrl,
        String barcode,
        int quantityInPallet
) {
}
