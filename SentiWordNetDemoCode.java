import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
public class SentiWordNetDemoCode {

	private Map<String, Double> dictionary;
	private static final String FILENAME = "Folder\\Opposites.txt";
	private static final String FILENAME2 = "Folder\\Learning.txt";

	public SentiWordNetDemoCode(String pathToSWN) throws IOException {
            
		// This is our main dictionary representation

		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}

	public double extract(String word, String pos) {
		try{ 
			return dictionary.get(word + "#" + pos);
		 }
		 catch(NullPointerException e){
		 	return 0;
		 }
		}
	
	public static void main(String [] args) throws IOException 
	{
		
		System.out.println("Enter A String");
		Scanner in=new Scanner(System.in);
		String m=in.nextLine();
		String learn="";


		for(String word : Split.words(m))
   		{
   			learn+=word;
   			learn+=" ";
   		}
   		//learn+="- ";

   		  	
	        String[] splitArray = m.split(" ");
	        double scores[]=new double[splitArray.length];
	     int numberOfSplittedCharacters=splitArray.length;
	     int addOn=0;

	     if(splitArray[numberOfSplittedCharacters-1].contains("!"))
	     {
	     	addOn=30;
	     }
	     else
	     	if (splitArray[numberOfSplittedCharacters-1].contains(".")) 
	     	{
	     		addOn=20;
	     	}
	 
	     	if (splitArray[numberOfSplittedCharacters-1].contains("..")) 
	     	{
	     		addOn=60;
	     	}
	     


		String pathToSWN = "E:\\STUDIES\\PROJECTS\\Major 2\\Java\\Folder\\SentiWordNet_3.0.0.txt";
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);

		
		for(int i=0;i<splitArray.length;i++)
		{	
			int count=0;
			scores[i] =sentiwordnet.extract(splitArray[i], "n");
			scores[i]+=sentiwordnet.extract(splitArray[i], "v");
			scores[i]+=sentiwordnet.extract(splitArray[i], "a");
			scores[i]+=sentiwordnet.extract(splitArray[i], "r");
			if(sentiwordnet.extract(splitArray[i], "n")==0)
				count++;
		
			if(sentiwordnet.extract(splitArray[i], "v")==0)
				count++;

			if(sentiwordnet.extract(splitArray[i], "a")==0)
				count++;

			if(sentiwordnet.extract(splitArray[i], "r")==0)
				count++;

			if(count==4)
				scores[i]=0;
			else
				scores[i]/=(4-count);

			//System.out.println(splitArray[i] + ":" + scores[i]);
		
		}
		float positive=0,negative=0;
		for(int j=0;j<scores.length;j++)
		{
			if(scores[j]<0)
				negative++;
			else if(scores[j]>0)
				positive++;
		}

		float possibilty=(negative*positive)!=0 ? (negative*positive) : 1;



		String[] conjunction ={"After","Although","As","As If","As Long As","Because","Before","But","And","Or","Either",
					"Even If","Even Though If","Once Provided","Since","So That",
					"That","Though","Till","Unless","Until","What","When","Whenever","Wherever",",",
					"Whether","While","Accordingly","Also","Anyway","Besides","Consequently",
					"Finally","For Example","For Instance","Further","Furthermore",
					"Hence","However","Incidentally","Indeed","In Fact","Instead","Likewise","Meanwhile","Moreover","OfCourse","Untill",
					"On the Contrary","On the Other Hand","Otherwise","Nevertheless","Nonetheless","Similarly","So Far","Until Now","Still","Then","Therefore","Thus" };
		
		int k=0;
		String[] breakers=new String[10];
		int breakerPosition[]=new int[10];

		for(int i=0;i<splitArray.length;i++)
		{
			for(int j=0;j<conjunction.length;j++)
			{
				if(splitArray[i].equalsIgnoreCase(conjunction[j]))
				{
					breakers[k]=splitArray[i];
					breakerPosition[k]=i;        
					k++;
				}
			}
		}
 		
 		float freq=0;
 		int l=0,l2=0;
 		//System.out.println(breakers[0] + " "+ breakerPosition[0]);
 		//System.out.println(k+1);

