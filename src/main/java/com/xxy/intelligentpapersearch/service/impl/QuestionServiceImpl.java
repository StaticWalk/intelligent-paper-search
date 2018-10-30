package com.xxy.intelligentpapersearch.service.impl;

import com.xxy.intelligentpapersearch.repository.AuthorRepository;
import com.xxy.intelligentpapersearch.repository.KeywordRepoitory;
import com.xxy.intelligentpapersearch.repository.PaperRepository;
import com.xxy.intelligentpapersearch.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/26
 * Time:12:51
 */

@Service
@Primary
public class QuestionServiceImpl implements QuestionService {


	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PaperRepository paperRepository;

	@Autowired
	private KeywordRepoitory keywordRepoitory;


	@Override
	public String answer(String question) throws Exception {

		/**
		 * 正常流程处理：
		 * 		抽象 todo
		 *  	问题模板
		 *  	cypher
		 * 		仿生
		 *
		 * 测试简化：
		 *
		 */



		return null;
	}
}
