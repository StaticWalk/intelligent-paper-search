package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.node.Paper;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/25
 * Time:21:30
 */
public interface PaperRepository extends GraphRepository<Paper>{
}
