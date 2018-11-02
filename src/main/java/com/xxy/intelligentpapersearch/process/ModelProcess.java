package com.xxy.intelligentpapersearch.process;

import com.xxy.intelligentpapersearch.enums.AbstractWordEnum;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.spark.mllib.classification.NaiveBayesModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:15:09
 */
public class ModelProcess {
	/**
	 * dict :  questionPattern paperKeyword  personName  问题模板个数个问题表
	 */


	/**
	 * Spark贝叶斯分类器
	 */
	NaiveBayesModel nbModel;


	/**
	 * 分类标签号，问题模板表
	 */
	Map<Double,String> questionsPattern;


	/**
	 * 论文关键词词汇表
	 */
	Map<Integer, String> paperKeywordDict;



	/**
	 * 论文类别词汇表
	 */
	Map<Integer, String> paperGenreDict;


	/**
	 * 存放被抽象化了的信息（人名、关键词、类别）
	 */
	Map<String,AbstractWordEnum> abstrctWords = new HashMap();



	/**
	 * 指定问题question及字典的txt模板所在的根目录
	 */
	String rootDirPath = "D:/intelligent-paper-search";


	/**
	 * OpenNLP中Named Entity Recognition数据训练集
	 */
	String nerDictPath = rootDirPath + "/opennlp/en-ner.person.bin";


	/**
	 * 分类模板索引
	 */
	int modelIndex = 0;


	public ModelProcess(){
		questionsPattern = loadQuestionsPattern();
		paperKeywordDict = loadPaperKeywordDict();
		paperGenreDict = loadPaperGenreDict();
		nbModel = loadClassifierModel();
	}


	public ModelProcess(String rootDirPath) throws Exception{
		this.rootDirPath = rootDirPath;
		questionsPattern = loadQuestionsPattern();
		paperGenreDict = loadPaperGenreDict();
		paperKeywordDict = loadPaperKeywordDict();
		nbModel = loadClassifierModel();
	}




	public ArrayList<String> analyQuery(String queryString) throws IOException {


		System.out.println("原始句子："+queryString);

		/**
		 * 原始句子抽象化
		 */
		String abstr = queryAbstract(queryString);
		System.out.println("句子抽象化结果："+abstr);

		/**
		 * 抽象的句子拿来匹配训练集中模板，拿到句子模板
		 */
		String strPatt = queryClassify(abstr);
		System.out.println("句子套用模板结果："+strPatt);


		/**
		 * 模板还原成句子，此时问题已转换为我们熟悉的操作
		 * todo
		 */
		String finalPattern = queryExtenstion(strPatt);
		System.out.println("原始句子替换成系统可识别的结果："+finalPattern);

		return null;
	}




	/**
	 * 将句子抽象化
	 * 将需要用到的一类信息抽象，如人名、关键词、类别
	 * 这里因为用了openNLP的ner，但是没有找到自定义词性这项功能，代码写得不是很好看
	 */
	public String queryAbstract(String querySentence) throws IOException {


		InputStream neris = new FileInputStream("opennlp/en-ner-person.bin");
		InputStream tokenis = new FileInputStream("opennlp/en-token.bin");

		TokenNameFinderModel ner_model = new TokenNameFinderModel(neris);
		TokenizerModel token_model = new TokenizerModel(tokenis);

		tokenis.close();
		neris.close();

		NameFinderME nameFinder = new NameFinderME(ner_model);
		Tokenizer tokenizer = new TokenizerME(token_model);

		//除去句子中所有类型的标点符号
		querySentence = querySentence.replaceAll("[\\pP‘’“”]","");

		String tokens [] =tokenizer.tokenize(querySentence);

		Span[] nameSpans = nameFinder.find(tokens);

		/**
		 * 开始真正的抽象工作
		 * 人名 -> nm  关键词 -> kw  作品分类 -> gr
		 *
		 * 先把NameFinderModer找出来的英文人名标记
		 *
		 */

		for (int i = 0; i < nameSpans.length; i++) {
			StringBuilder a= new StringBuilder();

			for (int j = nameSpans[i].getStart(); j < nameSpans[i].getStart()+nameSpans[i].length(); j++) {
				a.append(tokens[j]).append(" ");
			}
			abstrctWords.put(a.substring(0,a.length()-1),AbstractWordEnum.PERSONNAME);
		}

		//遍历当前数组，找出其他的信息词
		for (String token : tokens){
			if (paperKeywordDict.containsValue(token)){
				abstrctWords.put(token,AbstractWordEnum.KEYWORD);
			}
			else if(paperGenreDict.containsValue(token)){
				abstrctWords.put(token,AbstractWordEnum.GENRE);
			}

			/**
			 * else if()
			 * 这里补充其他信息词汇的查询
			 */
		}

		//拿到所有可以抽象的词abstractWords,然后对原始句子进行抽象
		for (Map.Entry<String,AbstractWordEnum> entry:abstrctWords.entrySet()){
					querySentence = querySentence.replace(entry.getKey(),entry.getValue().getNature());
		}

		return querySentence;
	}




	public String queryClassify(String abstr) {
		return null;
	}
	
	
	//
	public String queryExtenstion(String strPatt) {
		return null;
	}
	
	
	public NaiveBayesModel loadClassifierModel() {

		return null;
	}


	public Map<Double,String> loadQuestionsPattern() {
		Map<Double,String>  questionPattern = new HashMap<>();
		File file = new File(rootDirPath + "/question/paperKeywordDict.txt");
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try{
			while (null != (line = br.readLine())){
				String[] tokens = line.split(":");
				double index = Double.valueOf(tokens[0]);
				questionPattern.put(index,tokens[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionPattern;
	}


	private Map<Integer,String> loadPaperGenreDict() {


		Map<Integer,String>  paperGenres = new HashMap<>();
		File file = new File(rootDirPath + "/question/paperGenreDict.txt");
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try{
			while (null != (line = br.readLine())){
				String[] tokens = line.split(":");
				Integer index = Integer.valueOf(tokens[0]);
				paperGenres.put(index,tokens[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return paperGenres;
	}


	public Map<Integer, String> loadPaperKeywordDict() {

		Map<Integer,String>  paperKeywords = new HashMap<>();
		File file = new File(rootDirPath + "/question/paperKeywordDict.txt");
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try{
			while (null != (line = br.readLine())){
				String[] tokens = line.split(":");
				Integer index = Integer.valueOf(tokens[0]);
				paperKeywords.put(index,tokens[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return paperKeywords;
	}




}
