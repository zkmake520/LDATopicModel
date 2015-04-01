import java.io.*;
import java.util.*;
public class LDAModel {
	private int numTopics,iterations,startSaveIters,step;
	private double alpha,beta;
	private int docCount,termCount;
	public int[][] topicAssign;
	private int[][] numDocTopic;     //give doc,count the topic k in this doc
	private int[][] numTopicTerm;     //give topic, count the  term in topic k
	private int[] sumTopicTerm;  //sum of hits for one topic
	private int[] sumDocTopic; // sum of topic for one doc
	private double[][] topicTermDis; // distribution of term in topic
	private double[][] docTopicDis; // distribution of topic in doc
	private int[][] doc;
	public LDAModel(Docs docs){
		beta=0.1;
		numTopics = 100;
		iterations = 50;
		startSaveIters=40;
		step = 3;
		docCount = docs.docs.size();
		termCount = docs.word2Index.size();
		alpha = 50/numTopics;
		numDocTopic = new int[docCount][numTopics];
		numTopicTerm = new int[numTopics][termCount];
		sumTopicTerm = new int[numTopics];
		sumDocTopic = new int[docCount];
		topicTermDis = new double[numTopics][termCount];
		docTopicDis = new double[docCount][numTopics];
		//get all doc 
		doc = new int[docCount][];
		for(int i = 0; i < docCount; i++){
			int termCount = docs.docs.get(i).words.length;
			doc[i] = new int[termCount];
			for(int j =0; j < termCount;j++){
				doc[i][j] = docs.docs.get(i).words[j];
			}
		}
		//random assign a topic to each term;
		topicAssign = new int[docCount][];
		for (int i = 0;i < docCount;i++){
			int termCount = docs.docs.get(i).words.length;
			topicAssign[i] = new int[termCount];
			for(int j =0; j < termCount; j++){
				int topic = (int)(Math.random()*numTopics);
				numDocTopic[i][topic]++;
				numTopicTerm[topic][doc[i][j]]++;
				sumTopicTerm[topic]++;
			}
			sumDocTopic[i]=termCount;
		}
		
	}
	public int getTopic(int m,int n){
		int oldTopic = topicAssign[m][n];
		numDocTopic[m][oldTopic]--;
		sumDocTopic[m]--;
		numTopicTerm[oldTopic][doc[m][n]]--;
		sumTopicTerm[oldTopic]--;
		int term = doc[m][n];
		double [] probability = new double[numTopics];
		for(int i =0; i < numTopics;i++){
			probability[i] = ((double)numDocTopic[m][i]/sumDocTopic[m])*((double)numTopicTerm[i][term]/sumTopicTerm[i]);
		}
		//assign a new topic to the term
		//we can normalize the probability or we can resample directly
		for(int i = 1; i < numTopics;i++){
			probability[i] += probability[i-1];
		}
		double seed= Math.random()*probability[numTopics];
		int newTopic=-1;
		for(int i =0;i < numTopics;i++){
			if(seed < probability[i]){
				newTopic = i;
				break;
			}
		}
		numDocTopic[m][newTopic]++;
		sumDocTopic[m]++;
		numTopicTerm[newTopic][doc[m][n]]++;
		sumTopicTerm[newTopic]++;
		return newTopic;
	}
	public void updateDistribution(){
		for(int m =0; m < docCount;m++){
			for(int k =0;k < numTopics; k++){
				docTopicDis[m][k]=(double)(numDocTopic[m][k]+alpha)/(sumDocTopic[m]+numTopics*alpha);
				
			}
		}
		for(int k =0; k < numTopics;k++){
			for(int t =0;t < termCount; t++){
				topicTermDis[k][t]=(double)(numTopicTerm[k][t]+beta)/(sumTopicTerm[k]+termCount*beta);
			}
		}
	}
	public void saveModel(Integer iter) throws IOException{
		updateDistribution();
		BufferedWriter writer = new BufferedWriter(new FileWriter("out/lda_results"+iter.toString()+".txt"));
		writer.write("Documents topics distribution:\n");
		for(int k = 0; k < )
		for(int m =0; m < docCount;m++){
			
		}
		
	}
	public void inferenceModel(Docs docs){
		for(int iter = 0;iter < iterations; iter++){
			if(iter > startSaveIters && (iter-startSaveIters)%step == 0){
				saveModel(iter);
			}
			for(int m = 0; m < docCount; m++){
				int termCount = docs.docs.get(m).words.length;
				for(int n =0; n < termCount;n++){
					int newTopic = getTopic(m,n);
					topicAssign[m][n] = newTopic;
				}
			}
		}
		return;
	}
	
}
