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

	// @Query("MATCH (n:Author) WHERE n.name ={0} WITH n MATCH p=(n)-[*0..1]-(m) RETURN n,m")
	@Query("MATCH (n:Author)-[r:create]->(m:Paper) n.name ={0} RETURN n,m")
	List<Paper> findPaperByAuthorName(String name);


	@Query("match (n:Paper)-[:attribute]->(m:Keyword) where m.name={0} return n")
	List<Paper> findPaperByKeyword(String name);

}
