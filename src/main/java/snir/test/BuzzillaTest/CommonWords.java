package snir.test.BuzzillaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * A CommonWords represents the result of a list of words that most common in a list of sites. 
 * @author snir
 *
 */
public class CommonWords {

	
	/**
	 * Return a list of words that most common in the specified url List. 
	 * the size of the list will be as the specified amount.
	 * this method is use callbeck and it will return the result after SiteParser will finish to run.
	 * @param amountOfWords 
	 * @param urls
	 * @param amount
	 * @return
	 * @throws Exception 
	 */
	public List<Entry<String,Integer>> getCommonWords(List<String> urlList, int amountOfWords) throws Exception{
		SiteParser siteParser = new SiteParser(urlList);
		FutureTask<Map<String, Integer>> future = new FutureTask<Map<String, Integer>>(siteParser);
		future.run();
		try {
			return getSorted(future.get(2, TimeUnit.MINUTES), amountOfWords);
		} catch (Exception e) {
			siteParser.endAll();
			throw e;
		}
		
	
	}
	
	/**
	 * Sort the value of the map to find the most popular words
	 * @param wordMap 
	 * 
	 * @param wordMap
	 * @return
	 */
	private List<Entry<String, Integer>> getSorted(Map<String, Integer> wordMap, int amount) {

		Set<Entry<String, Integer>> set = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		return list.size() > amount ? list.subList(0, amount) : list;
	
	}
}
