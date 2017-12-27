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

public class SiteParser implements Callable<Map<String, Integer>> {
	
	private Map<String, Integer> wordMap;
	private int thradEnded = 0;
	private int thradAmount = 0;
	private List<String> urlList;
	private boolean isRunningTrad = true;
	
	public SiteParser(List<String> urlList) {
		this.urlList = urlList;
		this.wordMap = new HashMap<String, Integer>();
		this.thradAmount = urlList.size();
	}
	

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
		
		
		while(isRunningTrad)
			Thread.sleep(50);
		return wordMap;
		
	}
	
	public void endAll() {
		this.isRunningTrad = false;
	}
	
	public void parsUrl(String url) {
		try {
			URL oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(inputLine, " ");
				while (st.hasMoreTokens()) {
					String tmp = st.nextToken().toLowerCase().trim();
					if(hasSpecialCharacters(tmp))
						continue;
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
			if(thradEnded == thradAmount)
				isRunningTrad  = false;
			
		}
		
	}

	
	private boolean hasSpecialCharacters(String word) {
		if(word.length() < 3)
			return true;
		
		Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
	      Matcher m = p.matcher(word);
	    return m.find();
	         
	}

}
