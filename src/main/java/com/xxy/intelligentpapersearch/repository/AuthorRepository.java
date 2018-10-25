package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Author;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/25
 * Time:21:27
 */
public interface AuthorRepository extends GraphRepository<Author> {
}
