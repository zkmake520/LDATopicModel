// first create class that can represent the all documents
import java.util.*;
import java.util.regex.*;
import java.io.*;
;
public class Docs{
	ArrayList<Doc> docs;
	Hashtable<String,Integer> word2Index = new Hashtable<String,Integer>();
	Hashtable<Integer,String> index2Word = new Hashtable<Integer,String>();
	String defaultStopwordPath = "stoplists/en.txt";
	HashSet<String> stopword = new HashSet<String>();
	public Docs() throws IOException{
		loadStopword();
		docs = new ArrayList<Doc>();
	}
	public void addDocs(String filePath) throws IOException{
		File dir = new File(filePath);
		String[] files = dir.list();
		for(String fileName: files){
			Doc doc = new Doc(fileName);
		}	
	}
	public Doc getDocs(String fileName) throws IOException{
		for(int i = 0;i < docs.size();i++){
			if(docs.get(i).getDocName() == fileName)
				return docs.get(i);
		}
		return null;
	}
	public void tokenizeAndLowercase(String aux,ArrayList<String>content){
		aux.toLowerCase();
	    String[] auxSplit = aux.split("[^\\w']+");
	    for(int i =0;i < auxSplit.length;i++){
	    	content.add(auxSplit[i]);
	    }
		return;
	}
	public void loadStopword() throws IOException{
		loadStopword(defaultStopwordPath);
		return;
	}
	public void loadStopword(String stopwordPath) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopwordPath))));
		String aux ="";
		while((aux=reader.readLine())!=null){
			stopword.add(aux);
		}
		return;
	}
	public void removeStopword(ArrayList<String>content){
		for(int i =0; i < content.size();i++){
			if(stopword.contains(content.get(i))){
				content.remove(i);
				i--;
			}
		}
	}
	public  class Doc{
		private String fileName;
		int[] words;
		public Doc(String fileName) throws IOException{
			ArrayList<String> content = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)),"UTF-8"));
			String aux ="";
			while((aux = reader.readLine())!=null){
				tokenizeAndLowercase(aux,content);
				removeStopword(content);
			}
			//index the word in content
			words = new int[content.size()];
			for(int i =0;i < content.size();i++){
				if(!word2Index.containsKey(content.get(i))){
					int index = word2Index.size();
					word2Index.put(content.get(i), index);
					index2Word.put(index, content.get(i));
					words[i] = index;
				}
				else{
					words[i] = word2Index.get(content.get(i));
				}
			}
			content.clear();
		}
		public String getDocName(){
			return fileName;
		}
	}
}