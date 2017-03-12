import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

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
public class HW_08_Ingale_Ninad_DBScan {


	static double [][] inputData;
	static double[][] distanceMatrix;
	static double[][] forth_point;
	static HashSet<Integer> visitedPoints;
	static HashMap<Integer,HashSet<Integer>> clusterMap=new HashMap<Integer,HashSet<Integer>>(); 
	static HashMap<Integer,HashSet<Integer>> mergedClusterMap=new HashMap<Integer,HashSet<Integer>>();
	static ArrayList<Integer> visitedelement=new ArrayList<Integer>();  

	private static void recursiveDBScan() {
		// TODO Auto-generated method stub
		calculateEPS(inputData);
		buildNewMatrix();
		//		buildCluster();
		System.out.println("Elements visited "+visitedPoints.size());
		for (Entry<Integer,HashSet<Integer>> result1:clusterMap.entrySet()){

			//			String[] stringValues;
//			System.out.println(result1.getKey()+"\t"+result1.getValue().size());
		}


		for (Entry<Integer,HashSet<Integer>> result2:mergedClusterMap.entrySet()){
			if(result2.getValue().size()>75)
			System.out.println(result2.getKey()+"\t"+result2.getValue().size());
		}

	}


	private static void buildNewMatrix() {
		// TODO Auto-generated method stub
		visitedPoints=new HashSet<Integer>();
		for (int i=0;i<distanceMatrix.length;i++){
			int count=0;
			for (int j=0;j<distanceMatrix[0].length;j++){				
				if (distanceMatrix[i][j]<=0.8)
				{
					count++;
				}
			}
			//			System.out.println(i+"\t"+count);

			// Build a separate cluster only if no. of elements>5
			if (count>5){
				buildCluster(i);
			}
		}
		mergeCluster(clusterMap);
	}


	private static void buildCluster(int index) {
		// TODO Auto-generated method stub
		HashSet<Integer> nearByPoints=new HashSet<Integer>();
		for (int j=0;j<distanceMatrix[0].length;j++){
			if (distanceMatrix[index][j]<=0.75){
				nearByPoints.add(j);
			}
		}
		//			System.out.println(index+"\t"+nearByPoints.size());
		//			System.out.println();
		//Add to hashmap
		clusterMap.put(index, nearByPoints);

	}


	private static void mergeCluster(HashMap<Integer, HashSet<Integer>> clusters) {
		// TODO Auto-generated method stub
		try
		{
			for (Entry<Integer,HashSet<Integer>> result1:clusters.entrySet()){
				if (mergedClusterMap.size()==0){
					mergedClusterMap.put(result1.getKey(), new HashSet<Integer>(result1.getKey()));
					visitedelement.add(result1.getKey());
				}
				if(visitedelement.contains(result1.getKey())==false){
				for (int i:result1.getValue())
				{
					if(i!=result1.getKey())
					{
						HashSet<Integer> current=new HashSet<Integer>();
						if (visitedelement.contains(i)==false)
						{
							visitedelement.add(i);
							current.add(i);
//							System.out.println(i);
							if (clusterMap.get(i)!=null)
							{
								for(int j:clusterMap.get(i))
								{
									visitedelement.add(j);
									current.add(j);
								}
							}
									
						}
						
						if(mergedClusterMap.containsKey(result1.getKey()))
						{
							HashSet<Integer> element=mergedClusterMap.get(result1.getKey());
							element.addAll(current);
							mergedClusterMap.put(result1.getKey(), element);
						}
						else
						{
							mergedClusterMap.put(result1.getKey(), current);
						}
						
					}
				}
			}
		  }
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}			


	private static void readData() {
		// TODO Auto-generated method stub
		try 
		{
			Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW08-DBScan_NOISY/HW_08_DBScan_Data_NOISY_v300.csv")));
			//		  inputScanner.nextLine();
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

			System.out.println(inputData.length+"\t"+inputData[0].length);

			//		  for (int i=0;i<inputData.length;i++){
			//			  for (int j=0;j<inputData[0].length;j++){
			//				  System.out.print(inputData[i][j]+"\t");
			//			  }
			//			  System.out.println();
			//		  }

			//		  calculateEPS(inputData);

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void calculateEPS(double[][] inputData2) {
		// TODO Auto-generated method stub
		distanceMatrix=new double[inputData.length][inputData.length];
		XYSeries lineSeries = new XYSeries("Fourth Distance Vs. Points");

		for (int i=0;i<inputData.length;i++)
		{
			for (int j=0;j<inputData.length;j++)
			{
				double distance=0;

				for (int k=0;k<inputData[0].length;k++)
				{
					distance+=(inputData[i][k]-inputData[j][k])*(inputData[i][k]-inputData[j][k]);
				}
				distance=Math.sqrt(distance);
				distanceMatrix[i][j]=distance;
				//				System.out.println(i+"\t"+distanceList.get(3));
			}
			//			System.out.println();
		}

		ArrayList<Double> chartList=new ArrayList<Double>();
		forth_point=new double[inputData.length][2];
		for (int i=0;i<distanceMatrix.length;i++){
			ArrayList<Double> distanceList=new ArrayList<Double>();
			for (int j=0;j<distanceMatrix[0].length;j++){
				distanceList.add(distanceMatrix[i][j]);	
			}
			Collections.sort(distanceList);
			double forth_distance=distanceList.get(3);
			chartList.add(forth_distance);			
			//			System.out.print(i+"\t"+forth_distance);
			forth_point[i][0]=i;
			forth_point[i][1]=forth_distance;
			//			System.out.println();
		}

		Collections.sort(chartList);


		for (int i=0;i<chartList.size();i++){
			lineSeries.add(i,chartList.get(i));
		}
		XYSeriesCollection lineData = new XYSeriesCollection();
		lineData.addSeries(lineSeries);

		JFreeChart lineChart=ChartFactory.createXYLineChart(
				"LineChart", 
				"Sorted Distance Instance", 
				"Distance to the 4th closese point", 
				lineData, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);

		try {
			ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW08-DBScan_NOISY/LineChart.jpg"), lineChart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart3.");
		}		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readData();
		recursiveDBScan();
	}
}
