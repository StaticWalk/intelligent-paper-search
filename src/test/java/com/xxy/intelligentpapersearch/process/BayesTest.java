package com.xxy.intelligentpapersearch.process;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiongxiaoyu
 * Data:2018/10/31
 * Time:20:36
 */
public class BayesTest {


	@Test
	public void vectorTest(){

		/**
		 * 本地模式，*表示启动多线程并行计算
		 */
		SparkConf conf = new SparkConf().setAppName("NaivaBayesTest").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		/**
		 * MLlib本地向量分两种，DenseVector、SparseVector
		 * 前者保存稠密向量，后者保存稀疏向量
		 * 稠密向量表示方式：连续的指标
		 * 稀疏向量表示方式：向量维度len 、 值为1的index的int[] 、和index相同长度的值全为1的double[]
		 * 稀疏向量的性能更高！
		 *
		 * 长发（0） 短发（1） 口红（2） 高跟鞋（3） 喉结（4） 肩膀宽（5）
		 *
		 * vMale	 (0.0, 1.0, 0.0, 0.0, 1.0, 1.0)
		 * vFemale 	 (1.0, 0.0, 1.0, 1.0, 0.0, 0.0)
		 *
		 */
		//稠密向量表示  连续的
		Vector vMale = Vectors.dense(0,1,0,0,1,1);

		//稀疏向量   间隔的、指定的、未指定位置的向量默认0.0
		int len = 6;
		int[] index = new  int[]{0,2,3};
		double[] values =new double[]{1,1,1};
		//索引0、2、3位置上的向量值=1，索引1、4、5没给出，默认0
		Vector vFemale = Vectors.sparse(len, index, values);

		/**
		 * labeledPoint 是局部向量，表示方式可以稠密也可是稀疏
		 * 构成每个标记点的时候需要用一个label/response进行关联
		 * MLlib中 labeled points被用来监督学习算法
		 * 每个label都用double来存储，可以使用labeled points进行回归和分类
		 * 在二进制里面，label可以0或者1
		 */

		//训练集生成 ，规定数据结构为LabeledPoint == 构建方式:稠密向量模式  ，1.0:类别编号 == 男性
		LabeledPoint train_one = new LabeledPoint(1.0,vMale);  // (0.0, 1.0, 0.0, 0.0, 1.0, 1.0)
		//训练集生成 ，规定数据结构为LabeledPoint == 构建方式:稀疏向量模式  ，2.0:类别编号 == 女性
		LabeledPoint train_two = new LabeledPoint(2.0,vFemale); //(1.0, 0.0, 1.0, 1.0, 0.0, 0.0)
		//我们也可以给同一个类别增加多个训练集
		LabeledPoint train_three = new LabeledPoint(2.0, org.apache.spark.mllib.linalg.Vectors.dense(0,1,1,1,0,1));

		//List存放训练集【三个训练样本数据】
		List<LabeledPoint> trains = new ArrayList<>();
		trains.add(train_one);
		trains.add(train_two);
		trains.add(train_three);

		/**
		 * SPARK的核心是RDD(弹性分布式数据集)
		 * Spark是Scala写的,JavaRDD就是Spark为Java写的一套API
		 * JavaSparkContext sc = new JavaSparkContext(sparkConf);    //对应JavaRDD
		 * SparkContext	    sc = new SparkContext(sparkConf)    ;    //对应RDD
		 * 数据类型为LabeledPoint
		 */
		JavaRDD<LabeledPoint> trainingRDD = sc.parallelize(trains);

		/**
		 * 利用Spark进行数据分析时，数据一般要转化为RDD
		 * JavaRDD转Spark的RDD
		 */

		NaiveBayesModel nb_model = NaiveBayes.train(trainingRDD.rdd());

		//测试集  有口红有高跟鞋
		double [] dTest = {0,0,1,1,0,0,};
		Vector vTest = Vectors.dense(dTest);

		//使用朴素贝叶斯
		int modelIndex = (int) nb_model.predict(vTest);
		System.err.println("标签标号" +modelIndex);

		/**
		 * 计算测试目标向量与训练样本数据集里面对应的各个分类标签匹配的概率结果
		 */
		System.err.println(nb_model.predictProbabilities(vTest));

		if(modelIndex == 1){
			System.err.println("答案：贝叶斯分类器推断这个人的性别是男性");
		}else if(modelIndex == 2){
			System.err.println("答案：贝叶斯分类器推断这个人的性别是女性");
		}

		//释放资源
		sc.close();

	}

}
