package il.ac.afeka.cloud.layout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortByEnumaration;
import il.ac.afeka.cloud.logic.PostService;

@RestController
public class BlogController {
	private PostService service;
	
	@Autowired
	public BlogController(PostService service) {
		super();
		this.service = service;
	}
	
    @RequestMapping(path = "/blog",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PostBoundary create(
    		@RequestBody PostBoundary value){
        return this.service.create(value);
    }

    @RequestMapping(path = "/blog/byUser/{email}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostBoundary> getAllPostsByUser(
            @PathVariable("email") String email,
            @RequestParam(name = "filterType", required = false, defaultValue = "") FilterTypeEnum filterType,
            @RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue,
            @RequestParam(name = "sortBy", required = false, defaultValue = "postingTimestamp") SortByEnumaration sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return this.service.getPostsAllByUser(
        		email, 
        		filterType, 
        		filterValue, 
        		sortBy, 
        		sortOrder,
        		page,
        		size);
    }

    @RequestMapping(path = "/blog/byProduct/{productId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostBoundary> getAllPostByProduct(
            @PathVariable("productId") String productId,
            @RequestParam(name = "filterType", required = false, defaultValue = "") FilterTypeEnum filterType,
            @RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue,
            @RequestParam(name = "sortBy", required = false, defaultValue = "postingTimestamp") SortByEnumaration sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return this.service.getAllPostsByProduct(
                productId,
                filterType,
                filterValue,
                sortBy,
                sortOrder,
        		page, 
        		size
        );
    }

    @RequestMapping(path = "/blog",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostBoundary> getAllPosts(
            @RequestParam(name = "filterType", required = false, defaultValue = "") FilterTypeEnum filterType,
            @RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue,
            @RequestParam(name = "sortBy", required = false, defaultValue = "postingTimestamp") SortByEnumaration sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return this.service.getAllPosts(
        		filterType, 
        		filterValue, 
        		sortBy, 
        		sortOrder, 
        		page, 
        		size);
    }

    @RequestMapping(path = "/blog",
            method = RequestMethod.DELETE)
    public void delete() {
        this.service.delete();
    }	
}
