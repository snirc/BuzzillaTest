package snir.test.BuzzillaTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SiteParser is reading a list of sites and put the words in a map that present the word and the amount of appearance 
 * It use Callable to return the results in the end of the job
 * 
 * @author snir
 *
 */
public class SiteParser implements Callable<Map<String, Integer>> {
	
	private Map<String, Integer> wordMap;
	private int thradEnded = 0;
	private int thradAmount = 0;
	private List<String> urlList;
	private boolean isRunningTrad = true;
	
	
	/**
	 * 
	 * @param urlList
	 */
	public SiteParser(List<String> urlList) {
		this.urlList = urlList;
		this.wordMap = new HashMap<String, Integer>();
		this.thradAmount = urlList.size();
	}
	

	/**
	 * Running list of threads and return the result in the end
	 */
	public Map<String, Integer> call() throws Exception {
		for(final String url : urlList) {
			new Thread(){
			    public void run(){
			      try {
					parsUrl(url);
			      } catch (Exception e) {
					e.printStackTrace();
			      }			  
			    }
				
			}.start();
		}
		
		//waiting to all thread to finish their job
		while(isRunningTrad)
			Thread.sleep(50);
		return wordMap;
		
	}
	
	/**
	 * Mark the isRunningTrad flag to false to end waiting and return the results
	 */
	public void endAll() {
		this.isRunningTrad = false;
	}
	
	/**
	 * Read the asite content from the specifed url and pars it to map 
	 * @param url
	 */
	public void parsUrl(String url) {
		try {
			URL oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(inputLine, " ");
				while (st.hasMoreTokens()) {
					String tmp = st.nextToken().toLowerCase().trim();
					//if the word is invalid it will continue to the next word
					if(isInvalidWord(tmp))
						continue;
					//lock the words map
					synchronized(wordMap) {
						if (wordMap.containsKey(tmp)) {
							wordMap.put(tmp, wordMap.get(tmp) + 1);
						} else {
							wordMap.put(tmp, 1);
						}
					}
					
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			thradEnded++;
			//check if it is the last thread    
			if(thradEnded == thradAmount)
				isRunningTrad  = false;
			
		}
		
	}

	/**
	 * check if the word is valid and not contain special characters and numbers
	 * TODO check prepositions 
	 * @param word
	 * @return
	 */
	private boolean isInvalidWord(String word) {
		if(word.length() < 3)
			return true;
		
		Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
	      Matcher m = p.matcher(word);
	    return m.find();
	         
	}

}
