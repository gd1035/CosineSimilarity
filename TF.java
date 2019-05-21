import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TF {
	public static void main(String[] args) {
		BufferedReader reader;
		FileWriter writer;

		HashMap<String, Integer> tfMap = new HashMap<String, Integer>();
		
		String inputFile = args[0];
		String outputFile = args[1];
/*
** Initialize a HashMap with the unique words in the document set to 0
*/
		try
		{
			reader = new BufferedReader(new FileReader(inputFile));

			String line = "";
			String[] tokens;

			while((line = reader.readLine()) != null) {
				tokens = line.replaceAll("[^a-zA-Z ]","").toLowerCase().split(" ");
				for(int i = 0; i < tokens.length; i++)
					tfMap.put(tokens[i], 0);
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
/*
** Count the number of occurences for the words in the document
*/
		try
		{
			reader = new BufferedReader(new FileReader(inputFile));

			String line = "";
			String[] tokens;

			while((line = reader.readLine()) != null) {
				tokens = line.replaceAll("[^a-zA-Z ]","").toLowerCase().split(" ");
				
				for(int i = 0; i < tokens.length; i++) {
					tfMap.put(tokens[i], tfMap.get(tokens[i]) + 1);
				}
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
/*
** Write the results to a file
*/
		try
		{
			writer = new FileWriter(new File(outputFile));

			for(String key : tfMap.keySet()) {
				writer.write(key + " :: " + tfMap.get(key) + "\n");
			}

			writer.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
