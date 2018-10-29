package com.xxy.intelligentpapersearch.node;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:22:00
 */

@NodeEntity
@Data
public class Genre extends BaseEntity {

	@GraphId
	private Long id;
	private String type;

}
