package com.xxy.intelligentpapersearch.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.awt.print.Paper;
import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:15:05
 */

@Data
@NodeEntity
public class Author extends BaseEntity{

	@GraphId
	private Long id;

	private String  name;
	private String  email;
	private String  birth;

	@Relationship(type = "create")
	@JsonProperty("创作")
	private List<Paper> papers;

	@Relationship(type = "belongTo")
	@JsonProperty("属于")
	private List<Origination> originations;

}
