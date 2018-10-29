package com.xxy.intelligentpapersearch.node;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:22:01
 */

@NodeEntity
@Data
public class Meeting extends BaseEntity{

	@GraphId
	private Long id;
	private String location;
	private String date;
	private String name;
}
