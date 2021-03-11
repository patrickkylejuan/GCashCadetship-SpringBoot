package ph.apper.repository;

import org.springframework.data.repository.CrudRepository;
import ph.apper.domain.Blog;
import ph.apper.payload.BlogData;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BlogRepository extends CrudRepository<Blog,String> {

    Optional<Blog> findById(String blog_id);
    Stream<Blog> findAllByIsVisible(boolean isVisible);
    List<Blog> findAll();
}
