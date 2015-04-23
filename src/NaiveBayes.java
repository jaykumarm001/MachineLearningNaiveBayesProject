

import java.io.BufferedReader;
import java.math.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Class for Naive Bayes
public class NaiveBayes {
	ArrayList<Integer> sLab=new ArrayList<Integer>();			//arraylist to store class count values
	ArrayList<Double> pLab=new ArrayList<Double>();		// arraylist to store individual class probability values
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO code application logic here

		NaiveBayes nb = new NaiveBayes();  // Object for Naive Bayes class
		nb.mle();		//Method called for MLE calculation
		nb.mapAndClassification();  // Method called for Map Calculation and Classification 

	}

	// Mle method implementation
	
	public void mle() throws FileNotFoundException, IOException {

		try {
			

			
			FileInputStream f1 = new FileInputStream("train.label");					// Loading train.label file
			BufferedReader trainLab = new BufferedReader(new InputStreamReader(f1));
			int tot=0;
			int i=1,cnt=0;
			String stm;
			while ((stm=trainLab.readLine()) != null) {
				int k=Integer.parseInt(stm);
				if(k==i)
				{
					cnt++;

				}
				else if(i<=20)                   // In this loop calculating calculating individual counts and
				{								// it will be stored into array list sLab

					i++;
					sLab.add(cnt);
					cnt=0;
					cnt++;

				}
			}
			sLab.add(cnt);
// mle calculations
			for(int t=0;t<20;t++){
				tot=tot+sLab.get(t);	
			}
			int t=0;
			System.out.println("Total number of documents in training data \n"+tot);
			System.out.println("Number of documents classified into each class from class 1 to class 20 as follows \n"+sLab);

			while(t<20)
			{
				pLab.add(Double.parseDouble(sLab.get(t).toString())/tot);		// In this loop calculating individual class probabilities
																							// it will be stored into arraylist pLab
				t++;
			}
			System.out.println("Calculated MLE values for each class");
			System.out.println(pLab);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Method implementation for map values and classification
	public void mapAndClassification() throws IOException{



		FileInputStream f2 = new FileInputStream("train.data");					// Loading train.data file
		BufferedReader tdata = new BufferedReader(new InputStreamReader(f2));
		ArrayList<String> stwcnt=new ArrayList<String>();						// arraylist to store word count values from train.data file
		ArrayList<String> stwid=new ArrayList<String>();						// arraylist to store word id values from train.data file
		ArrayList<String> stdid=new ArrayList<String>();						// arraylist to store document id values from train.data file
		int[][] clasword = new int[20][61188];									// Clasword array stores individual word count to their corresponding class and word ids
		int i=0;
		String str1;
		while((str1=tdata.readLine())!=null){
			//String str1;
			//str1 = tdata.readLine();
			//System.out.println("last value"+str1);								// This loop splits train.data file into three arraylists to store doc ids, word ids and word count
			String[] str2 = new String[3];

			str2=str1.split(" ");
			//System.out.println("last value change"+str2[0]+" "+str2[1]+" "+str2[2]);
			stwid.add(str2[1]);
			stdid.add(str2[0]);
			stwcnt.add(str2[2]);

		}
		//System.out.println("\narraylist is "+store);
		//System.out.println("\nfirst is "+store.get(0));


		FileInputStream f1 = new FileInputStream("train.label");				// Loading train.label file 
		BufferedReader tlabel = new BufferedReader(new InputStreamReader(f1));
		ArrayList<String> arrclas = new ArrayList<String>();					// array list to store train.label class values 
		String str4;
		//int c1=0;
		while((str4=tlabel.readLine())!=null){
			arrclas.add(str4);
			//System.out.println(str4);
		}
		//System.out.println(c1);

		FileInputStream f3 = new FileInputStream("train.data");
		BufferedReader tdata1 = new BufferedReader(new InputStreamReader(f3));
		int p,q,r,s=0;
		while(tdata1.readLine()!=null){
			p = Integer.parseInt(arrclas.get(Integer.parseInt(stdid.get(s))-1));		// This loop finds word count with respect to class id and word id store to clasword array
			q = Integer.parseInt(stwcnt.get(s));
			r = Integer.parseInt(stwid.get(s));
			clasword[p-1][r-1] = clasword[p-1][r-1]+q;
			//System.out.println(clasword[p-1][r-1]);
			s++;
		}
//		for(int l=0;l<20;l++){
//			for(int m=0;m<61188;m++){
//
//				System.out.print(clasword[l][m]+"--");		// This loop is used to print above calculated count values of each class
//			}
//
//			//System.out.print("\n");
//		}

		//calculating class word total sum
		int[] clsum = new int[20];				// clsum stores all sum of all word counts for respective classes
		for(int l=0;l<20;l++){
			int sum = 0;
			for(int m=0;m<61188;m++){			// This loop calculates sum of all word counts for individual classes
				sum+=clasword[l][m];			
			}
			clsum[l] = sum;
			//System.out.println(clsum[l]);
		}

		//calculating map probabilities
		double[][] mapProb = new double[20][61188];			//mapProb array is used to store calculated map values
		//System.out.println("map values");		
		for(int l=0;l<20;l++){
			for(int m=0;m<61188;m++){
				mapProb[l][m] = ((double)((double)clasword[l][m]+(double)(0.00001634307)))/((double)((double)clsum[l]+(((double)1))));		// This loop calculates map values each class and word ids
				
				//System.out.print(mapProb[l][m]+"--");
				
			}
			//System.out.println("\n");
		}
		
		// Matrix transpose
		double[][] tranMap = new double[61188][20];		// tranMap stores transpose of mapProb array logarithmic values
		//System.out.println(mapProb[2][3]);
		
		for(int l=0;l<20;l++){
			for(int m=0;m<61188;m++){
				
				double z = (Math.log(mapProb[l][m]))/(Math.log(2));		//This loop calculates logarithmic values of map values and will be transposed to store into tranMap array
				tranMap[m][l] = z;
				
				//System.out.println(tranMap[m][l]+"--");
			}
			//System.out.println("\n");
		}
		//System.out.println((Math.log(mapProb[2][3]))/(Math.log(2)));	
		//System.out.println(tranMap[3][2]+"--");
		//System.out.println(Math.log(0));
		
		
//		for(int l=0;l<20;l++){
//			for(int m=0;m<61188;m++){
//			
//				//System.out.print(mapProb[l][m]+"--");
//				
//			}
//			System.out.println("\n");
//		}
		//float d = (float)(clsum[1]+1);
		//System.out.println(clsum[1]);
		//System.out.println((float)clasword[9][745]);
		//System.out.println((1/(float)61188));
		//System.out.println((float)(clsum[1]+1));
		
		
		// Classification starts here
		
		FileInputStream f4 = new FileInputStream("test.data");						// Loading test.data file data
		BufferedReader tesdata = new BufferedReader(new InputStreamReader(f4));
		
		FileInputStream f5 = new FileInputStream("test.label");						// Loading test.label file data
		BufferedReader teslab = new BufferedReader(new InputStreamReader(f5));
		
		ArrayList<String> tlab = new ArrayList<String>();							// tlab array list stores test.label class labels
		
		String st;
		while((st=teslab.readLine())!=null){
			
			tlab.add(st);
			
		}
	
		ArrayList<String> tesdoc = new ArrayList<String>();						// This array list stores doc ids from test.data file 
		ArrayList<String> teswrd = new ArrayList<String>();						// This array list stores word ids from test.data file
		ArrayList<String> teswcnt = new ArrayList<String>();					// This array list stores word counts from test.data file
		int[][] testarr = new int[7505][61188];									// This array stores word counts for respective doc and word ids
		System.out.println("Please wait it will take 2 to 3 minutes time to print accuracy....");
		/*for(int k=0;k<7505;k++)
		{System.out.println(testarr[k]);}*/
	String str3;
		int w=0;
		while((str3=tesdata.readLine())!=null){
			//System.out.println(str3);
			String[] str = new String[3];
			str = str3.split(" ");
			tesdoc.add(str[0]);
			teswrd.add(str[1]);											// This loop calculates word counts for each corresponding doc and word ids
			teswcnt.add(str[2]);
			p = Integer.parseInt(tesdoc.get(w));
			q = Integer.parseInt(teswrd.get(w));
			r = Integer.parseInt(teswcnt.get(w));
			testarr[p-1][q-1] = testarr[p-1][q-1]+r;
//			System.out.println("--"+testarr[p-1][q-1]);
			w++;
		}
		
//		for(int l=0;l<7505;l++){
//			for(int m=0;m<61188;m++){
//				System.out.print(testarr[l][m]+"--");
//			}
//			System.out.println("\n");
//		}
		//System.out.println(pLab);
		ArrayList<Double> logmle = new ArrayList<Double>();			// logmle array list stored logarithmic mle values
		
		int x=0; 
		while(x<20){
			double y;
			y = (Math.log(pLab.get(x)))/Math.log(2);			// Logarithmic mle values calculation
			
			logmle.add(y);
			x++;
		}
		//System.out.println(logmle);
		//calculating mat for sum
		double[][] mleMat = new double[7505][20];				// This array stores mle values for each corresponding doc and class labels
		
		for(p=0;p<7505;p++){
			
			for(q=0;q<20;q++){
				
				mleMat[p][q] = logmle.get(q);
				//System.out.print(mleMat[p][q]+"--");
				
			}
			//System.out.println();
			
		}
	
	
		// Matrix multiplication to calculate product of count of each word from test data and map values from test data 
		
		double[][] mulMat = new double[7505][20];
		
		for (int a = 0; a < 7505; a++){
            for (int b = 0; b < 20; b++){
                for (int c = 0; c < 61188; c++){
                    mulMat[a][b] += ((double)testarr[a][c] * tranMap[c][b]);					// Here product of count of each word from test data and map values from test data will be calculated
                   // System.out.println("docid"+(a+1)+" class "+(b+1)+"  "+mulMat[a][b]+"--"); // it will be stored to mulMat array
		}
	}
}
		// Total Sum for classification
		
		double[][] csumMat = new double[7505][20];				// This array stores sum of logarithmic mle values and above calculated product values
		
		for (i = 0; i < 7505; i++){
            for (p = 0; p < 20; p++){
                csumMat[i][p] = mleMat[i][p] + mulMat[i][p];
                //System.out.println("docid"+(i+1)+" class "+(p+1)+"  "+csumMat[i][p]+"--");		// Here sum of each logarithmic mle and above calculated product values for 
                //System.out.println("docid"+(i+1)+" class "+(p+1)+"  "+mulMat[i][p]+"--");			// corresponding doc and class labels
            }
           //System.out.println();
            }
		
		//System.out.println(csumMat[7504][19]+"0,0 mat mul value---");
		
		
		
		//calculating maximum class 
		
		ArrayList<Integer> cltesarr = new ArrayList<Integer>();		// This array list stores class labels which have maximum value for each doc id
		int u=0;
		for(p=0;p<csumMat.length;p++){
			
			double max = csumMat[p][0];
																	// Here class labels with maximum classification value for each doc id will be calculated
			for(q=0;q<csumMat[p].length;q++){
				
				if (max<csumMat[p][q]) {
					
					max = csumMat[p][q];
					u=q+1;
				}
				
			}
			
			if(max==csumMat[p][0]){
			
				u=1;
			}
			
			//System.out.println("u values"+u);
			cltesarr.add(u);
			
		}

		//System.out.println("classes"+cltesarr);
		
		
		// Accuracy calculation 
		
		//FileInputStream f6 = new FileInputStream("test.label");
		//BufferedReader teslab = new BufferedReader(new InputStreamReader(f5));
		
	//	ArrayList<String> tlab = new ArrayList<String>();
		
	
	// Calculating percentage accuracy value	
		q=0;
		for(p=0;p<7505;p++){
			
			if (cltesarr.get(p)==(Integer.parseInt(tlab.get(p)))) {			// Here we calculate count of matched class labels from test.label and classification class labels
				q++;
			}
			
			
		}
		
		double m = (double)(q)/7505;
		System.out.println("Percentage Accuracy is \n"+(m*100));				//  Here we calculate percentage accuracy value with the help of count value
	
		// Calculating confusion matrix
		int[] cntLab = new int[20];										// This array stores number of documents belonging to each class from test.label file data
		int[] cntCal = new int[20];										// This array stores number of documents belonging to each class from classification data
		
		int[][] conMat = new int[20][20];								// This array stores confusion matrix values
		
		for(p=0;p<7505;p++){
			
			if (cltesarr.get(p)==(Integer.parseInt(tlab.get(p)))){
				
				conMat[Integer.parseInt(tlab.get(p))-1][cltesarr.get(p)-1] = conMat[Integer.parseInt(tlab.get(p))-1][cltesarr.get(p)-1] + 1;
			
				cntLab[Integer.parseInt(tlab.get(p))-1]++;
				cntCal[cltesarr.get(p)-1]++;								// Here we calculate confusion matrix values for each class from test.label and classification data
				
			}
			else{
				
				conMat[Integer.parseInt(tlab.get(p))-1][cltesarr.get(p)-1] = conMat[Integer.parseInt(tlab.get(p))-1][cltesarr.get(p)-1] + 1;
				
				cntLab[Integer.parseInt(tlab.get(p))-1]++;
				
			
			}
			
		}
		
		// Printing confusion matrix
		
		System.out.println("confusion matrix as follows\n");
		
		for(i=0;i<20;i++){
			for (int j = 0; j < conMat.length; j++) {
		          System.out.print(conMat[i][j]+"	");
		          
			}
			
			System.out.println();
		}
		
		// news group confusion values
		
		System.out.println("news group classes    Actual test label classification       calculated classification    		difference bwn them	percentage classification");
		
		for(p=0;p<20;p++){
			
			System.out.println((p+1)+"  			   "+cntLab[p]+"    		 	  "+cntCal[p]+"    			   	 "+(cntLab[p]-cntCal[p])+"						"+((((double)cntCal[p]/(double)cntLab[p]))*100));
			
		}
		
	// Calculating top hundred words  
		
		double[] rankMat = new double[61188];
		
		for(i=0;i<20;i++){
			for(p=0;p<61188;p++){
				
				rankMat[p]+=(pLab.get(i)*mapProb[i][p]);	// Multiplies MLE values with MAP values
															// which will give each word probability values with respect to all the words but not per category probability
			}
			
			
		}
		int[] comp = new int[61188];
		
		boolean swapped = true;
	    int j = 0;
	    double tmp;
	    while (swapped) {
	        swapped = false;
	        j++;											// Here using Bubble sort algorithm, word id will be stored into “comp” array of size 61188  
	        for (i = 0; i < rankMat.length - j; i++) {		// in the descending order of their corresponding
	            if (rankMat[i] > rankMat[i + 1]) {			// word probability values which will give rank for each word
	                tmp = rankMat[i];
	                rankMat[i] = rankMat[i + 1];
	                rankMat[i + 1] = tmp;
	                swapped = true;
	            }
	        }
	    comp[j-1]=(i+1);
	    
	    }
		
	    FileInputStream f6 = new FileInputStream("vocabulary.txt");						// Loading vocabulary.txt file data
		BufferedReader vocdata = new BufferedReader(new InputStreamReader(f6));
		
		String stv;
		ArrayList<String> vocarr = new ArrayList<String>();
		
		while((stv=vocdata.readLine())!=null){					//array list all words of vocabulary.txt
			
			vocarr.add(stv);
		}
		System.out.println("top hundred words");
		for(i=0;i<100;i++){
			String vocprt;										// Logic for printing top 100 words
			vocprt = vocarr.get(comp[i]-1);
			   System.out.println((i+1)+". "+vocprt);
		
		}
	}


}

