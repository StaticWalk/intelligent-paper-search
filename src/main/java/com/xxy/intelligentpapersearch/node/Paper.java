package com.xxy.intelligentpapersearch.node;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

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
//	private String  abstract;  todo

	private String  keywords;
}
