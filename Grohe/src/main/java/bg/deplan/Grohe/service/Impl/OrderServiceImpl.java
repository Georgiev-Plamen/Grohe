package bg.deplan.Grohe.service.Impl;

import bg.deplan.Grohe.data.ArticleRepository;
import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.model.DTOs.OrderDTO;
import bg.deplan.Grohe.model.Order;
import bg.deplan.Grohe.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ArticleRepository articleRepository;



    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, ArticleRepository articleRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.articleRepository = articleRepository;
//        this.todayDate = LocalDate.now();
    }

    @Override
    public boolean createOrder(OrderDTO orderDTO) {

        Optional<Article> optionalArticle = articleRepository.findByArtNum(orderDTO.article());

        Optional<Order> optionalOrder = Optional.ofNullable(modelMapper.map(orderDTO, Order.class));

        orderRepository.save(modelMapper.map(orderDTO, Order.class));

        return true;
    }
}
