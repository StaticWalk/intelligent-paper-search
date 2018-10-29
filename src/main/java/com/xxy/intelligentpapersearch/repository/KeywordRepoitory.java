package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Keyword;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/29
 * Time:19:44
 */

@Repository
public interface KeywordRepoitory extends GraphRepository<Keyword>{

	List<Keyword> findByName(@Param("name") String name);

}
