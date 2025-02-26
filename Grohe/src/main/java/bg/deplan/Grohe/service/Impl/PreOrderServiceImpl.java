package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderItemRepository;
import bg.deplan.Grohe.data.OrderRepository;

import bg.deplan.Grohe.data.PreOrderItemRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.ArticleDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.model.PreOrderItem;
import bg.deplan.Grohe.service.ArticleService;
import bg.deplan.Grohe.service.ExcelExportService;
import bg.deplan.Grohe.service.OrderService;
import bg.deplan.Grohe.service.PreOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PreOrderItemRepository preOrderItemRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;
    @Autowired
    private final ExcelExportService excelExportService;

    @Autowired
    private OrderService orderService;

    public PreOrderServiceImpl(ResourceLoader resourceLoader, ArticleRepository articleRepository, ArticleService articleService, PreOrderItemRepository preOrderItemRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ExcelExportService excelExportService, OrderService orderService) {
        this.resourceLoader = resourceLoader;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.preOrderItemRepository = preOrderItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.excelExportService = excelExportService;
        this.orderService = orderService;
    }

    public PreOrderItem savePreOrder(PreOrderItem preOrder) {
        return preOrderItemRepository.save(preOrder);
    }


    public void addItem(ArticleDTO articleDTO) {
        PreOrderItem preOrderItem = new PreOrderItem();

        Optional<Article> optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());

        if (optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(articleDTO.artNum());
            article.setBrand(articleDTO.brand());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(articleDTO.artNum());
        }

        preOrderItem.setArticle(optionalArticle.get());
        preOrderItem.setQuantityForOrder(articleDTO.quantityForOrder());
        preOrderItem.setOrderBy(articleDTO.orderBy());
        preOrderItem.setDate(articleDTO.date());
        preOrderItem.setOrderReason(articleDTO.orderReason());
        preOrderItem.setComment(articleDTO.comment());

        preOrderItemRepository.save(preOrderItem);
    }

    @Override
    @Transactional
    public void updateItems(PreOrderDTO preOrderDTO, Long id) {
        PreOrderItem preOrderItem = preOrderItemRepository.findById(preOrderDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("PreOrderItem not found with id: " + id));


        Optional<Article> optionalArticle;

        String artNum = preOrderDTO.artNum();
        if (preOrderDTO.artNum().isEmpty()) {
            optionalArticle = articleRepository.findByArtNum(preOrderItem.getArticle().getArtNum());
        } else {
            optionalArticle = articleRepository.findByArtNum(preOrderDTO.artNum());
        }

        if (optionalArticle.isEmpty()) {
            Article article = new Article();
            article.setArtNum(preOrderDTO.artNum());
            article.setBrand(preOrderDTO.brand());
            articleRepository.save(article);
            optionalArticle = articleRepository.findByArtNum(preOrderDTO.artNum());
        }

        if (preOrderDTO.artNum() != null && !preOrderItem.getArticle().getArtNum().equals(optionalArticle.get().getArtNum())) {
            preOrderItem.setArticle(optionalArticle.get());
        }
        if (preOrderDTO.quantityForOrder() != null && !preOrderDTO.quantityForOrder().isEmpty()) {
            preOrderItem.setQuantityForOrder(preOrderDTO.quantityForOrder());
        }
        if (preOrderDTO.orderBy() != null && !preOrderDTO.orderBy().isEmpty()) {
            preOrderItem.setOrderBy(preOrderDTO.orderBy());
        }
        if (preOrderDTO.date() != null) {
            preOrderItem.setDate(preOrderDTO.date());
        }
        if (preOrderDTO.orderReason() != null && !preOrderDTO.orderReason().isEmpty()) {
            preOrderItem.setOrderReason(preOrderDTO.orderReason());
        }
        if (preOrderDTO.comment() != null && !preOrderDTO.comment().isEmpty()) {
            preOrderItem.setComment(preOrderDTO.comment());
        }

        preOrderItemRepository.save(preOrderItem);
    }

    @Override
    public PreOrderItem findById(Long id) {
        return preOrderItemRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void deletePreOrderArticle(Long id) {
        preOrderItemRepository.deleteByArticleId(id);
    }

    @Override
    public List<ArticleDTO> getAllArticle(String brand) {
        return preOrderItemRepository.findAll()
                .stream()
                .map(PreOrderServiceImpl::toAllItem)
                .filter(a -> a.brand().equals(brand))
                .toList();
    }

    public List<PreOrderDTO> getAllPreOrder(String brand) {
        return preOrderItemRepository.findAllByArticle_Brand(brand)
                .stream().map(PreOrderServiceImpl::toAllPreOrderItem)
                .toList();
    }

    @Override
    @Transactional
    public void makeOrder(String name, String brand) {

        List<PreOrderItem> preOrderList = preOrderItemRepository.findAllByArticle_Brand(brand);
        orderService.createOrder(preOrderList, name, brand);
        preOrderItemRepository.deleteAllByArticle_Brand(brand);
    }

    @Override
    @Transactional
    public boolean createAndExportOrder(String name, String brand) throws IOException {

        List<PreOrderItem> PreOrderItem = preOrderItemRepository.findAllByArticle_Brand(brand);
        orderService.createOrder(PreOrderItem, name, brand);
        preOrderItemRepository.deleteAllByArticle_Brand(brand);
//        excelExportService.exportOrderToExcel(orderService.lastOrderId());

        return true;
    }

    private static ArticleDTO toAllItem(PreOrderItem preOrderItem) {
        return new ArticleDTO(
                preOrderItem.getArticle().getId(),
                preOrderItem.getArticle().getBrand(),
                preOrderItem.getArticle().getArtNum(),
                preOrderItem.getArticle().getCodeDeplan(),
                preOrderItem.getArticle().getName(),
                preOrderItem.getArticle().getDescription(),
                preOrderItem.getQuantityForOrder(),
                preOrderItem.getArticle().getArtUrl(),
                preOrderItem.getOrderBy(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment(),
                preOrderItem.getArticle().getBarcode()
        );
    }

    private static PreOrderDTO toAllPreOrderItem(PreOrderItem preOrderItem) {
        return new PreOrderDTO(
                preOrderItem.getId(),
                preOrderItem.getArticle().getBrand(),
                preOrderItem.getArticle().getArtNum(),
                preOrderItem.getQuantityForOrder(),
                preOrderItem.getOrderBy(),
                preOrderItem.getDate(),
                preOrderItem.getOrderReason(),
                preOrderItem.getComment()
        );
    }

    public List<PreOrderExcelDTO> readPreOrderFromExcel(InputStream inputStream, String brand) throws IOException {
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
                            brand,
                            artNum,
                            quantityForOrder,
                            LocalDate.now(), // Use current date
                            comment
                    );

                    preOrderExcelDTOList.add(preOrderExcelDTO);
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
                article.setBrand(preOrderExcelItems.brand());
                articleRepository.save(article);
                optionalArticle = articleRepository.findByArtNum(preOrderExcelItems.artNum());
            }

            preOrderItem.setArticle(optionalArticle.get());
            preOrderItem.setQuantityForOrder(preOrderExcelItems.quantityForOrder());
            preOrderItem.setOrderBy("Вили");
            preOrderItem.setDate(preOrderExcelItems.date());
            preOrderItem.setOrderReason(checkComment(preOrderExcelItems.comment()));
            preOrderItem.setComment(preOrderExcelItems.comment());

            preOrderItemRepository.save(preOrderItem);
        }
    }

    private String checkComment(String comment) {
        if(comment == null) {
            return "Stocks";
        }

        String[] splitComment = comment.split(" ");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < splitComment.length; i++) {
            stringBuilder.append(splitComment[i] + " ");
        }

        String commentsInfo = stringBuilder.toString();
        String translateComment = transliterate(commentsInfo);

        for (int i = 0; i < splitComment.length; i++) {
            String currentWord = splitComment[i].toLowerCase();

            if (currentWord.contains("промo")) {
                return "Promo offer";
            }

            if (currentWord.contains("мостр")) {
                return "Samples " + translateComment;
            }

            if (currentWord.contains("проек")) {
                return "Project " + translateComment;
            }

            if (currentWord.contains("офер")) {
                return "Project " + translateComment;
            }
            if (currentWord.contains("ofert")) {
                return "Project " + translateComment;
            }
        }

        return "Stocks";
    }

    public static String transliterate(String message){
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","q","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Q","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

}
