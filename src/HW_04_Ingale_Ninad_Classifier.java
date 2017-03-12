import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HW_04_Ingale_Ninad_Classifier {
	static String[] stringInput;
	static String[] tree;
 
	
	public void trainer(Node node){
		try
		{
//		Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW04-Decision Tree/HW_04_DecTree_Testing__Data_TO_CLASSIFY__v101.csv")));

		Scanner inputScanner=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW04-Decision Tree/HW_04_DecTree_Testing__Data_TO_CLASSIFY__v101.csv")));		
		FileWriter writer=new FileWriter("E:/BDA Homework/HW04-Decision Tree/HW_04_DecTree_Testing__Data_TO_CLASSIFY__v101 - testing.txt",true);
		inputScanner.nextLine();
		while (inputScanner.hasNextLine()){
			Scanner s=new Scanner (new BufferedReader(new FileReader("E:/BDA Homework/HW04-Decision Tree/tree.csv")));
			String input=inputScanner.nextLine();				
			String[] stringInput = input.split(",");
			String treeinput=s.nextLine();				
			tree=treeinput.split(",");
			if (Double.parseDouble(stringInput[1])<=Double.parseDouble(tree[1])){
				treeinput=s.nextLine();				
				tree=treeinput.split(",");
				if (Double.parseDouble(stringInput[1])<=Double.parseDouble(tree[1])){
					treeinput=s.nextLine();				
					tree=treeinput.split(",");
					if (Double.parseDouble(stringInput[0])<=Double.parseDouble(tree[1])){
						writer.write(input+","+"0");
					}
					
					else{
						writer.write(input+","+"1");
					}
				} 
				
				else{
					writer.write(input+","+"1");
				}
				
			}
			else
			{
				writer.append(input+","+"1");
				
			}
			writer.append("\r\n");
			}
//		writer.flush();
		writer.close();
		
	}
	catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
