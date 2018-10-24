package com.xxy.intelligentpapersearch.node;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:15:13
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public abstract class BaseEntity {

	@GraphId
	private Long id;
}
