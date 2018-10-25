intelligent-paper-search  
---  
Spring-Boot + Neo4j + Spark的朴素贝叶斯分类器实现石油相关论文的智能分析问答系统  
---
#### 写在前面  
打算仿照[Spring-Boot-Neo4j-Movies](https://github.com/kobeyk/Spring-Boot-Neo4j-Movies)做一个基于石油相关论文的智能分析系统。
分析了下这个电影知识问答系统，底层功能实现是操作cypher语句，前台的业务：   
1.汉语分词器HanLP将原始语句分词  
2.语句抽象化(提高匹配问题模板标签准确率)  
3.获取模板标签，使用模板将句子转化成系统可以识别的结果   
4.cypher语句获取结果返回前台  
既然涉及问答系统，中途也看了微软小冰和其他的语料库资料，感觉自己做出一个偏向应用的石油相关智能问答系统的可能性不大，首先自己不做
爬虫就语料库这个问题也解决不了的，要真有现成的语料库那也就没我做的必要了。  
#### 区别
对比自己想做的石油论文智能分析系统，我的数据来源都是国外网站，用户的原始语句是英文就用不到分词，但词汇库就复杂了，需要自己去找英
文人名词汇表，提取论文信息生成全文搜索词汇表。所以对这个项目我抱的期望不是很大，先罗列几个比较困难的点，做出来更新：  
1.项目中通过稠密向量来生成训练集，而每个局部向量是由词汇表来确定的，电影知识问答系统中是个190词的电影相关汉语词汇表，但石油相关
词汇都是英语网站的数据，所以词汇表内容都是英语词汇，解决办法是在有些数据后生成这个表，但是搞爬虫的同学还在准备中期考试 ~、~   
2.问题归类，英语比较吃力了，同样的一个问题怎么来问，同一个问题预设问法越多，模型在学习后识别同类问题的准确率才会更高。   
3.。。。


