package bg.deplan.Grohe.web;

import bg.deplan.Grohe.model.DTOs.AddArticleDTO;
import bg.deplan.Grohe.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("articles/api/")
public class ArticleRestController {

    private ArticleService articleService;

    public ArticleRestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping ("/receive/bulkUpdateArticle")
    public ResponseEntity<String> bulkUpdateArticle(@RequestBody List<AddArticleDTO> updates,
                                                    @RequestParam (required = false) String _method) {

        try {
            articleService.bulkUpdateArticle(updates);

            return ResponseEntity.ok().body("Update is successful");
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing updates: " + e.getMessage());
        }
    }
}
