package com.xxy.intelligentpapersearch.repository;

import com.xxy.intelligentpapersearch.IntelligentPaperSearchApplication;
import com.xxy.intelligentpapersearch.node.Author;
import com.xxy.intelligentpapersearch.node.Paper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/27
 * Time:21:19
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntelligentPaperSearchApplication.class)
public class RepositoriesTest {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PaperRepository paperRepository;

	@Autowired
	private KeywordRepoitory keywordRepoitory;


	//fingby_其实是固定的cypher，需要使用自定义的才行，否则查不出来
	//呜呜呜，cypher用错了

	@Test
	public void Author_FBName_test(){
		List<Author> a=authorRepository.findByName("Hydraulic Fracturing-New Developments");
		System.err.println(a);
	}

	@Test
	public void Paper_FBProperties_test(){
		//测试能否拿到级联属性的数据，开始发现不能获取级联数据，结果是服务器数据库没导入

		//通过关键词拿到级联的paper
//		List<Paper> a=paperRepository.findPaperByKeywords("Oklahoma");
		//通过作者名字拿到paper
		List<Paper> a=paperRepository.findPaperByAuthorName("xiongxiaoyu");
		//论文的name获取论文详细信息
//		List<Paper> a=paperRepository.findByName("Hydraulic Fracturing-New Developments");
		System.err.println(a);
	}


	@Test
	public void Paper_FBKeywords_test(){
		//通过关键词拿到匹配的Paper
//		List<Paper> a=keywordRepoitory.getPaperTiles();
//		List<Paper> a=keywordRepoitory.findByName("Duncan");
//		System.err.println(a);
	}


}