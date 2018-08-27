import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * This class holds the nodes of the decision tree. 
 */
class Node
{
	double attribute;
	double threshold;
	Node left; 
	Node right;
	String value;
	Node(){};
	Node(double attr, int thr)
	{
		attribute=attr;
		threshold=thr;
		value="";
	}
}

/**
 * @author Ninad Ingale
 * Date : 25 Feb 2016
 *
 */
public class HW_04_Ingale_Ninad_Trainer {
//	static ArrayList<Double[]> data=new ArrayList<Double[]>();
	static String[] stringInput;
	static double[][] doubleInput;
	static int rowNo;
	static double[][] frequency;
	static double [][]threshold;
	static double maxInfoGain;
	
	Node root;
	static int interationCount;
	/**
	 * This method reads the file and sets the input in the format 
	 * required by the program. i.e. it reads a csv file and stores
	 * it in a 2D array named doubleInput. 
	 */	
	private static void initialize() {		
		rowNo=0;
		doubleInput=new double[450][5];
		try {
			Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW04-Decision Tree/HW_04_DecTree_Training__v101.csv")));
			inputScanner.nextLine();
			while (inputScanner.hasNextLine()){
				String input=inputScanner.nextLine();				
				stringInput=input.split(",");
				for (int i=0;i<stringInput.length;i++){
					doubleInput[rowNo][i]=Double.parseDouble(stringInput[i]);
				}
				rowNo++;
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * This method is responsible for building the decision tree recursively 
	 * 
	 *  Parameters passed :	
	 * 						Node node  An object of class Node which holds our root at 
	 * 								   each recursion level.
	 * 						int thr    The threshold value for a particular attribute
	 * 								   during each iteration. 	
	 * 						Filewriter Write the tree into a file. 
	 */	
	static Node buildTree(Node node, int thr, FileWriter writer){
		interationCount++;
		
		double size=doubleInput.length;
		if (size<=((0.1)*450) || thr<1)
		{
			Node n=new Node();
			n.value="0";
			return n; 
		}
		
		System.out.println("Iteration "+interationCount);
		String result=buildFrequencyMatrix(thr);
		String[] contents=result.split(",");
		int attribute=Integer.parseInt(contents[1]);
		int limit=(int)Double.parseDouble(contents[2]);
		
		try {
			writer.append(contents[1]+","+contents[2]);
			writer.append("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Build a new matrix and fetch the smaller data everytime 
		//recursion takes place. 
		constructMatrix(attribute,limit);
		
		// If node is null , treat it as root. 
		if (node==null){
			node=new Node(attribute,limit);
			Node n=new Node();
			n.value="1";
			node.left=buildTree(node,limit,writer);
			node.right=n;
			return node; 
		}
		else 
			// If node is not null , go till the leaf and add a new node. 
		{
			Node head=node;
			Node n=new Node();
			n.value="1";
			while (head.left!=null)
				head=head.left;
			head=new Node(attribute,limit);
			head.left=buildTree(node,limit,writer);
			head.right=n;
			return head; 
		}		
	}
	
	/*
	 *	Build a new matrix and fetch the smaller data everytime 
	 *	recursion takes place.
	 *	Parameters passed 
	 *			int attr : The attribute with maximum information gain
	 *					   for that particular iteration. 
	 *			int thr  : Threshold for the attribute.	 
	 */
	static void constructMatrix(int attr,int thr){
		int count=0;
		for (int i=0;i<doubleInput.length;i++){
			if (doubleInput[i][attr]<(double)thr)
				count++;
		}
		
		System.out.println("Number of data elements remaining "+count);
		double[][] matrix=new double[count][5];
		count=0;
		for (int i=0;i<rowNo;i++){
			if (doubleInput[i][attr]<thr){
				for (int j=0;j<5;j++){
					matrix[count][j]=doubleInput[i][j];
				}
				count++;
			}
		}
		
		doubleInput=null;
		doubleInput=new double[count][5];
		for (int i=0;i<count;i++){
			for (int j=0;j<5;j++){
				doubleInput[i][j]=matrix[i][j];
			}
		}

		rowNo=doubleInput.length;
	}
	
	
   /*
	*	This method builds the frequency matrix for each attribute.
	*	Parameter Passed : 
	*
	*					int thr : Current Threshold value 	
	*/
	static String buildFrequencyMatrix(int thr) {
		// TODO Auto-generated method stub
		double [][] threshold=new double[4][4];
		frequency=new double[thr][5];
		int min=0;
		for (int i=0;i<thr;i++)
		{
			frequency[i][0]=min;
			min++;
		}
		
		//Frequency for Attribute 1
		int count=0;
		for (int i=0;i<thr-1;i++)
		{
			count=0;
			for (int j=0;j<rowNo-1;j++){					
				if (doubleInput[j][0]>=frequency[i][0]&&doubleInput[j][0]<frequency[i+1][0])
					count++;
			}
			frequency[i][1]=count; 
		}
		
		//Frequency for Attribute 2
		count=0;
		for (int i=0;i<thr-1;i++)
		{
			count=0;
			for (int j=0;j<rowNo-1;j++){					
				if (doubleInput[j][1]>=frequency[i][0]&&doubleInput[j][1]<frequency[i+1][0])
					count++;
			}
			frequency[i][2]=count; 
		}
		
		//Frequency for Attribute 3
		count=0;
		for (int i=0;i<thr-1;i++)
		{
			count=0;
			for (int j=0;j<rowNo-1;j++){					
				if (doubleInput[j][2]>=frequency[i][0]&&doubleInput[j][2]<frequency[i+1][0])
					count++;
			}
			frequency[i][3]=count; 
		}

		//Frequency for Attribute 4
		count=0;
		for (int i=0;i<thr-1;i++)
		{
			count=0;
			for (int j=0;j<rowNo-1;j++){					
				if (doubleInput[j][3]>=frequency[i][0]&&doubleInput[j][3]<frequency[i+1][0])
					count++;
			}
			frequency[i][4]=count; 
		}

		String result=null;
		double maxInfoGain=0;
		int index=0;
		double thresholdValue=0; 
		for (int i=0;i<4;i++){
			result=findthreshold(i,thr);
			String [] val=result.split(",");
			if (Double.parseDouble(val[1])>maxInfoGain){
				maxInfoGain=Double.parseDouble(val[1]);
				index=Integer.parseInt(val[0]);
				thresholdValue=Double.parseDouble(val[2]);
			}
		}		
		
		System.out.println("Attribute "+(index+1));
		System.out.println("threshold "+thresholdValue);
		System.out.println("********************************");
		return maxInfoGain+","+index+","+thresholdValue;		
	}

	
	/*
	 * This method is responsible for calculating threshold for each attribute
	 * in each recursion and calculating the minimum misclassification rate for
	 * each attribute.
	 * 
	 * Parameters Passed :
	 * 					int attrIndex : Index of the attribute in the file. 
	 * 					int thr : Threshold for the same attribute. 						
	 */
	private static String findthreshold(int attrIndex, int thr) {
		
		double max=0;
		for (int i=0;i<doubleInput.length;i++){
			{
				if (max<doubleInput[i][attrIndex])
					max=doubleInput[i][attrIndex];
			}
		}
		
		int frequencyMatrixsize=(int)Math.ceil(max);
//		System.out.println("max "+max);
		threshold=new double[4][4];
		double [][]frequencyMatrix=new double[frequencyMatrixsize][6];
		// TODO Auto-generated method stub
		
		int min=0;
		for (int i=0;i<frequencyMatrix.length;i++)
		{
			frequencyMatrix[i][0]=min;
			min++;
		}
		
		//TP
		for (int i=0;i<frequencyMatrix.length;i++)
		{
			int truePositive=0;
			for (int j=0;j<rowNo;j++){					
				if (doubleInput[j][attrIndex]>=i
						&&doubleInput[j][attrIndex]<(i+1) &&doubleInput[j][4]==0)
					truePositive++;
			}
			frequencyMatrix[i][1]=truePositive;				
		}

		//FP
		for (int i=0;i<frequencyMatrix.length;i++)
		{
			int falsePositive=0;
			for (int j=0;j<rowNo;j++){					
				if (doubleInput[j][attrIndex]>=i
						&&doubleInput[j][attrIndex]< (i+1) &&doubleInput[j][4]==1)
//					if (doubleInput[j][attrIndex]<frequency[i][0]&&doubleInput[j][4]==1)

					falsePositive++;
			}
//			System.out.println(" ");
			frequencyMatrix[i][2]=falsePositive;
		}
		
		//FN
		double falseNegative=frequencyMatrix[0][1];
		for (int i=0;i<frequencyMatrix.length;i++)
		{
			falseNegative=0;
			for (int j=i+1;j<frequencyMatrix.length;j++)
			{
				falseNegative+=frequencyMatrix[j][1];
			}
			frequencyMatrix[i][3]=falseNegative;
		}

		
		//TN
		falseNegative=frequencyMatrix[0][2];
		for (int i=0;i<frequencyMatrix.length;i++)
		{
			falseNegative=0;
			for (int j=i+1;j<frequencyMatrix.length;j++)
			{
				falseNegative+=frequencyMatrix[j][2];
			}
			frequencyMatrix[i][4]=falseNegative;
		}

//		frequency[0][3]=0;
		
		double truePositive=frequencyMatrix[0][1];
		for (int i=1;i<frequencyMatrixsize;i++)
		{
			truePositive+=frequencyMatrix[i][1];
			frequencyMatrix[i][1]=truePositive;
		}


//		frequency[0][3]=0;
		
		double falsePositive=frequencyMatrix[0][2];
		for (int i=1;i<frequencyMatrixsize;i++)
		{
			falsePositive+=frequencyMatrix[i][2];
			frequencyMatrix[i][2]=falsePositive;
		}

		double minVal=9999; 
		double thresholdValue=0;
		
		for (int i=0;i<frequencyMatrixsize-1;i++){
			double  minMissclassificationRate=0;
			minMissclassificationRate=
					(frequencyMatrix[i][1]+frequencyMatrix[i][4])/
					(frequencyMatrix[i][1]+frequencyMatrix[i][2]+frequencyMatrix[i][3]+frequencyMatrix[i][4]);
			if (minMissclassificationRate<minVal)
			{
				minVal=minMissclassificationRate;
			thresholdValue=frequencyMatrix[i][0];
			}
		}

		maxInfoGain=0;
		String result=null;
		threshold[attrIndex][0]=thresholdValue;
		threshold[attrIndex][1]=calculateEntropy(frequencyMatrix,(int)thresholdValue,attrIndex);
		
			result=attrIndex+","+threshold[attrIndex][1]+","+threshold[attrIndex][0];
		return result;
	}
	
	/*
	 * This method is responsible for calculating entropy at each attribute for respective
	 * thresholds. 
	 * 
	 * Parameters Passed :
	 * 					double[][] : frequency matrix built in the previous method. 
	 * 					int thresholdValue : Threshold for the same attribute.
	 * 					int attrIndex : index of the attribute.  						
	 */				    
	
	private static double calculateEntropy(double[][] frequencyMatrix, 
			int thresholdValue, int attrIndex) {
		// TODO Auto-generated method stub
		
		double total_entropy=0; 
		double total_no_of_0=frequencyMatrix[frequencyMatrix.length-1][1]+
				frequencyMatrix[frequencyMatrix.length-1][3];
		double total_no_of_1=frequencyMatrix[frequencyMatrix.length-1][2]+
				frequencyMatrix[frequencyMatrix.length-1][4];
		
		total_entropy=(((-1)*(total_no_of_0/(total_no_of_0+total_no_of_1)))*(Math.log(total_no_of_0/(total_no_of_0+total_no_of_1)))/(Math.log(2)))
				+(((-1)*(total_no_of_1/(total_no_of_0+total_no_of_1)))*(Math.log(total_no_of_1/(total_no_of_0+total_no_of_1)))/(Math.log(2)));
				
		double entropy_on_left= 
				(((-1)*(frequencyMatrix[thresholdValue][1]/(frequencyMatrix[thresholdValue][1]+frequencyMatrix[thresholdValue][2])))*(Math.log(frequencyMatrix[thresholdValue][1]/(frequencyMatrix[thresholdValue][1]+frequencyMatrix[thresholdValue][2])))/(Math.log(2)))
				+(((-1)*(frequencyMatrix[thresholdValue][2]/(frequencyMatrix[thresholdValue][1]+frequencyMatrix[thresholdValue][2])))*(Math.log(frequencyMatrix[thresholdValue][2]/(frequencyMatrix[thresholdValue][1]+frequencyMatrix[thresholdValue][2])))/(Math.log(2)));

		if (Double.isNaN(entropy_on_left)){
			entropy_on_left=0;
		}
		
		double entropy_on_right= 
				(((-1)*(frequencyMatrix[thresholdValue][3]/(frequencyMatrix[thresholdValue][3]+frequencyMatrix[thresholdValue][4])))*(Math.log(frequencyMatrix[thresholdValue][3]/(frequencyMatrix[thresholdValue][3]+frequencyMatrix[thresholdValue][4])))/(Math.log(2)))
				+(((-1)*(frequencyMatrix[thresholdValue][4]/(frequencyMatrix[thresholdValue][3]+frequencyMatrix[thresholdValue][4])))*(Math.log(frequencyMatrix[thresholdValue][4]/(frequencyMatrix[thresholdValue][3]+frequencyMatrix[thresholdValue][4])))/(Math.log(2)));

		if (Double.isNaN(entropy_on_right)){
			entropy_on_right=0;
		}
		
		
		double no_of_elements_on_left=frequencyMatrix[thresholdValue][1]+frequencyMatrix[thresholdValue][2];
		double no_of_elements_on_right=frequencyMatrix[thresholdValue][3]+frequencyMatrix[thresholdValue][4];
		
		double informationGain=total_entropy-(((no_of_elements_on_left/rowNo)*entropy_on_left)+
								((no_of_elements_on_right/rowNo)*entropy_on_right));

		return informationGain;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer=new FileWriter("E:/BDA Homework/HW04-Decision Tree/tree.csv");	
		HW_04_Ingale_Ninad_Classifier classifier=new HW_04_Ingale_Ninad_Classifier(); 
		initialize();
		HW_04_Ingale_Ninad_Trainer t=new HW_04_Ingale_Ninad_Trainer();
		t.root=buildTree(t.root,11,writer);
	
		writer.flush();
		writer.close();
		classifier.trainer(t.root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
}
