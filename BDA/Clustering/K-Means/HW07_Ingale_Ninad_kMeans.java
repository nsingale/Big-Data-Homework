import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 */

/**
 * @author Ninad
 *
 */
public class HW07_Ingale_Ninad_kMeans {
	
	static int count=0;
	static String[] stringValues;
	static double[][] doubleValues;
	static HashMap<Integer,String> centroids=new HashMap<Integer,String>();
	static double[][] SSEArray=new double[20][2];
	static double[][] SS=new double[20][2];
	
	/*
	 * Recursively calculate the centroid value for each cluster and update the
	 * centroid value. 
	 */
	public static void recursiveMean(){
		calculateCentroid();
		buildClusters(centroids);
	}
	
	
	private static void readFile() {
		// TODO Auto-generated method stub
		count=0;
		doubleValues=new double[501][3];
		try {
			Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW07-K-Means/HW_07_KMEANS_DATA_v300.csv")));
			while (inputScanner.hasNextLine()){
				String input=inputScanner.nextLine();				
				stringValues=input.split(",");
				for (int i=0;i<stringValues.length;i++){
					doubleValues[count][i]=Double.parseDouble(stringValues[i]);
				}
				count++;
			}	
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 	Centroid 
	 */
	public static void calculateCentroid(){
		String clusterCentroids=String.valueOf(1);
		centroids.put(1, clusterCentroids);
		
	//	int currentPoint=1; 
		
		for (int i=1;i<5;i++){
		
			int currentPoint=1;
			String centroidValue=String.valueOf(currentPoint);
			int interval=count/(i+1);
			currentPoint=currentPoint+interval;
			centroidValue=centroidValue+"~"+String.valueOf(currentPoint);
			centroids.put(i, centroidValue);
			// For more than 2 clusters
			if ((i+1)>2)
			{
				for (int j=i;j>=2;j--)
				{
//					interval=interval*j;
					currentPoint+=interval;
//					System.out.println(interval);
					centroidValue+="~"+String.valueOf(currentPoint);
					centroids.put(j, centroidValue);
				}
			}
//			System.out.print(i+1+"\t");
//			System.out.println(centroidValue);			
		}
	}
	
	
	public static void buildClusters(HashMap<Integer,String> centroidList){
//		 HashMap<Integer,ArrayList<String>> clusterList=new HashMap<Integer,ArrayList<String>>();
		HashMap<Integer,HashSet<String>> clusterList=new HashMap<Integer,HashSet<String>>();
		for (Entry<Integer, String> result:centroids.entrySet()){
//		 System.out.println(result.getKey()+"\t"+result.getValue());
		 String[] clusters=result.getValue().split("~");
		 double total;
//		 double min=9999;
		 HashSet<String> str1=new HashSet<String>();
		 for (int i=0;i<count;i++){
			 int index=0;
			 int minFromData=0;
			 double min=9999;
			 String str=null;
//			 ArrayList<String> arrayList=new ArrayList<String>();
			 HashSet<String> arrayList= new HashSet<String>();
			 for (int j=0;j<clusters.length;j++){
				 double distance=0;
//				 System.out.println(doubleValues[Integer.parseInt(clusters[j])-1][0]+"\t"+
//						 			doubleValues[Integer.parseInt(clusters[j])-1][1]+"\t"+
//						 			doubleValues[Integer.parseInt(clusters[j])-1][2]);
//				 if (i!=Integer.parseInt(clusters[j])){
					 
				  distance=
						  ((doubleValues[i][0]-doubleValues[Integer.parseInt(clusters[j])-1][0])*
						  (doubleValues[i][0]-doubleValues[Integer.parseInt(clusters[j])-1][0]))+
						  ((doubleValues[i][1]-doubleValues[Integer.parseInt(clusters[j])-1][1])*
						  (doubleValues[i][1]-doubleValues[Integer.parseInt(clusters[j])-1][1]))+
						  ((doubleValues[i][2]-doubleValues[Integer.parseInt(clusters[j])-1][2])*
						  (doubleValues[i][2]-doubleValues[Integer.parseInt(clusters[j])-1][2]));
//				 }
				  distance=Math.sqrt(distance);
				  
//				  System.out.println((i)+"\t"+(Integer.parseInt(clusters[j]))+"\t"+distance);
				  
				  if (distance<min && distance!=0){
					 min=distance;
					 index=Integer.parseInt(clusters[j]);
					 minFromData=i;
				  }
			 }
			  
//			  System.out.println((i)+"\t"+index+"\t"+min);

			  if (clusterList.containsKey(index)){
				  
				  str1=clusterList.get(index);
				  
//				  System.out.println(str1);
				  
				  clusterList.remove(index);
				 
				  
				  str1.add(Integer.toString(minFromData));
//				  System.out.println(str1);
					  
				  clusterList.put(index, str1);
			  }
			  
			  else
			  {
				  str1.add(Integer.toString(minFromData));
				  clusterList.put(index, arrayList);
			  }
		   }
		 
			for (Entry<Integer, HashSet<String>> result1:clusterList.entrySet()){
//				System.out.println(result1.getKey()+"\t"+result1.getValue());
				System.out.println(result1.getKey()+"\t"+result1.getValue().size());
//				System.out.println(result1.getValue().size());
			}
			
//		 break;
			
			calculateNewCentroid(clusterList);
			clusterList=new HashMap<Integer,HashSet<String>>();
//			 break;
			
		}		
	}
	
	
	
	private static void calculateNewCentroid(HashMap<Integer, HashSet<String>> clusterList) {
		// TODO Auto-generated method stub
			double[][] centroidArray=new double[clusterList.size()][3];
			int rowCount=0;
			HashMap<Integer,HashSet<String>> newclusterList=new HashMap<Integer,HashSet<String>>();
			HashSet<String> str1=new HashSet<String>();
		for (Entry<Integer, HashSet<String>> result:clusterList.entrySet()){
			double x=0;
			double y=0;
			double z=0;
			

			HashSet<String> center=result.getValue();
			for (String i:center){
				x+=doubleValues[Integer.parseInt(i)][0];
				y+=doubleValues[Integer.parseInt(i)][1];
				z+=doubleValues[Integer.parseInt(i)][2];
			}
			
			x+=doubleValues[result.getKey()][0];
			y+=doubleValues[result.getKey()][1];
			z+=doubleValues[result.getKey()][2];
			
			x=x/(center.size()+1);
			y=y/(center.size()+1);
			z=z/(center.size()+1);
			
			centroidArray[rowCount][0]=x;
			centroidArray[rowCount][1]=y;
			centroidArray[rowCount][2]=z;
			
			rowCount++;
		}
		
		for (int i=0;i<centroidArray.length;i++){
			for (int j=0;j<centroidArray[0].length;j++)
			{
				System.out.print(centroidArray[i][j]+"\t");
			}
			System.out.println();
		}
		
//		System.out.println("************************************");
		
		for (int i=0;i<count;i++){
			double distance=0;
			double min=9999;
			int index=0; 
			int dataFromFile=0;
			HashSet<String> arrayList= new HashSet<String>();
			for (int j=0;j<centroidArray.length;j++){
				  distance=
						  (doubleValues[i][0]-centroidArray[j][0])*(doubleValues[i][0]-centroidArray[j][0])+
						  (doubleValues[i][1]-centroidArray[j][1])*(doubleValues[i][1]-centroidArray[j][1])+
						  (doubleValues[i][2]-centroidArray[j][2])*(doubleValues[i][2]-centroidArray[j][2]);

			
			distance=Math.sqrt(distance);
			
			if (distance<min ){
				 min=distance;
				 index=j;
				 dataFromFile=i;
				}
			
//			System.out.println(index+"\t"+dataFromFile+"\t"+min);
			
			}
			
//			---
			
//			System.out.println(index+"\t"+dataFromFile+"\t"+min);
			
			  if (newclusterList.containsKey(index)){
//				  str1=null;
				  str1=newclusterList.get(index);
				  newclusterList.remove(index);
//				  System.out.println(str1);
				  str1.add(Integer.toString(dataFromFile));
//			
//				  System.out.println(str1);
				  
				  newclusterList.put(index, str1);
			  }
//			  
			  else
			  {
//				  str1=null;
				  arrayList.add(Integer.toString(dataFromFile));
//				  System.out.println(str1);
				  newclusterList.put(index, arrayList);
			  }
		}
		
		
		double total=0;
		for (Entry<Integer, HashSet<String>> result1:newclusterList.entrySet()){
			int count=0;
//			total=0;
			double SSE=0;
			System.out.println(result1.getKey()+"\t"+result1.getValue());
			for (int j=0;j<centroidArray.length;j++)
			{
				for (String i:result1.getValue()){
				SSE+=
					((doubleValues[Integer.parseInt(i)][0]-centroidArray[j][0])*(doubleValues[Integer.parseInt(i)][0]-centroidArray[j][0]))+
					((doubleValues[Integer.parseInt(i)][1]-centroidArray[j][1])*(doubleValues[Integer.parseInt(i)][1]-centroidArray[j][1]))+
					((doubleValues[Integer.parseInt(i)][2]-centroidArray[j][2])*(doubleValues[Integer.parseInt(i)][2]-centroidArray[j][2]));
					}
			}
			total+=SSE;
		}

		SSEArray[centroidArray.length-1][0]=centroidArray.length;
		SSEArray[centroidArray.length-1][1]=total;
		System.out.println("************************************");
	}
	
	private static void plotLineChart(){		
		XYSeries lineSeries = new XYSeries("SSE Vs. Number of Clusters");
		for(int i=0;i<SSEArray.length;i++){
			lineSeries.add(SSEArray[i][0],SSEArray[i][1]);	
		}
		
		// To plot SSE vs. Number of clusters Line chart. 
		XYSeriesCollection lineData = new XYSeriesCollection();
		lineData.addSeries(lineSeries);
		
		JFreeChart lineChart=ChartFactory.createXYLineChart(
				"LineChart", 
				"Number of Clusters", 
				"SSE", 
				lineData, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
				
		 // Saving the JPEG file on local machine. 
		try {
			ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW07-K-Means/LineChart.jpg"), lineChart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart3.");
		}
		
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readFile();
		recursiveMean();
//		displayMatrix();
		plotLineChart();
	}
}
