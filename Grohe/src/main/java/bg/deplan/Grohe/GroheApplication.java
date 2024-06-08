package bg.deplan.Grohe;

import bg.deplan.Grohe.data.OrderRepository;
import bg.deplan.Grohe.model.Article;
import bg.deplan.Grohe.model.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GroheApplication {

	public static void main(String[] args) {


		SpringApplication.run(GroheApplication.class, args);

		Article article = new Article();
		article.setArtNum("35600000");
		article.setQuantityForOrder(2);

		Article article1 = new Article();
		article1.setArtNum("22500LN0");
		article1.setQuantityForOrder(10);

		java.util.List<Article> articles = new ArrayList<>();
		articles.add(article);
		articles.add(article1);

		Order order = new Order(articles);

		System.out.println();
	}

}
