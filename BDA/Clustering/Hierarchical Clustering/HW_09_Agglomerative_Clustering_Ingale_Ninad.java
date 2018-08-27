import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

public class HW_09_Agglomerative_Clustering_Ingale_Ninad {

	static double [][] inputData;

	static double [][] distanceMatrix;

	static HashSet<Integer> alreadyVisited=new HashSet<Integer>();

	static HashMap<String, String> clusterList=new HashMap<String,String>();
	
	static double[][] hashMapValue;

	static ArrayList<String> hashMapKey=new ArrayList<String>();
	
	static int hashSetIndex;

	/*  
	 * 	This method is responsible for reading the data from the file. 
	 */
	public static double[][] readData(){
		try 
		{
		  Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW09-Agglomerative Clustering/HW_09_SHOPPING_CART_v037.csv")));
		  inputScanner.nextLine();
		  ArrayList <String> stringInput=new ArrayList<String>();
		  while(inputScanner.hasNextLine()){
			  String input=inputScanner.nextLine();
			  stringInput.add(input);
		  }
		  
		  int rowNumber=0;
		 
		  String[] inputString=new String[stringInput.size()];
		 
		  inputData=new double[stringInput.size()][stringInput.get(1).split(",").length];
		  
		  for (int i=0;i<stringInput.size();i++){
			  inputString=stringInput.get(i).split(",");
			  for (int j=0;j<inputString.length;j++){
				 inputData[i][j]=Double.parseDouble(inputString[j]); 
			  }
		  }
		  
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputData;
	}
	
	
	/*
	 * This method will recursively executes the clustering process.
	 */
	public static void buildCluster(){
		
			int count=0;
			while(inputData.length>1){
			String minDistanceFromMatrix=null; 
			String minDistanceMatrixHashMap=null;
			String minDistanceFromHashMap=null;
			String[] inputString;
			String [][]newKeyValue=null;
			
			if (clusterList.size()==0){
				
				System.out.println("Iteration "+count);
				
				
				minDistanceFromMatrix=buildDistanceMatrix(inputData);
				
//				System.out.println(minDistanceFromMatrix);
				
				inputString=minDistanceFromMatrix.split("\\|");
				
				for (int i=0;i<inputString.length;i++){
					System.out.println(inputString[i]);
				}
				
				
				buildClusterList(inputString[0],inputString[1]);
				System.out.println("Cluster :"+inputString[0]);
				buildNewMatrix(inputString[0]);
				
				System.out.println("******************************");
			}
			else
			{
				System.out.println("Iteration "+count);
				minDistanceFromMatrix=buildDistanceMatrix(inputData);
				minDistanceMatrixHashMap=minDistanceMatrixHashMap(hashMapValue,inputData);
				minDistanceFromHashMap=mindistanceHashMap(hashMapValue);
				
				  ArrayList <String> stringInput=new ArrayList<String>();
				  stringInput.add(minDistanceFromMatrix);
				  stringInput.add(minDistanceMatrixHashMap);
				  stringInput.add(minDistanceFromHashMap);

				  inputString=new String[stringInput.size()];
					 
				  newKeyValue=new String[stringInput.size()][stringInput.get(1).split("\\|").length];
				  
				  
				  for (int i=0;i<stringInput.size();i++){
					  inputString=stringInput.get(i).split("\\|");
					  for (int j=0;j<inputString.length;j++){
						  newKeyValue[i][j]=inputString[j]; 
					  }
				  }
				  
				  for (int i=0;i<stringInput.size();i++){
					  inputString=stringInput.get(i).split("\\|");
					  for (int j=0;j<inputString.length;j++){
						  System.out.print(newKeyValue[i][j]+"\t"); 
					  }
					  System.out.println();
				  }
				  
//				  System.out.println(clusterList);
//				  System.out.println();
//				  System.out.println();
				  
				  double min=9999;
				  int index=0;

				  for (int i=0;i<newKeyValue.length;i++){
					  if (Double.parseDouble(newKeyValue[i][2])<min & Double.parseDouble(newKeyValue[i][2])!=0){
						  min=Double.parseDouble(newKeyValue[i][2]);
						  index=i;
					  }
				  }
				  
				  System.out.println(min);
				  System.out.println("Cluster :"+newKeyValue[index][0]);
				  
				  	
				  	
					buildClusterList(newKeyValue[index][0],newKeyValue[index][1]);
					buildNewMatrix(newKeyValue[index][0]);

				  
//				System.out.println(minDistanceMatrixHashMap);
					
//					System.out.println("H to M"+minDistanceMatrixHashMap);
				System.out.println("******************************");
			}
			
			count++;
		}
	}

	
	private static String mindistanceHashMap(double[][] inputMatrix) {
		// TODO Auto-generated method stub
		double min=9999;
		int cluster1=0;
		int cluster2=0;
		String key=null;
		String value=null;
		
			for (int i=0;i<inputMatrix.length-1;i++)
			{
				for (int j=i+1;j<inputMatrix.length;j++)
				{
					double distance=0;
			
					for (int k=1;k<inputMatrix[0].length;k++)
					{
						distance+=(inputMatrix[i][k]-inputMatrix[j][k])*(inputMatrix[i][k]-inputMatrix[j][k]);
					}
					distance=Math.sqrt(distance);
//					System.out.println(distance);
					if (distance<min){
						min=distance;
						cluster1=i;
						cluster2=j;
					}
				}
			}

			value="";
			for (int k=1;k<inputMatrix[0].length;k++){
				value+=((inputMatrix[cluster1][k]+inputMatrix[cluster2][k])/2)+",";
			}
			
			
			key=cluster1+","+cluster2;
			value=value.substring(0, value.length()-1);
			
//			clusterList.remove(hashMapKey.get(cluster1));
			
			String pushResultToBuildCluster=key+"|"+value+"|"+min;
			
//			System.out.println(clusterList);
//			System.out.println(pushResultToBuildCluster);
//			Build the cluster list.
//			buildClusterList(key,value);
			
		
//		Build the new matrix by removing the already clustered data points.
//		buildNewMatrix(key);
		
		return pushResultToBuildCluster;

		
//		return null;
	}


	private static String minDistanceMatrixHashMap(double[][] hashMaptoArray,double [][] inputMatrix) {
		// TODO Auto-generated method stub
//		String minDistanceMatrixHashMap=null;

		double min=9999;
		String cluster1=null;
		int cluster2=0;
		String key=null;
		String value=null;
		int index=0;

		System.out.println(hashMaptoArray[0][0]);
		
		for (int i=0;i<hashMaptoArray.length;i++)
		{
			for (int j=0;j<inputMatrix.length;j++)
			{
				double mindistance=0;
		
				for (int k=1;k<inputMatrix[0].length;k++)
				{
					mindistance+=(hashMaptoArray[i][k]-inputData[j][k])*(hashMaptoArray[i][k]-inputData[j][k]);
//					value+=((hashMaptoArray[i][k]+inputMatrix[j][k])/2)+",";
				}
				mindistance=Math.sqrt(mindistance);
//				System.out.println(hashMapKey.get(i)+"|"+j+"|"+mindistance);
//				System.out.println(i);
//				System.out.println(");
				if (mindistance<min){
					min=mindistance;
//					cluster1=hashMapKey.get(i);
					index=i;
					cluster2=j;
				}
			}
		}

		
		value="";
		for (int k=1;k<inputMatrix[0].length;k++){
			value+=((hashMaptoArray[index][k]+inputData[cluster2][k])/2)+",";
		}
		
		
		key=hashMapKey.get(index)+","+cluster2;
		
		
		
//		System.out.println("*******"+hashMapKey.get(index));
		value=value.substring(0, value.length()-1);
		
		clusterList.remove(hashMapKey.get(index));
		hashMapKey.remove(index);
//		System.out.println("Key : "+key);
//		System.out.println("Value :"+value);
//		System.out.println("min distance from hashmap to matrix :"+min);
		
//		buildClusterList(key,value);
		
		String pushResultToBuildCluster=key+"|"+value+"|"+min;
//		System.out.println(pushResultToBuildCluster);
		
		return pushResultToBuildCluster;

	}


	public static String buildDistanceMatrix(double[][] inputMatrix){

		double min=9999;
		int cluster1=0;
		int cluster2=0;
		String key=null;
		String value=null;
		
			for (int i=0;i<inputMatrix.length-1;i++)
			{
				for (int j=i+1;j<inputMatrix.length;j++)
				{
					double distance=0;
			
					for (int k=1;k<inputMatrix[0].length;k++)
					{
						distance+=(inputMatrix[i][k]-inputMatrix[j][k])*(inputMatrix[i][k]-inputMatrix[j][k]);
					}
					distance=Math.sqrt(distance);
//					System.out.println(distance);
					if (distance<min){
						min=distance;
						cluster1=i;
						cluster2=j;
					}
				}
			}

			value="";
			for (int k=1;k<inputMatrix[0].length;k++){
				value+=((inputMatrix[cluster1][k]+inputMatrix[cluster2][k])/2)+",";
			}
			
			
			key=cluster1+","+cluster2;
			value=value.substring(0, value.length()-1);
		
//			clusterList.remove(hashMapKey.get(cluster1));
			
//			System.out.println("Key : "+key);
//			System.out.println("Value :"+value);
//			System.out.println("min distance from matrix :"+min);
			
			String pushResultToBuildCluster=key+"|"+value+"|"+min;
//			System.out.println(pushResultToBuildCluster);
//			Build the cluster list.
//			buildClusterList(key,value);
			
		
//		Build the new matrix by removing the already clustered data points.
//		buildNewMatrix(key);
		
		return pushResultToBuildCluster;
	}
	
	
	private static void buildClusterList(String key,String value) {
		// TODO Auto-generated method stub
	//	clusterList=new HashMap<String,String>();
		clusterList.put(key, value);
//		System.out.println("-----------------------------------------------------------");
//		System.out.println(clusterList);
//		System.out.println("-----------------------------------------------------------");
//		displayClusters();

		hashSetIndex=0;
//		Build hash set for visited nodes
		alreadyVisited=new HashSet<Integer>();
		String[] visitedClusters=key.split(",");
		for (int i=hashSetIndex;i<visitedClusters.length;i++)
		{
			alreadyVisited.add(Integer.parseInt(visitedClusters[i]));
//			hashSetIndex++;
		}
		
		
		
		
//		System.out.println(x);
//		java.util.Iterator<Integer> hashSetIterator=alreadyVisited.iterator();
//		while (hashSetIterator.hasNext()){
//			System.out.println(hashSetIterator.next());
//		}
				
		hashMapValue=new double[clusterList.size()][inputData[0].length];
		
		
		
		hashMapKey.add(key);
		
		
		
		String[] strHashMapValue=new String[clusterList.size()];
		for (String name:clusterList.keySet()){
//			System.out.println("Key : "+name.toString());
//			String value=clusterList.get(name).toString();
//			System.out.println("Value :"+clusterList.get(name).toString());
			strHashMapValue=clusterList.get(name).toString().split(",");
		}
		
		
//		System.out.println("******************************************************************************************");
		int i=0;
		for (Entry<String, String> index:clusterList.entrySet())
		{
			hashMapValue[i][0]=(double)i;
//			System.out.print(hashMapValue[i][0]+"\t");
			strHashMapValue=index.getValue().toString().split(",");
			for (int j=1;j<hashMapValue[0].length;j++)
			{
				hashMapValue[i][j]=Double.parseDouble(strHashMapValue[j-1]);
//				System.out.print(hashMapValue[i][j]+"\t");
			}
//			System.out.println();
		}
//		System.out.println(clusterList);
//		System.out.println("******************************************************************************************");
	}


	private static void buildNewMatrix(String key) {
		// TODO Auto-generated method stub
		Double[][] newMatrix=new Double[inputData.length-alreadyVisited.size()][inputData[0].length];		
		int index=0; 
		boolean checkIfPresent=false; 	
		for (int i=0;i<inputData.length;i++){		
			checkIfPresent=false;			
			for (int j=0;j<newMatrix[0].length;j++){
				
				if (alreadyVisited.contains(i)){
					checkIfPresent=true;
					break;
				}
				
				else{
					newMatrix[index][j]=inputData[i][j];
				}
			}
			
			if(!checkIfPresent){
				index++;
			}
		}
	
		inputData=new double[newMatrix.length][newMatrix[0].length];
		
		for (int i=0;i<newMatrix.length;i++){
			for (int j=0;j<newMatrix[0].length;j++){
				inputData[i][j]=newMatrix[i][j];
			}
		}
		
		
//		Method to display the new matrix built.		
		
//		displayData(inputData);
	}


	public static void displayClusters(){
		int rowNumber=1;
		for (String name:clusterList.keySet()){
			System.out.println("Cluster "+rowNumber+" :");
			System.out.println("Key : "+name.toString());
			System.out.println("Value :"+clusterList.get(name).toString());
			rowNumber++;
		}
	}
	
	
	public static void displayData(double[][] inputMatrix){
		for (int i=0;i<inputMatrix.length;i++){
			for (int j=0;j<inputMatrix[0].length;j++){
				System.out.print(inputMatrix[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double [][] matrix=readData();
//		displayData(matrix);
//		buildDistanceMatrix(matrix);
		buildCluster();
	}
}
