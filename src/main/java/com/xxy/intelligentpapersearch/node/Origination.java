package com.xxy.intelligentpapersearch.node;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:22:03
 */

@NodeEntity
@Data
public class Origination extends BaseEntity{

	private int id;
	private String  name;
	private String  location;
}
