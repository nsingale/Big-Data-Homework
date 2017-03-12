import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This class is used to classify drivers between categories reckless and safe on 
 * the basis of speed at which they are driving.  
 */

/**
 * @author Ninad Ingale
 * Date : 02/11/2016
 */
public class HW03_Ingale_Ninad_Program {

	static double[] speed;
	static double[] reckless;
	static String [] stringInput;
	static String []stringValue;
	static double[] bins;
	static double [][] frequency;
	static double count;
	static double min=40;
	static double max=75;
	static double interval=0.5;
	static int size;
	static int j; 
	double miscalculationRate=9999;
	double threshold=0;

  /*
   * This method reads the data from the file provided and initializes
   * arrays for speed and reckless behaviour.      
   */
	static void initialize(){
		speed=new double[129];
		reckless=new double[129];
		Scanner inputScanner = null;
		//Input Scan.
		try {
			// Reading a file from specified location. 
			inputScanner = new Scanner(new File ("E:/BDA Homework/HW3-Classification by Thresholding/CLASSIFIED_TRAINING_SET_FOR_RECKLESS_DRIVERS_2016.csv"));
			inputScanner.useDelimiter("\n");
			inputScanner.nextLine();
			int lineNumber=0;
			stringInput=new String[129];
			while (inputScanner.hasNextLine()){
					stringInput[lineNumber]=inputScanner.nextLine();
					stringValue=stringInput[lineNumber].split(",");
					speed[lineNumber]=Double.parseDouble(stringValue[0]);
					//System.out.println(speed[lineNumber]);
					reckless[lineNumber]=Double.parseDouble(stringValue[1]);
					lineNumber++;
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
		static void binningInput() throws IOException{
			size= (int)((max-min)/interval);
			frequency=new double[size+1][10];
			System.out.println("Bins");
			//bins=new double[size+1];
			for (int i=0;i<size+1;i++)
			{
				frequency[i][0]=min;
				min=min+interval;
			}
			
//			Frequency of recklessness in each bin. 
			
			for (int i=0;i<size;i++)
			{
				count=0;
				for (int j=0;j<speed.length;j++){					
					if (speed[j]>=frequency[i][0]&&speed[j]<frequency[i+1][0])
						count++;
					
				}
				frequency[i][1]=count; 
			}
			
			//Frequency of vehicles which are non reckless within a particular bin.  
			
			for (int i=0;i<size;i++)
			{
				int truePositive=0;
				for (int j=0;j<speed.length-1;j++){					
					if (speed[j]>=frequency[i][0]&&speed[j]<frequency[i+1][0]&&reckless[j]==0)
						truePositive++;
				}
				frequency[i][1]=truePositive;				
			}
			
			//Frequency of vehicles which are reckless within a particular bin. 
			// False Positive value. 
			
			for (int i=0;i<size;i++)
			{
				int falsePositive=0;
				for (int j=0;j<speed.length-1;j++){					
					if (speed[j]>=frequency[i][0]&&speed[j]<frequency[i+1][0]&&reckless[j]==1)
						falsePositive++;
				}
//				System.out.println(" ");
				frequency[i][2]=falsePositive;
			}
			
			//Setting the frequency count for the non reckless drivers which are 
			//driving at a speed less than the threshold under consideration. 
			// True Positive value.
			
			frequency[0][3]=0;
		
			double truePositive=frequency[0][1];
			for (int i=1;i<size;i++)
			{
				truePositive+=frequency[i][1];
				frequency[i][3]=truePositive;
			}

			//Setting the frequency count for the "reckless" drivers which are 
			//driving at a speed less than the threshold under consideration. 
			// False alarms. 
			
			double falsePositive=frequency[0][2];
			for (int i=1;i<size;i++)
			{
				falsePositive+=frequency[i][2];
				frequency[i][4]=falsePositive;
			}
			
			//Setting the frequency count for the non reckless drivers which are 
			//driving at a speed more than the threshold under consideration. 
			// False Negatives. 			
			
			double falseNegative=frequency[0][1];
			for (int i=0;i<size;i++)
			{
				falseNegative=0;
				for (j=i+1;j<size;j++)
				{
					falseNegative+=frequency[j][1];
				}
				frequency[i][5]=falseNegative;
			}
			
			//Setting the frequency count for the reckless drivers which are 
			//driving at a speed more than the threshold under consideration. 
			//True negative.Correct Rejections.
			
			double correctRejections=frequency[0][2];
			for (int i=0;i<size;i++)
			{
				correctRejections=0;
				for (j=i+1;j<size-1;j++)
				{
					correctRejections+=frequency[j][2];
				}
				frequency[i][6]=correctRejections;
			}
			
			// Calculating misclassification rate and finding the threshold
			// where the misclassification rate is minimum.
			// misclassification rate = (FP+FN)/(TP+FP+FN+TN)
			
			double misclassificationRate=9999;
			double threshold=0;
			for (int i=1;i<size;i++)
			{
				double a =(frequency[i][4]+frequency[i][5])/(frequency[i][3]+frequency[i][4]+frequency[i][5]+frequency[i][6]);
				if (a<misclassificationRate)
				{
					misclassificationRate=a;
					threshold=frequency[i][0];
				}
			}
			System.out.println("Misclassification Rate :"+ misclassificationRate);
			System.out.println("Corresponding Threshold :"+ threshold);
			
			//Calculate True Positive Rate (TPR) and False Positive Rate (FPR)
			//This is for plotting the ROC curve. 
			//TPR= TP/(TP+FN)
			//FPR=FP/(FP+TN)
			for (int i=0;i<size;i++){
				double TPR=0;
				double FPR=0;
				//TP/(TP+FN)
				TPR=frequency[i][3]/(frequency[i][3]+frequency[i][5]);
				//FP/(FP+TN)
				FPR=frequency[i][4]/(frequency[i][4]+frequency[i][6]);
				frequency[i][7]=TPR;
				frequency[i][8]=FPR;
			}
			
			//For running out algorithm on test data. 
			runTestData(threshold);
//			displayFrequencyMatrix();
//		}
//		
		}
		
		
// Display contents of the entire 2-D Array.
// Used only for testing purpose.
//		static void displayFrequencyMatrix(){
//			for (int i=0;i<size;i++)
//			  {
//				for (int j=0;j<9;j++){
//					System.out.print(frequency[i][j]+" ");
//			  }
//				System.out.println("");
//			}
//		}
		
	   /*
		* This method reads the data from the test file and calculates 
		* whether the driver at the corresponding speed is reckless or 
		* not. If the driver is driving at a speed>threshold , he will 
		* be classified as reckless otherwise classified as safe. 
		*/
		static void runTestData(double threshold) throws IOException {
			Scanner inputScanner = null;
			FileWriter writer=new FileWriter("E:/BDA Homework/HW3-Classification by Thresholding/HW_03_Ingale_Ninad_CLASSIFICATINOS.csv");
			try {
				inputScanner = new Scanner(new File ("E:/BDA Homework/HW3-Classification by Thresholding/SPEEDS_TO_CLASSIFY_2016.csv"));
				while (inputScanner.hasNextLine()){
					String line=inputScanner.nextLine();
					String testInput[]=line.split(",");
					int reckless=0;
////					System.out.println(testInput[0]);
					if (Double.parseDouble(testInput[0])>threshold)
					{
//						reckless=1;
						writer.append(testInput[0]+","+"1");
					}
					else
					{
//						reckless=0;
						writer.append(testInput[0]+","+"0");
					}
					writer.append("\n");
//				System.out.println(testInput[0]+" "+reckless);
				}
				writer.flush();
				writer.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

	   /*
		* This method plots the required ROC curve and Line chart mentioned in the
		* question. 
		*/
		static void drawGraph(){
			XYSeries lineSeries = new XYSeries("FPR Vs. TPR");
			for(int i=0;i<size;i++){
				lineSeries.add(frequency[i][8],frequency[i][7]);	
			}
			
			// To plot minimum mixed variance vs. Quantized speed Line chart. 
			XYSeriesCollection lineData = new XYSeriesCollection();
			lineData.addSeries(lineSeries);

			JFreeChart lineChart=ChartFactory.createXYLineChart(
					"FPR Vs. TPR", 
					"False Positive Rate", 
					"True Positive Rate", 
					lineData, 
					PlotOrientation.VERTICAL, 
					true, 
					true, 
					false);

			XYSeries misclassificationSeries = new XYSeries("Misclassification Vs. Threshold");
			for(int i=0;i<size;i++){
				double misclassification=0;
				misclassification=(frequency[i][4]+frequency[i][5])/(frequency[i][3]+frequency[i][4]+frequency[i][5]+frequency[i][6]);
				misclassificationSeries.add(frequency[i][0],misclassification);	
			}
			
			// To plot minimum mixed variance vs. Quantized speed Line chart. 
			
			XYSeriesCollection misclassifyData = new XYSeriesCollection();
			misclassifyData.addSeries(misclassificationSeries);

			JFreeChart missclassifyChart=ChartFactory.createXYLineChart(
					"Misclassification Vs. Threshold", 
					"Speed in mph", 
					"Misclassification Rate", 
					misclassifyData, 
					PlotOrientation.VERTICAL, 
					true, 
					true, 
					false);

			 // Saving the JPEG file on local machine. 
//			try {
//				ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW3-Classification by Thresholding/missclassify.jpg"), missclassifyChart, 500, 300);
//				ChartUtilities.saveChartAsJPEG(new File("E:/BDA Homework/HW3-Classification by Thresholding/LineChart.jpg"), lineChart, 500, 300);
//			} catch (IOException e) {
//				System.err.println("Problem occurred creating chart3.");
//			}
		}			

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		initialize();
		binningInput();
		drawGraph();
	}
}
