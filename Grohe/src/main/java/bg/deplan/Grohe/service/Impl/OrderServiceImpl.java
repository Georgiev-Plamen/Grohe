package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.AddArticleDTO;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.service.OrderService;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;
    private final LocalDate todayDate;


    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, ArticleRepository articleRepository, LocalDate localDate) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.articleRepository = articleRepository;
        this.todayDate = LocalDate.now();
    }

    @Override
    public long createOrder(AddArticleDTO addArticleDTO) {

        Article mappedArticle = modelMapper.map(addArticleDTO, Article.class);

        Article article = articleRepository.findByArtNum(mappedArticle.getArtNum());

        Order order = new Order();
        order.setArticles(List.of(article));
        order.setDateOfOrder(Instant.from(todayDate));

        orderRepository.save(order);

        return 0;
    }
}
