package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.OrderService;
import bg.deplan.Grohe.service.PreOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Autowired
    private final ResourceLoader resourceLoader;

    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final ArticleService articleService;
    @Autowired
    private final PreOrderItemRepository preOrderRepository;
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    public PreOrderServiceImpl(ResourceLoader resourceLoader, ArticleRepository articleRepository, ArticleService articleService, PreOrderItemRepository preOrderRepository, OrderRepository orderRepository, OrderService orderService) {
        this.resourceLoader = resourceLoader;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.preOrderRepository = preOrderRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public PreOrderItem savePreOrder(PreOrderItem preOrder) {
        return preOrderRepository.save(preOrder);
    }


    public void addItem(ArticleDTO articleDTO) {
        PreOrderItem preOrderItem = new PreOrderItem();

        Optional<Article> optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());

        if (optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(articleDTO.artNum());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());
        }
        preOrderItem.setArticle(optionalArticle.get());
        preOrderItem.setQuantityForOrder(articleDTO.quantityForOrder());
        preOrderItem.setOrderBy(articleDTO.orderBy());
        preOrderItem.setDate(articleDTO.date());
        preOrderItem.setOrderReason(articleDTO.orderReason());
        preOrderItem.setComment(articleDTO.comment());

        preOrderRepository.save(preOrderItem);
    }

    @Override
    public void updateItems(PreOrderDTO preOrderDTO, Long id) {

        PreOrderItem preOrderItem = preOrderRepository.getReferenceById(id);

        if (preOrderItem == null) {
            throw new EntityNotFoundException("PreOrderItem not found with id: " + id);
        }

        Optional<Article> optionalArticle = articleRepository.findByArtNum(preOrderDTO.artNum());

        if (optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(preOrderDTO.artNum());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(preOrderDTO.artNum());
        }

        if (!preOrderDTO.artNum().isEmpty()) {
            preOrderItem.setArticle(optionalArticle.get());
        }
        if (preOrderDTO.quantityForOrder().isEmpty()) {
            preOrderItem.setQuantityForOrder(preOrderDTO.quantityForOrder());
        }
        if (!preOrderDTO.orderBy().isEmpty()) {
            preOrderItem.setOrderBy(preOrderDTO.orderBy());
        }
        if (!preOrderDTO.date().toString().isEmpty()) {
            preOrderItem.setDate(preOrderDTO.date());
        }
        if (!preOrderDTO.orderReason().isEmpty()) {
            preOrderItem.setOrderReason(preOrderDTO.orderReason());
        }
        if (!preOrderDTO.comment().isEmpty()) {
            preOrderItem.setComment(preOrderDTO.comment());
        }

        preOrderRepository.save(preOrderItem);
    }

    @Override
    public PreOrderItem findById(Long id) {
        return preOrderRepository.findById(id).get();
    }

    @Override
    public void deletePreOrder(Long id) {
        preOrderRepository.deleteById(id);
    }


    @Override
    public List<ArticleDTO> getAllArticle() {
        return preOrderRepository.findAll()
                .stream()
                .map(PreOrderServiceImpl::toAllItem)
                .toList();
    }

    @Override
    public void makeOrder(String name) {

        List<PreOrderItem> preOrderList = preOrderRepository.findAll();
        orderService.createOrder(preOrderList, name);
        preOrderRepository.deleteAll();
    }

    private static ArticleDTO toAllItem(PreOrderItem preOrderItem) {
        return new ArticleDTO(
                preOrderItem.getId(),
                preOrderItem.getArticle().getArtNum(),
                preOrderItem.getQuantityForOrder(),
                preOrderItem.getArticle().getArtUrl(),
                preOrderItem.getOrderBy(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment()
        );
    }

    public List<PreOrderExcelDTO> readPreOrderFromExcel(InputStream inputStream) throws IOException {
        // List to store the parsed DTOs
        List<PreOrderExcelDTO> preOrderExcelDTOList = new ArrayList<>();

        // Use the provided InputStream to read the Excel file
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            // Assuming the data is in the first sheet
            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum() - 3;

            // Iterate over rows, starting from the 10th row (skip the header)
            for (int i = 9; i < lastRow; i++) {
                Row row = sheet.getRow(i);

                if (row != null) {
                    // Read article number
                    Cell artNumCell = row.getCell(1);
                    String artNum;
                    if (artNumCell.getCellType() == CellType.NUMERIC) {
                        artNum = String.valueOf((long) artNumCell.getNumericCellValue());
                    } else {
                        artNum = artNumCell.getStringCellValue();
                    }

                    // Read quantity for order
                    Cell quantityCell = row.getCell(3);
                    String quantityForOrder;
                    if (quantityCell.getCellType() == CellType.NUMERIC) {
                        quantityForOrder = String.valueOf((int) quantityCell.getNumericCellValue());
                    } else {
                        quantityForOrder = quantityCell.getStringCellValue();
                    }

                    // Read comment
                    String comment = row.getCell(10).getStringCellValue();

                    // Create DTO and add it to the list
                    PreOrderExcelDTO preOrderExcelDTO = new PreOrderExcelDTO(
                            artNum,
                            quantityForOrder,
                            LocalDate.now(), // Use current date
                            comment
                    );

                    preOrderExcelDTOList.add(preOrderExcelDTO);

                    // Print for debugging
                    System.out.printf("Article: %s - %s pcs. Comment: %s %n",
                            preOrderExcelDTO.artNum(),
                            preOrderExcelDTO.quantityForOrder(),
                            preOrderExcelDTO.comment());
                }
            }
        }

        // Convert the list of DTOs to PreOrderItem (if needed)
        listToPreOrderItem(preOrderExcelDTOList);

        return preOrderExcelDTOList;
    }

    public void listToPreOrderItem(List<PreOrderExcelDTO> preOrderExcelDTOList) {

        for (PreOrderExcelDTO preOrderExcelItems : preOrderExcelDTOList) {
            Optional<Article> optionalArticle = articleRepository.findByArtNum(preOrderExcelItems.artNum());
            PreOrderItem preOrderItem = new PreOrderItem();

            if (optionalArticle.isEmpty()) {
                Article article = new Article();
                article.setArtNum(preOrderExcelItems.artNum());
                articleRepository.save(article);
                optionalArticle = articleRepository.findByArtNum(preOrderExcelItems.artNum());
            }

            preOrderItem.setArticle(optionalArticle.get());
            preOrderItem.setQuantityForOrder(preOrderExcelItems.quantityForOrder());
            preOrderItem.setOrderBy("Вили");
            preOrderItem.setDate(preOrderExcelItems.date());
            //TODO: need to create method that translate cyrillic to latin
            preOrderItem.setOrderReason("Stocks");
            preOrderItem.setComment(preOrderExcelItems.comment());

            preOrderRepository.save(preOrderItem);
        }
    }

}
