package com.xxy.intelligentpapersearch.process;

import org.apache.spark.mllib.classification.NaiveBayesModel;

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

	public ModelProcess() throws Exception{
//		questionsPattern = loadQuestionsPattern();
//		vocabulary = loadVocabulary();
//		nbModel = loadClassifierModel();
	}

}
