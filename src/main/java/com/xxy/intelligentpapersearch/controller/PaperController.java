package com.xxy.intelligentpapersearch.controller;

import com.xxy.intelligentpapersearch.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/27
 * Time:21:01
 */

@Controller
@RequestMapping("/question")
public class PaperController {

	@Autowired
	QuestionService questionService;

	@RequestMapping("/query")
	@ResponseBody
	public String query(@RequestParam(value = "question") String question) throws Exception {

		String a = questionService.answer(question);

		return a;

	}

}
