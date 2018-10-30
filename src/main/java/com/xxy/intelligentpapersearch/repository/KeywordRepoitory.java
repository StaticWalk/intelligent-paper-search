package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Keyword;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/29
 * Time:19:44
 */

@Repository
public interface KeywordRepoitory extends GraphRepository<Keyword>{




//	@Query("match(n:Paper)-[:attribute]->(m:Keyword) where m.name='Duncan' return n")
//	List<Paper> getPaperTiles();

}
