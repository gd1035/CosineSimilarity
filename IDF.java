import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.lang.Math;

/*
** This program will calculate the inverse document frequency (IDF)
** of a collection of documents. All documents for which the IDF
** is to be calculated must be passed to the command line
** and the last argument to be passed to the program is the output
** file where the IDF values are to be written.
*/

public class IDF{
	public static void main(String[] args) {
		Map<String, Integer> idfMap = new HashMap<String, Integer>();
		BufferedReader reader;
		FileWriter writer;

		int numberOfDocuments = args.length - 1;
//need to refactor the below try block, this initialization should be done during the reading of the individual documents!!!!!
		try
		{
			reader = new BufferedReader(new FileReader("./full.txt"));

			String line = "";
			String[] tokens;

			while((line = reader.readLine()) != null) {
				tokens = line.replaceAll("[^a-zA-Z ]","").toLowerCase().split(" ");
				for(int i = 0; i < tokens.length; i++) {
					if(!idfMap.containsKey(tokens[i]))
						idfMap.put(tokens[i], 0);
				}
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
/*
** Here is where we determine the term frequency of the terms throughout
** the corpus of documents that were passed to the program.
*/
		for(int i = 0; i < numberOfDocuments; i++) {
			ArrayList<String> contains = new ArrayList<String>();
			try
			{
				reader = new BufferedReader(new FileReader(args[i]));

				String line = "";
				String[] tokens;

				while((line = reader.readLine()) != null) {
					tokens = line.replaceAll("[^a-zA-Z ]","").toLowerCase().split(" ");
					for(int j = 0; j < tokens.length; j++) {
						if(!contains.contains(tokens[j])) {
							contains.add(tokens[j]);
							idfMap.put(tokens[j], idfMap.get(tokens[j]) + 1);
						}
					}	
				}

			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
/*
** Here is where we calculate the IDF for the corpus.
*/
		try
		{
			writer = new FileWriter(new File(args[args.length-1]));

			for(String key : idfMap.keySet()) {
				double d = idfMap.get(key).doubleValue();
				double idf = Math.log(numberOfDocuments / d);

				writer.write(key + " :: " + idf + "\n");
			}
			writer.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
