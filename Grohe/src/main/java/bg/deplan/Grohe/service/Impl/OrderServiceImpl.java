package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.AddArticleDTO;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.model.User;
import bg.deplan.Grohe.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;
//    private final LocalDate todayDate;


    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, ArticleRepository articleRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.articleRepository = articleRepository;
//        this.todayDate = LocalDate.now();
    }

    @Override
    public long createOrder(AddArticleDTO addArticleDTO) {

        Article mappedArticle = modelMapper.map(addArticleDTO, Article.class);


        Article article = articleRepository.findByArtNum(mappedArticle.getArtNum());

        List<Article> articles = new ArrayList<>();
        articles.add(article);

        User user = new User();
        user.setUserName("Test");

        List<User> users = new ArrayList<>();
        users.add(user);


        Order order = new Order(users, articles);

        orderRepository.save(order);

        return 0;
    }
}
