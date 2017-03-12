import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HW02_Ingale_Ninad_Program {

	static double[] input;
	static String [] stringInput;
	static int[] bins;
	static double [][] frequency;
	static int count;
	static int min=38;
	static int max=80;
	static int interval=2;
	static int size;
	static int j; 

  /*
   * This method initializes the frequency matrix which holds 
   * all the values required for computing the minimum mixed variance at
   * each and every threshold (start of every bin).   
   */
	static void initialize(){
		input=new double[128];
		Scanner inputScanner = null;
		//Input Scan.
		try {
			// Reading a file from specified location. 
			inputScanner = new Scanner(new File ("E:/BDA Homework/HW2-Otsu's 1D Clustering/UNCLASSIFIED_Speed_Observations_for_128_vehicles.csv"));
			inputScanner.useDelimiter(",");
			while (inputScanner.hasNextLine()){
				stringInput=new String[128];
				for (int x=0;x<128;x++){
					stringInput[x]=inputScanner.nextLine();
				} 				
			}
		//Converting the input into double. 	
			for (int x=0;x<128;x++){
				input[x]=Double.parseDouble(stringInput[x]);
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 /*
  * This method is responsible for dividing the input into bins on 
  * the basis of min,max values and interval(bin size) provided. 
  * This method gets called from the main() method of the class. 
  */
	static void binningInput(){
		size= (max-min)/interval;
		bins=new int[size+1];
		for (int i=0;i<size+1;i++)
		{
			bins[i]=min;
			min=min+interval;
		}
		System.out.println("");
		
		//Initialize frequency matrix.
		frequency=new double[size+1][10];
		
		for (int i=0;i<size+1;i++)
		{
			frequency[i][0]=bins[i];
		}		
		
		//Set the frequency for the bins.
		for (int i=0;i<size;i++)
		{
			count=0;
			for (int j=0;j<input.length-1;j++){
				if (input[j]>=frequency[i][0]&&input[j]<frequency[i+1][0])
					count++;
			}
			frequency[i][1]=count; 
		}
		
		//Set the Weight under and weight over for the current bin in consideration. 
		//Weight under=weight_left , weight over=weight_right. 
		double weight_left=0; 
		double weight_right=0;
		//Weight under
		for (int i=0;i<=size;i++)
		{
			weight_left=weight_left+frequency[i][1];	
			frequency[i][2]=weight_left/128;	
			// mean left on right should be 1- mean weight on left 
			frequency[i][3]=1-frequency[i][2];
			j++;
			calculateMean(j);
		}
	}

	// Display contents of the entire 2-D Array.
	// Used only for testing purpose.
	static void displayFrequencyMatrix(){
		for (int i=0;i<size+1;i++)
		  {
			for (int j=0;j<10;j++){
				System.out.print(frequency[i][j]+"\t\t\t");
		  }
			System.out.println("");
		}
	}
	
	/*
	 * This function is used to calculate the average frequency of the 
	 * elements on both sides of the threshold currently under consideration.
	 * The function is called from method binningInput(). 
	 * Parameters : 
	 * 				j -> No. of bins on the left side of the current bin.  
	*/
	static void calculateMean(int j){
		//Calculating mean weight on the left side
		//of the bin we are currently in.
		double mean_left=0;
		for (int i=0;i<j;i++) 
			mean_left=(mean_left+frequency[i][1]);
		mean_left=mean_left/j;
		frequency[j-1][4]=mean_left;
		calculateLeftVarience(j,mean_left);
		
		//Calculating mean weight on the right side
		//of the bin we are currently in.
		double mean_right=0;
		for (int i=j;i<size;i++)
			mean_right=(mean_right+frequency[i][1]);
		mean_right=mean_right/(size-(j-1));
		frequency[j-1][5]=mean_right;
		calculateRightVarience(j,mean_right);		
	}
	
	
  /*
   * This method is responsible for calculating the variance on the left 
   * side of the current bin into consideration. 
   */
	private static void calculateRightVarience(int j2, double right_mean) {
		// TODO Auto-generated method stub
		double right_variance=0;
		
		for (int i=j2;i<size;i++)
		{	
			right_variance=(frequency[i][1]-right_mean)*(frequency[i][1]-right_mean);
			right_variance+=right_variance;
		}
		right_variance=right_variance/j2;
		frequency[j2-1][7]=right_variance;
	}

  /*
   * This method is responsible for calculating the variance on the right 
   * side of the current bin into consideration. 
   */	
	private static void calculateLeftVarience(int j2, double left_mean) {
		double left_varience=0;
		for (int i=0;i<j2;i++)
			{	
			left_varience=(frequency[i][1]-left_mean)*(frequency[i][1]-left_mean);
			left_varience+=left_varience;
			}
			left_varience=left_varience/(j2-1);
			frequency[j2-1][6]=left_varience;
	}
	
  /* 
   * This method is used to calculate the minimum mixed variance based on the 
   * variances on the left and right sides of the current bin in consideration. 
   */
	private static void calculateMixedVariance(){
		double mixed_variance=0;
		for (int i=0;i<size;i++){
			mixed_variance=(frequency[i][2]*frequency[i][4])+(frequency[i][3]*frequency[i][5]);
			frequency[i][8]=mixed_variance;
		}
		
		double mixedVarience=9999;
		double threshold=0;
		for (int i=1;i<size;i++)
		{
			if (frequency[i][8]<mixedVarience && frequency[i][8]!=0.0){
				mixedVarience=frequency[i][8];
				threshold=frequency[i][0];
			}
		}
		//System.out.println(mixedVarience +" "+ threshold);
		System.out.println("Minimum mixed variance :"+ mixedVarience);
		System.out.println("Corresponding Threshold :"+ threshold);
	}
	
  /*
   * This method plots the required Histogram and Line chart mentioned in the
   * question. 
   */
	private static void plotHistogram(){
		// To plot  Quantized speed Vs. Frequency.
		XYSeries histogramSeries = new XYSeries("Speed Vs. Frequency");
		for(int i=0;i<size;i++){
			histogramSeries.add(frequency[i][0],frequency[i][1]);	
		}
		XYSeriesCollection histogramData = new XYSeriesCollection();
		histogramData.addSeries(histogramSeries);
		JFreeChart histogram = ChartFactory.createXYBarChart(
				"Histogram", // Title
				"Quantized Speed", 
				false, "Frequency", 
				histogramData, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false 
				);
		
		XYSeries lineSeries = new XYSeries("Speed Vs. Mixed Variance");
		for(int i=0;i<size;i++){
			lineSeries.add(frequency[i][8],frequency[i][0]);	
		}
		
		// To plot minimum mixed variance vs. Quantized speed Line chart. 
		XYSeriesCollection lineData = new XYSeriesCollection();
		lineData.addSeries(lineSeries);

		JFreeChart lineChart=ChartFactory.createXYLineChart(
				"LineChart", 
				"Mixed Variance", 
				"Quantized Speed", 
				lineData, 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
				
		 // Saving the JPEG file on local machine. 
//		try {
//			ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW2-Otsu's 1D Clustering/Histogram.jpg"), histogram, 500, 300);
//			ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW2-Otsu's 1D Clustering/LineChart.jpg"), lineChart, 500, 300);
//		} catch (IOException e) {
//			System.err.println("Problem occurred creating chart3.");
//		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initialize();
		binningInput();
		calculateMixedVariance();
		displayFrequencyMatrix();
		plotHistogram();
	}
}
