package com.xxy.intelligentpapersearch.process;

import org.apache.spark.mllib.classification.NaiveBayesModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/24
 * Time:15:09
 */
public class ModelProcess {



	/**
	 * Spark贝叶斯分类器
	 */
	NaiveBayesModel nbModel;

	/**
	 * 分类标签号，问题模板表
	 */
	Map<Double,String> questionsPattern;

	/**
	 * 词语和下标的对应表   == 词汇表
	 */
	Map<String, Integer> vocabulary;


	/**
	 * 指定问题question及字典的txt模板所在的根目录
	 */
	String rootDirPath = "D:/HanLP/data";

	/**
	 * 分类模板索引
	 */
	int modelIndex = 0;


	public ModelProcess(){
		questionsPattern = loadQuestionsPattern();
//		vocabulary = loadVocabulary();
//		nbModel = loadClassifierModel();
	}


	public ModelProcess(String rootDirPath) throws Exception{
		this.rootDirPath = rootDirPath+'/';
		questionsPattern = loadQuestionsPattern();
//		vocabulary = loadVocabulary();
//		nbModel = loadClassifierModel();
	}


	private Map<Double,String> loadQuestionsPattern() {
		Map<Double,String>  questionPattern = new HashMap<>();
		File file = new File(rootDirPath + "/question/a_question_classification.txt");
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




}
