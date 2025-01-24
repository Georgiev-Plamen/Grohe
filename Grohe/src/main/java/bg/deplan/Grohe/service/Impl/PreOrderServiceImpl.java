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
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PreOrderItemRepository preOrderRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    public PreOrderItem savePreOrder(PreOrderItem preOrder) {
        return preOrderRepository.save(preOrder);
    }


    public void addItem(ArticleDTO articleDTO) {
        PreOrderItem preOrderItem = new PreOrderItem();

        Optional<Article> optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());

        if(optionalArticle.isEmpty()) {
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

        if(optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(preOrderDTO.artNum());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(preOrderDTO.artNum());
        }

        if(!preOrderDTO.artNum().isEmpty()) {
                preOrderItem.setArticle(optionalArticle.get());
        }
        if(preOrderDTO.quantityForOrder() != 0) {
            preOrderItem.setQuantityForOrder(preOrderDTO.quantityForOrder());
        }
        if(!preOrderDTO.orderBy().isEmpty()) {
            preOrderItem.setOrderBy(preOrderDTO.orderBy());
        }
        if(!preOrderDTO.date().toString().isEmpty()) {
            preOrderItem.setDate(preOrderDTO.date());
        }
        if(!preOrderDTO.orderReason().isEmpty()) {
            preOrderItem.setOrderReason(preOrderDTO.orderReason());
        }
        if(!preOrderDTO.comment().isEmpty()) {
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

    public List<PreOrderExcelDTO> readPreOrderFromExcel(String filePath) throws IOException {
            List<PreOrderExcelDTO> preOrders = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                // Assuming the data is in the first sheet
                Sheet sheet = workbook.getSheetAt(0);

                // Iterate over rows, starting from the second row (skip the header)
                for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    if (row != null) {
                        String artNum = row.getCell(1).getStringCellValue();
                        Integer quantityForOrder = (int) row.getCell(3).getNumericCellValue();
                        LocalDate date = LocalDate.now();
                        String orderReason = "Stocks";
                        String comment = row.getCell(10).getStringCellValue();

                        // Create DTO and add it to the list
                        PreOrderExcelDTO preOrderExcelDTO = new PreOrderExcelDTO(
                                artNum,
                                quantityForOrder,
                                date,
                                comment
                        );

                        preOrders.add(preOrderExcelDTO);
                    }
                }
            }

           return preOrders;
    }

}
