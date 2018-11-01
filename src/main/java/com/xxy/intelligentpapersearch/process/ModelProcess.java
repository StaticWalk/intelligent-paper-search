package com.xxy.intelligentpapersearch.process;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
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
		nbModel = loadClassifierModel();
	}


	public ModelProcess(String rootDirPath) throws Exception{
		this.rootDirPath = rootDirPath;
		questionsPattern = loadQuestionsPattern();
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



	// 将句子抽象化
	public String queryAbstract(String querySentence) throws IOException {


		InputStream is = new FileInputStream("opennlp/en-ner-person.bin");

		TokenNameFinderModel model = new TokenNameFinderModel(is);
		is.close();

		NameFinderME nameFinder = new NameFinderME(model);


		String []sentence =querySentence.replace("[\\pP‘’“”]","").split(" ");

		System.out.println(sentence.length);

		Span nameSpans[] = nameFinder.find(sentence);

		for(Span s: nameSpans) {
			System.err.println(s.getStart()+ ""+s.length());
		}

		return null;
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
