package com.xxy.intelligentpapersearch.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/27
 * Time:19:51
 */

@NodeEntity
@Data
public class Keyword extends BaseEntity {

	@GraphId
	private Long id;
	private String name;

	@Relationship(type = "attribute")
	@JsonProperty("属于")
	private List<Paper> papers;
}
