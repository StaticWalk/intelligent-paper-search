package com.xxy.intelligentpapersearch.process;

import com.xxy.intelligentpapersearch.enums.AbstractWordEnum;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import java.io.*;
import java.util.*;


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
	 * 词语和下标的对应表   == 词汇表
	 */
	Map<String, Integer> vocabulary;


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


	public ModelProcess() throws IOException {
		questionsPattern = loadQuestionsPattern();
		vocabulary = loadVocabulary();
		paperKeywordDict = loadPaperKeywordDict();
		paperGenreDict = loadPaperGenreDict();
		nbModel = loadClassifierModel();
	}




	public ModelProcess(String rootDirPath) throws Exception{
		this.rootDirPath = rootDirPath;
		questionsPattern = loadQuestionsPattern();
		vocabulary = loadVocabulary();
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
		 * 模板还原成句子
		 */
		String finalPattern = queryExtenstion(strPatt);
		System.out.println("原始句子替换成系统可识别的结果："+finalPattern);

		ArrayList<String> resultList = new ArrayList<>();
		resultList.add(String.valueOf(modelIndex));
		String[] finalPattArray = finalPattern.split(" ");
		for (String word : finalPattArray) {
			resultList.add(word);
		}
		System.err.println("resultList: " +resultList);
		return resultList;
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

		//除去句子中所有类型的标点符号和 's
		querySentence = querySentence.replace("'s","").replaceAll("[\\pP‘’“”]","");

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

			//先把人名抽象出来，再将流程中使用到的word全部转成小写，避免格式原因大小写
			token = token.toLowerCase();

			if (paperKeywordDict.containsValue(token)){
				abstrctWords.put(token,AbstractWordEnum.KEYWORD);
			}
			else if(paperGenreDict.containsValue(token)){
				abstrctWords.put(token,AbstractWordEnum.GENRE);
			}

			/**
			 * todo
			 * 通过添加else if()
			 * 在这里补充其他信息词汇的查询
			 */
		}

		//拿到所有可以抽象的词abstractWords,然后对原始句子进行抽象
		for (Map.Entry<String,AbstractWordEnum> entry : abstrctWords.entrySet()){
					querySentence = querySentence.replace(entry.getKey(),entry.getValue().getNature());
		}

		return querySentence;
	}

	/**
	 *
	 * @param sentence
	 * @return
	 * @throws IOException
	 *
	 * 语句分词后生成向量
	 */
	public double[] sentenceToArrays(String sentence) throws IOException {

		/**
		 * 引入训练英文分词训练集，使用openNLP的Tokenizer分词
		 */
		InputStream is = new FileInputStream("opennlp/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		is.close();

		double[] vector = new double[vocabulary.size()];

		/**
		 * 模板对照词汇表的大小初始化，全部为0
		 */
		for (int i = 0; i < vocabulary.size(); i++) {
			vector[i] = 0;
		}


		/**
		 * vocabulary中的每一列词不能重复或者有多余空格
		 */

		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize(sentence);
		for (String token:tokens){
			if (vocabulary.containsKey(token)) {
				int index = vocabulary.get(token);
				vector[index] = 1;
			}
		}

		return vector;

	}


	private Map<String,Integer> loadVocabulary() {

		Map<String, Integer> vocabulary = new HashMap();
		File file = new File(rootDirPath + "/question/vocabulary.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(":");
				int index = Integer.parseInt(tokens[0]);
				String word = tokens[1];
				vocabulary.put(word, index);
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return vocabulary;
	}



	public String queryClassify(String abstr) throws IOException {

		double[] testArray = sentenceToArrays(abstr);
		Vector v = Vectors.dense(testArray);

		/**
		 * 对数据进行预测
		 * 获取在spark分类器中的索引
		 * 推断出句子使用的模板
		 */
		double index = nbModel.predict(v);
		modelIndex = (int)index;
		System.out.println("the model index is " + modelIndex);
		System.out.println(nbModel.predictProbabilities(v));

		return questionsPattern.get(index);
	}
	
	
	//将句子对应成模板问题
	public String queryExtenstion(String strPatt) {

		String extendedsQuery = "";

		for (Map.Entry<String,AbstractWordEnum> entry : abstrctWords.entrySet()){

			/**
			 * 找出模板中存在的抽象词，并用真实值替代还原语句  nm -> Jay Chou
			 */
			if (strPatt.contains(entry.getValue().getNature())){

				extendedsQuery = strPatt.replace(entry.getValue().getNature(),entry.getKey());
			}
		}

		/**
		 * 置空abstrctWords
		 */
		abstrctWords.clear();
		abstrctWords = null;
		return  extendedsQuery;

	}

	
	public NaiveBayesModel loadClassifierModel() throws IOException {

		/**
		 * 生成Spark对象
		 * 一、Spark程序是通过SparkContext发布到集群上？？
		 * Spark程序的运行都是在SparkContext为核心的调度器的指挥下进行的
		 * Spark程序的运行都是以SparkContext结束作为结束
		 * JavaSparkContext对象用来创建Spark的核心RDD的
		 * 首个RDD需要由SparkContext来构建
		 *
		 * 二、SparkContext的主构造器参数是SparkConf
		 * SparkConf必须设置appname和master，否则会报错
		 * spark.master   用于设置部署模式
		 * local[*] == 本地运行模式[也可以是集群的形式]，如果需要多个线程执行，可以设置为local[2],表示2个线程 ，*表示多个
		 */

		SparkConf conf = new SparkConf().setAppName("NaiveBayesTest").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		List<LabeledPoint> train_list = new LinkedList<LabeledPoint>();
		String[] sentences = null;

		/**
		 * 0.0： nm papers
		 */
		String nmPapersQuestions = loadFile("/question/[0]nm_papers.txt");
		sentences = nmPapersQuestions.split("`");
		for (String sentence : sentences) {
			double[] array = sentenceToArrays(sentence);

//			for (int i = 0; i < array.length; i++) {
//				System.out.print(array[i] + " ");
//			}
//			System.out.println();

			LabeledPoint train_one = new LabeledPoint(0.0, Vectors.dense(array));
			train_list.add(train_one);
		}

		/**
		 *  1.0: kw papers
		 */
		String kwPapersQuestions = loadFile("/question/[1]kw_papers.txt");
		sentences = kwPapersQuestions.split("`");
		for (String sentence : sentences) {
			double[] array = sentenceToArrays(sentence);
			LabeledPoint train_two = new LabeledPoint(1.0, Vectors.dense(array));
			train_list.add(train_two);
		}

		/**
		 * 继续添加其他问题模板
		 * 2.0:
		 */

		/**
		 * SPARK的核心是RDD(弹性分布式数据集)
		 * Spark是Scala写的,JavaRDD就是Spark为Java写的一套API
		 * JavaSparkContext sc = new JavaSparkContext(sparkConf);    //对应JavaRDD
		 * SparkContext	    sc = new SparkContext(sparkConf)    ;    //对应RDD
		 */
		JavaRDD<LabeledPoint> trainingRDD = sc.parallelize(train_list);
		NaiveBayesModel nb_model = NaiveBayes.train(trainingRDD.rdd());

		/**
		 * 记得关闭资源
		 */
		sc.close();

		/**
		 * 返回贝叶斯分类器
		 */
		return nb_model;

	}

	private String loadFile(String filename) throws IOException {
		File file = new File(rootDirPath + filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String content = "";
		String line;
		while ((line = br.readLine()) != null){
			/**
			 * 暂时替代换行符 "~"
			 */
			content += line + "`";
		}

		/**
		 * 关闭资源
		 */
		br.close();
		return content;
	}


	public Map<Double,String> loadQuestionsPattern() {
		Map<Double,String>  questionPattern = new HashMap<>();
		File file = new File(rootDirPath + "/question/questionClassification.txt");
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
