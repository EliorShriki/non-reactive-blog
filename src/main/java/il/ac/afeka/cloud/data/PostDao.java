package il.ac.afeka.cloud.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PostDao extends PagingAndSortingRepository<PostEntity, String>{
	public List<PostEntity> findAll(Sort sort);
	public List<PostEntity> findAllByUser(@Param("user") UserEntity user, Pageable sort);
	public List<PostEntity> findAllByProduct(ProductEntity product, Pageable sort);
	
	public List<PostEntity> findAllByUserAndLanguage(UserEntity user, String language, Pageable sort);
	public List<PostEntity> findAllByUserAndProduct(UserEntity user, ProductEntity product, Pageable sort);
	
	public List<PostEntity> findAllByProductAndLanguage(ProductEntity product, String language, Sort sort);
}
