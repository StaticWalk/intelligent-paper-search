package com.xxy.intelligentpapersearch.service.impl;

import com.xxy.intelligentpapersearch.process.ModelProcess;
import com.xxy.intelligentpapersearch.repository.AuthorRepository;
import com.xxy.intelligentpapersearch.repository.KeywordRepoitory;
import com.xxy.intelligentpapersearch.repository.PaperRepository;
import com.xxy.intelligentpapersearch.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/26
 * Time:12:51
 */

@Service
@Primary
public class QuestionServiceImpl implements QuestionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
		 *						   paperKeyword.txt		authorName.txt(OpenNlp ner)
		 * 	 vocabulary classify: paperKeyword->pkw    authorName->atn
		 *   eg:
		 *   abstract:
		 *
		 */

		ModelProcess queryProcess = new ModelProcess();
		ArrayList<String>  resultString = queryProcess.analyQuery(question);
		int modexIndex = Integer.valueOf(resultString.get(0));
		String answer = null;
		String author = "";
		String keyword = "";
		String paper = "";

		switch (modexIndex){
			case 0:
				/**
				 * nm papers
				 */

				author = resultString.get(1);
				paper = paperRepository.findPaperByAuthorName(author).toString();
				if (author == null || paper ==null){
					answer = null;
				}
				else {
					answer = paper;
				}
				break;

			case 1:
				/**
				 * kw papers
				 */

				keyword = resultString.get(1);
				paper = paperRepository.findPaperByKeyword(keyword).toString();
				if (keyword == null || paper ==null){
					answer = null;
				}
				else {
					answer = paper;
				}
				break;

			default:
				break;
		}
		logger.info(answer);

		if (answer != null && !answer.equals("") && !answer.equals("\\N")) {
			return answer;
		} else {
			return "sorry,I didn't find the answer you wanted.";
		}


	}
}
