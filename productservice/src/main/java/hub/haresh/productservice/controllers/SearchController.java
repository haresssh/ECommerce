package hub.haresh.productservice.controllers;

import hub.haresh.productservice.dtos.SearchRequestDto;
import hub.haresh.productservice.models.Product;
import hub.haresh.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public Page<Product> search(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.search(searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(), searchRequestDto
                        .getSize());
    }
}