 		while(l<k+1)
 		{
	 		for(int i=0;i<breakerPosition[l];i++)
	 		{
	 			for(int j=breakerPosition[l];j<scores.length;j++)
	 			{
	 				if((scores[i]<0 && scores[j]>0)||(scores[i]>0 && scores[j]<0))
	 				{
	 					//System.out.print(scores[i]+" ");
	 					//System.out.println(scores[j]);
	 					freq++;
	 				}
	 			}
	 		}
	 		l++;
 		}

 		float contradictiveProbability=freq/possibilty;

 		System.out.println("Final Probability of having Contradictive Sarcasm:" + contradictiveProbability);
 		//System.out.println(freq2);
 		double posdif[]=new double[5000];
 		int p=0;
 		float total=0,over8=0;

 		while(l2<k+1)
 		{
		     for(int i=0;i<breakerPosition[l2];i++)
				{
					for(int j=breakerPosition[l2]+1;j<scores.length;j++)
					{
						total++;
						posdif[p]=Math.abs(Math.abs(scores[i])-Math.abs(scores[j]));
						//System.out.println("Score and their location"+(i+1)+" "+(j+1)+" "+posdif[p]);
						p++;
					}
				}
			l2++;
		}
			for(int i=0;i<p;i++)
			{
				if(posdif[i]>0.4)
				{
					over8++;
					//System.out.println("Which are more than 80%"+posdif[i]);
				}
			}

			//System.out.println(total+" "+over8);
			float positiveProbability=(total>0)?(over8/total) : 0;
			System.out.println("Final Probability of having Positive Sarcasm:"+ positiveProbability);

			BufferedReader br = null;
			FileReader fr = null;
			float oppositeProbability=0;


				for(int j=0;j<splitArray.length;j++)
				{
					try 
					{
						fr = new FileReader(FILENAME);
						br = new BufferedReader(fr);

						String sCurrentLine;

						br = new BufferedReader(new FileReader(FILENAME));
						int flag=0;
						while ((sCurrentLine = br.readLine()) != null) 
						{
							String[] splitData=sCurrentLine.split(" ");
							//System.out.println(splitData[0] + " "+ splitData[2] + " "+splitArray[j]);
							if(splitData[0].equalsIgnoreCase(splitArray[j]))
							{	//System.out.println("Yes");

								for(int i=0;i<splitArray.length;i++)
								{
									
										if(splitArray[i].equalsIgnoreCase(splitData[2]))
										{
											//System.out.println("Yes 1");
											oppositeProbability+=1;
											//flag=1;
											break;
										}
									
								}
							}
							else
								
									if(splitData[2].equalsIgnoreCase(splitArray[j]))
									{
										//System.out.println("Yes 2");
										for(int i=0;i<splitArray.length;i++)
										{
											if(splitArray[i].equalsIgnoreCase(splitData[0]))
											{
												//System.out.println("Yes 3");
												oppositeProbability+=1;
												//flag=1;
												break;
											}
										}
									}
									
						}

					} 

					catch (IOException e) 
						{
							e.printStackTrace();
						} 

					finally 
					{
						try 
						{
							if (br != null)
								br.close();

							if (fr != null)
								fr.close();

						} 
						catch (IOException ex) 
						{

							ex.printStackTrace();

						}

					}

				}
			

			/*if(oppositeProbability%2==0)
			{	
				oppositeProbability=(oppositeProbability)/2;
			}
			else
				if(oppositeProbability%2!=0)
					oppositeProbability=(oppositeProbability-1)/2;

			else if(oppositeProbability==0)
			{
				oppositeProbability=0;
			}
			else
				oppositeProbability=0;*/

			//System.out.println("No. Of Anatonym Pairs in the text: "+ oppositeProbability/2);

			float bayesianProbability= contradictiveProbability*9/10;
			bayesianProbability+=positiveProbability*5/10;
			oppositeProbability/=2;
			bayesianProbability+=oppositeProbability;
			bayesianProbability/=(1.4+oppositeProbability);
			bayesianProbability*=100;
			bayesianProbability+=addOn;

			System.out.println("Probability Of Overall Irony And Sarcasm is:" + bayesianProbability + "%");
			learn+=bayesianProbability;
			learn+="\n";


			//Training algorithm 

			int typeOfSarcasm;
			String[] trainCheck=learn.split(" ");

						fr = new FileReader(FILENAME2);
						br = new BufferedReader(fr);

