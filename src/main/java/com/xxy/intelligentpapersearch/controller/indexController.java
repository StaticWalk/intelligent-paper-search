package com.xxy.intelligentpapersearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/30
 * Time:19:19
 */

@Controller
public class indexController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}


}
