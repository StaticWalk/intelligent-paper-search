package com.xxy.intelligentpapersearch.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:22:50
 */

@NodeEntity
@Data
public class Paper extends BaseEntity {

	private Long id;
	private String  doi;
	private String  document_id;
	private String  publisher;
	private String  publication_date;
	private String  summary;

	@Relationship(type = "classify")
	@JsonProperty("归类")
	private List<Genre> genres;

	@Relationship(type = "participate")
	@JsonProperty("参加")
	private List<Meeting> meetings;

		@Relationship(type = "attribute")
	@JsonProperty("属于")
	private List<Keyword> keywords;

}