						String sCurrentLine;

						br = new BufferedReader(new FileReader(FILENAME2));
						float maxEqualCount=0;
						int maxLineIndex=0;
						int lineIndex=1; 

						while ((sCurrentLine = br.readLine()) != null) 
						{	
							String[] splitTrainData=sCurrentLine.split(" ");
							int countEqual=0;
							for(int i=0;i<trainCheck.length;i++)
							{
								for(int j=0;j<splitTrainData.length;j++)
								{
									if(trainCheck[i].equalsIgnoreCase(splitTrainData[j]))
									{
										countEqual++;
									}
								}
							}
							if(maxEqualCount<=countEqual)
							{
								maxEqualCount=countEqual;
								maxLineIndex=lineIndex;
							}

							lineIndex++;
						}

			//System.out.println(maxEqualCount);
			//System.out.println(maxLineIndex);

			double equalityProb=maxEqualCount/trainCheck.length;
			//System.out.println(equalityProb);


				fr = new FileReader(FILENAME2);
				br = new BufferedReader(fr);

				String currentLineForMean;

				br = new BufferedReader(new FileReader(FILENAME2));
				float sumMean=0;
				double mean=0;
				int linesWithNonZeroResult=0;
				while ((currentLineForMean = br.readLine()) != null) 
						{	
							String[] splitTrainDataForMean=currentLineForMean.split(" ");
							if(Float.valueOf(splitTrainDataForMean[splitTrainDataForMean.length-1])!=0.0)
							{	
								linesWithNonZeroResult++;
								sumMean+=Float.valueOf(splitTrainDataForMean[splitTrainDataForMean.length-1]);
							}
						}

				mean=(sumMean/linesWithNonZeroResult>0)?sumMean/linesWithNonZeroResult:0;

				//System.out.println(mean);

				fr = new FileReader(FILENAME2);
				br = new BufferedReader(fr);

				String currentLineForVariance;

				br = new BufferedReader(new FileReader(FILENAME2));
				float sumVariance=0;
				float variance=0;
				double currentVariance=0;
				double currentVarianceSquare=0;
				float sumChiDifference=0;
				//int linesWithNonZeroResult=0;

				while ((currentLineForVariance = br.readLine()) != null) 
						{	
							String[] splitTrainDataForVariance=currentLineForVariance.split(" ");
							if(Float.valueOf(splitTrainDataForVariance[splitTrainDataForVariance.length-1])!=0.0)
							{	
								//linesWithNonZeroResult++;
								currentVariance=Math.abs(mean-Float.valueOf(splitTrainDataForVariance[splitTrainDataForVariance.length-1]));
								currentVarianceSquare=Math.pow(currentVariance,2);
								double currentValue=Float.valueOf(splitTrainDataForVariance[splitTrainDataForVariance.length-1]);
								double numerator=Math.pow((currentValue-mean),2);
								sumChiDifference+=numerator/currentVarianceSquare;
								sumVariance+=currentVariance;
							}
						}

				variance=(sumVariance/linesWithNonZeroResult>0)?sumVariance/linesWithNonZeroResult:0;

				//System.out.println(sumChiDifference);
				//Find Chi Squared differences

