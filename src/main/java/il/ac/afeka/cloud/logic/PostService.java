package il.ac.afeka.cloud.logic;

import java.util.List;

import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortByEnumaration;
import il.ac.afeka.cloud.layout.PostBoundary;

public interface PostService {
    PostBoundary create(PostBoundary value);

    List<PostBoundary> getPostsAllByUser(String email, FilterTypeEnum filterType, String filterValue, SortByEnumaration sortBy, String sortOrder);

    List<PostBoundary> getAllPostsByProduct(String productId, FilterTypeEnum filterType, String filterValue, SortByEnumaration sortBy, String sortOrder);

    List<PostBoundary> getAllPosts(FilterTypeEnum filterType, String filterValue, SortByEnumaration sortBy, String sortOrder);

    void delete();
}
