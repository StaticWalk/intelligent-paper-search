package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Paper;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/25
 * Time:21:30
 */

@Repository
public interface PaperRepository extends GraphRepository<Paper>{

	List<Paper> findByName(@Param("name") String name);


	@Query("start k:Keyword{name:{0}} match (p:Paper)-[r:attribute]->k return p ")
	List<Paper> findByKeywords(String keyword);

}
