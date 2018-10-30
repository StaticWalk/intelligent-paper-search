package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Author;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/25
 * Time:21:27
 */
@Repository
public interface AuthorRepository extends GraphRepository<Author> {


	List<Author> findByName(@Param("name") String name);


}
