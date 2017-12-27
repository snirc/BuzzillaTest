package snir.test.BuzzillaTest;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

public class CommonWordsTest {
	private static String[] urls = {"https://en.wikipedia.org/wiki/The_Beatles", 
									"https://en.wikipedia.org/wiki/Rock_and_roll", 
									"https://en.wikipedia.org/wiki/Blues"};
	private static int amountOfWords = 30;

	@Test
	public void test() {
		CommonWords commonWords = new CommonWords();
		try {
			List<Entry<String,Integer>> entryList = commonWords.getCommonWords(Arrays.asList(urls), amountOfWords);
			for(Entry<String,Integer> wordAmount : entryList) {
				System.out.println(wordAmount.getKey() + " : " + wordAmount.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}