			if(equalityProb >= 0.75)
			{	
				//System.out.println("Case 1");
				fr = new FileReader(FILENAME2);
				br = new BufferedReader(fr);
				double tfIdf=0;
				String currentLineForEdit;

				br = new BufferedReader(new FileReader(FILENAME2));
				int counterForEdit=0;



					while ((currentLineForEdit = br.readLine()) != null) 
						{	
							counterForEdit++;
							//System.out.println(counterForEdit);
							if(counterForEdit==maxLineIndex)
							{
								String[] splitTrainDataForEdit=currentLineForEdit.split(" ");
								int lengthOfEditString=splitTrainDataForEdit.length;
								float probToEdit=Float.valueOf(splitTrainDataForEdit[lengthOfEditString-1]);
								//System.out.println(probToEdit);
								float maxtermFrequency=trainCheck.length;
								float termFrequency=maxEqualCount;
								double tfScore= 0.5+(equalityProb/2);
								double idfScore= Math.log(equalityProb);
								tfIdf=tfScore*idfScore;
								//System.out.println(tfIdf);

							}
							
						}

						double precision= (tfIdf/2)+(sumChiDifference/2);

						//double newProbability= bayesianProbability-precision;

						//System.out.println(newProbability);

						fr = new FileReader(FILENAME2);
						br = new BufferedReader(fr);

						String currentLineForWrite;
						String newString="";

						br = new BufferedReader(new FileReader(FILENAME2));
						int counterForEditAgain=0;
						while ((currentLineForWrite = br.readLine()) != null) 
						{	
							counterForEditAgain++;
							if(counterForEditAgain!=maxLineIndex)
							{	
		
								newString+=currentLineForWrite;
								newString+="\n";
							}
							else
							{	
								int pos=currentLineForWrite.lastIndexOf(' ');
								newString+=currentLineForWrite.substring(0,pos);
								newString+=" ";
								String[] p1=currentLineForWrite.split(" ");
								double newProbability=Double.valueOf(p1[p1.length-1])-precision;
								newString+=Double.toString(newProbability);
								newString+="\n";
							}
							


						}

					//System.out.println(newString);

				BufferedWriter bw = null;
				FileWriter fw = null;
				try 
				{

					File file = new File(FILENAME2);

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					// true = append file
					fw = new FileWriter(file.getAbsoluteFile(),false);
					bw = new BufferedWriter(fw);

					bw.write(newString);

				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}

				finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

			}
			else if(equalityProb > 0.45 && equalityProb < 0.75)
			{
				//System.out.println("Case 2");
				fr = new FileReader(FILENAME2);
				br = new BufferedReader(fr);
				double tfIdf=0;
				String currentLineForEdit;

				br = new BufferedReader(new FileReader(FILENAME2));
				int counterForEdit=0;



					while ((currentLineForEdit = br.readLine()) != null) 
						{	
							counterForEdit++;
							//System.out.println(counterForEdit);
							if(counterForEdit==maxLineIndex)
							{
								String[] splitTrainDataForEdit=currentLineForEdit.split(" ");
								int lengthOfEditString=splitTrainDataForEdit.length;
								float probToEdit=Float.valueOf(splitTrainDataForEdit[lengthOfEditString-1]);
								// System.out.println(probToEdit);
								float maxtermFrequency=trainCheck.length;
								float termFrequency=maxEqualCount;
								double tfScore= 0.5+(equalityProb/2);
								double idfScore= Math.log(equalityProb);
								tfIdf=tfScore*idfScore;
								// System.out.println(tfIdf);

							}
							
						}

						double precision= (tfIdf/2)+(sumChiDifference/2);

						//double newProbability= bayesianProbability-precision;

						//System.out.println(newProbability);

						fr = new FileReader(FILENAME2);
						br = new BufferedReader(fr);

						String currentLineForWrite;
						String newString="";

						br = new BufferedReader(new FileReader(FILENAME2));
						int counterForEditAgain=0;
						while ((currentLineForWrite = br.readLine()) != null) 
						{	
							counterForEditAgain++;
							if(counterForEditAgain!=maxLineIndex)
							{	
		
								newString+=currentLineForWrite;
								newString+="\n";
							}
							else
							{	
								int pos=currentLineForWrite.lastIndexOf(' ');
								newString+=currentLineForWrite.substring(0,pos);
								newString+=" ";
								String[] p1=currentLineForWrite.split(" ");
								double newProbability=Double.valueOf(p1[p1.length-1])-precision;
								newProbability*=0.5;
								newProbability+=0.5*bayesianProbability;
								newString+=Double.toString(newProbability);
								newString+="\n";
							}
							


						}

					//System.out.println(newString);

				BufferedWriter bw = null;
				FileWriter fw = null;
				try 
				{

					File file = new File(FILENAME2);

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					// true = append file
					fw = new FileWriter(file.getAbsoluteFile(),false);
					bw = new BufferedWriter(fw);

					bw.write(newString);

				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}

				finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}
		}
			else
			{
				BufferedWriter bw = null;
				FileWriter fw = null;
				try 
				{

					File file = new File(FILENAME2);

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					// true = append file
					fw = new FileWriter(file.getAbsoluteFile(), true);
					bw = new BufferedWriter(fw);

					bw.write(learn);

				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}

				finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
		}


		
	}
 		
}
