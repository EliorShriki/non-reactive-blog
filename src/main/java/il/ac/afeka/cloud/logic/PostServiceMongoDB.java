package il.ac.afeka.cloud.logic;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import il.ac.afeka.cloud.data.PostDao;
import il.ac.afeka.cloud.data.PostEntity;
import il.ac.afeka.cloud.data.ProductEntity;
import il.ac.afeka.cloud.data.UserEntity;
import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortByEnumaration;
import il.ac.afeka.cloud.enums.TimeEnum;
import il.ac.afeka.cloud.layout.PostBoundary;

@Service
public class PostServiceMongoDB implements PostService {
	private final String DEFAULT_SECONDARY_SORT = "_id";
	private PostDao postDao;
	
	@Autowired
	public PostServiceMongoDB(PostDao postDao) {
		this.postDao = postDao;
	}

	@Override
	public PostBoundary create(PostBoundary boundary) {
		boundary.setPostingTimestamp(new Date());
		PostEntity entity = boundary.toEntity();
		entity = this.postDao.save(entity);
		return new PostBoundary(entity);
	}

	@Override
	public List<PostBoundary> getPostsAllByUser(String email, FilterTypeEnum filterType, String filterValue,
			SortByEnumaration sortBy, String sortOrder) {
		
		Sort.Direction dir = sortOrder.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
		if (filterType != null)		
			switch (filterType) {
			case byProduct:
				return this.getAllPostByUserAndProduct(
						new UserEntity(email),
						new ProductEntity(filterValue),
						dir,
						sortBy.name());
			case byLanguage:
				return this.getAllPostsByUserAndLanguage(
						new UserEntity(email),
						filterValue,
						dir,
						sortBy.name());
			case byCreation:
				TimeEnum time = TimeEnum.valueOf(filterValue);
				return this.getAllPostsByUserAndCreation(
						new UserEntity(email),
						time,
						dir,
						sortBy.name());
			default:
				break;
			}

		return this.postDao.findAllByUser(
				new UserEntity(email),
				Sort.by(
						dir,
						sortBy.name(),
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PostBoundary> getAllPostsByProduct(String productId, FilterTypeEnum filterType, String filterValue,
			SortByEnumaration sortBy, String sortOrder) {
		Sort.Direction dir = sortOrder.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
		if (filterType != null)
			switch (filterType) {
			case byLanguage:
				return this.getAllPostsByProductAndLanguage(
						new ProductEntity(productId),
						filterValue,
						dir,
						sortBy.name());
			case byCreation:
				TimeEnum time = TimeEnum.valueOf(filterValue);
				return this.getAllPostsByProductAndCreation(
						new ProductEntity(productId),
						time,
						dir,
						sortBy.name());

			default:
				break;
			}

		return this.postDao.findAllByProduct(
				new ProductEntity(productId),
				Sort.by(
						dir,
						sortBy.name(),
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<PostBoundary> getAllPosts(FilterTypeEnum filterType, String filterValue, SortByEnumaration sortBy,
			String sortOrder) {
		Sort.Direction dir = sortOrder.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
		if (filterType != null)
			switch (filterType) {
			case byCreation:
				TimeEnum time = TimeEnum.valueOf(filterValue);
				return this.getAllPostsByCreation(
						dir,
						sortBy.name(),
						time);
			case byCount:
				return this.getAllPostsByCount(Long.parseLong(filterValue));
	
			default:
				break;
			}
		
		return this.postDao
				.findAll(Sort.by(
						sortOrder.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
						sortBy.name(),
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
	}

	private List<PostBoundary> getAllPostsByCount(long count) {
		return this.postDao
				.findAll(Sort.by(
						Sort.Direction.DESC,
						SortByEnumaration.postingTimestamp.name(),
						DEFAULT_SECONDARY_SORT))
//				.limitRequest(count)
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
	}

	private List<PostBoundary> getAllPostsByCreation(Direction dir, String sortBy, TimeEnum timeFilter) {
		List<PostBoundary> posts = this.postDao
				.findAll(Sort.by(
						dir,
						sortBy,
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
		
		return this.filterByCreation(posts, timeFilter);
	}

	@Override
	public void delete() {
		this.postDao.deleteAll();

	}

	private List<PostBoundary> getAllPostsByUserAndCreation(UserEntity userEntity, TimeEnum timeFilter, Direction dir,
			String sortBy) {	
		List<PostBoundary> posts = this.postDao.findAllByUser(
				userEntity,
				Sort.by(
						dir,
						sortBy,
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
		
		return this.filterByCreation(posts, timeFilter);
	}

	private List<PostBoundary> getAllPostsByUserAndLanguage(UserEntity userEntity, String lang, Direction dir,
			String sortBy) {
		return this.postDao.findAllByUserAndLanguage(userEntity, lang, Sort.by(
				dir,
				sortBy,
				DEFAULT_SECONDARY_SORT))
		.stream()
		.map(PostBoundary::new)
		.collect(Collectors.toList());
	}

	private List<PostBoundary> getAllPostByUserAndProduct(UserEntity userEntity, ProductEntity productEntity, Sort.Direction dir, String sortBy) {
		return this.postDao.findAllByUserAndProduct(userEntity, productEntity, Sort.by(
				dir,
				sortBy,
				DEFAULT_SECONDARY_SORT))
		.stream()
		.map(PostBoundary::new)
		.collect(Collectors.toList());
	}

	private List<PostBoundary> getAllPostsByProductAndLanguage(ProductEntity productEntity, String lang,
			Direction dir, String sortBy) {
		return this.postDao.findAllByProductAndLanguage(
				productEntity,
				lang,
				Sort.by(
						dir,
						sortBy,
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
	}

	private List<PostBoundary> getAllPostsByProductAndCreation(ProductEntity productEntity, TimeEnum timeFilter,
			Direction dir, String sortBy) {
		List<PostBoundary> posts = this.postDao.findAllByProduct(
				productEntity,
				Sort.by(
						dir,
						sortBy,
						DEFAULT_SECONDARY_SORT))
				.stream()
				.map(PostBoundary::new)
				.collect(Collectors.toList());
		
		return this.filterByCreation(posts, timeFilter);
	}

	private List<PostBoundary> filterByCreation(List<PostBoundary> posts, TimeEnum timeFilter) {
		long timeToSubstract = timeFilter.millisecs();
		return posts.stream().filter(p -> p.getPostingTimestamp()
				.compareTo(new Date(System.currentTimeMillis() - timeToSubstract)) > 0).collect(Collectors.toList());
	}

}
