package com.xxy.intelligentpapersearch.process;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiongxiaoyu
 * Data:2018/10/27
 * Time:12:09
 */
public class ModelProcessTest {

	public static void main(String[] args) {
		ModelProcess a=new ModelProcess();
		System.out.println(a.questionsPattern);
	}



	@Test
	public void SentenceDetect() throws IOException {
		String paragraph = "Hi. How are you? This is Mike.";

		// always start with a model, a model is learned from training data
		InputStream is = new FileInputStream("opennlp/en-sent.bin");
		SentenceModel model = new SentenceModel(is);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);

		String sentences[] = sdetector.sentDetect(paragraph);

		System.out.println(sentences[0]);
		System.out.println(sentences[1]);
		is.close();
	}


	@Test
	public void Tokenize() throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("opennlp/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize("Hi. How are you? This is Mike.");
		for (String a : tokens)
			System.out.println(a);
		is.close();
	}


	@Test
	public void findPersonName() throws IOException {
		InputStream is = new FileInputStream("opennlp/en-ner-person.bin");

		TokenNameFinderModel model = new TokenNameFinderModel(is);
		is.close();

		NameFinderME nameFinder = new NameFinderME(model);

		String []sentence = new String[]{
				"is","a","Mike","Smith","good","Mike","person","Smith","Gerald","R. Coulter"
		};

		Span nameSpans[] = nameFinder.find(sentence);
		for(Span s: nameSpans)
				System.out.println(s.getStart()+ ""+s.length());

	}



}