package com.xxy.intelligentpapersearch.service.impl;

import com.xxy.intelligentpapersearch.process.ModelProcess;
import com.xxy.intelligentpapersearch.repository.AuthorRepository;
import com.xxy.intelligentpapersearch.repository.KeywordRepoitory;
import com.xxy.intelligentpapersearch.repository.PaperRepository;
import com.xxy.intelligentpapersearch.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


	@Value("${paperKeywordDictPath}")
	private String paperKeywordDictPath;

	@Value("${authorNameDictPath}")
	private String authorNameDictPath;


//	private String


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
		 *						   paperKeyword.txt		authorName.txt
		 * 	 vocabulary classify: paperKeyword->pkw    authorName->atn
		 *   eg:
		 *   abstract:
		 *
		 */
		String  eg = "what is you name";
//		String[] a = eg.split(" ");
		ModelProcess modelProcess = new ModelProcess();
		modelProcess.analyQuery(eg);







		return null;
	}
}
