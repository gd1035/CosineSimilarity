import java.util.*;
import java.io.*;

public class Prediction {
	public static void main(String[] args) {
		/* Were the correct arguments given? */
		String initialArgs = "\n********************\n********************\n\nThis program requires you to input 8 command line arguments\nExpected were 7 files containing tf-idf scores for 7 different categories and a file containing test data.\n\nPlease try again.\n\n********************\n********************\n";
		if(args.length != 8){
			System.out.println(initialArgs);
			return;
		}

		Prediction pred = new Prediction();
		
		/* The vectors for each category */
	  Map<String, Double> vector1 = new HashMap<String, Double>();
	  Map<String, Double> vector2 = new HashMap<String, Double>();
	  Map<String, Double> vector3 = new HashMap<String, Double>();
	  Map<String, Double> vector4 = new HashMap<String, Double>();
		Map<String, Double> vector5 = new HashMap<String, Double>();
		Map<String, Double> vector6 = new HashMap<String, Double>();
		Map<String, Double> vector7 = new HashMap<String, Double>();

		vector1 = pred.getVector(args[0]);
		vector2 = pred.getVector(args[1]);
		vector3 = pred.getVector(args[2]);
		vector4 = pred.getVector(args[3]);
		vector5 = pred.getVector(args[4]);
		vector6 = pred.getVector(args[5]);
		vector7 = pred.getVector(args[6]);

		/* The norms of the above vectors */
		double norm1 = pred.norm(vector1);
		double norm2 = pred.norm(vector2);
		double norm3 = pred.norm(vector3);
		double norm4 = pred.norm(vector4);
		double norm5 = pred.norm(vector5);
		double norm6 = pred.norm(vector6);
		double norm7 = pred.norm(vector7);

		/* The vector of test data */
		Map<String, Map<String, Integer>> testVector = new HashMap<String, Map<String, Integer>>();
		
		testVector = pred.getTestVect(args[7]);
		
		/* To store the results */
		Map<String, Double[]> results = new HashMap<String, Double[]>();

		/* Calculating the cosine similarities between each category vector and the test vector */
		for(String key : testVector.keySet()) {
			Double[] simil = new Double[7];
			
			int keySize = testVector.get(key).size();
			double normKey = Math.sqrt(keySize);
			
			simil[0] = pred.innerProduct(vector1, testVector.get(key)) / (norm1 * normKey);
			simil[1] = pred.innerProduct(vector2, testVector.get(key)) / (norm2 * normKey);
			simil[2] = pred.innerProduct(vector3, testVector.get(key)) / (norm3 * normKey);
			simil[3] = pred.innerProduct(vector4, testVector.get(key)) / (norm4 * normKey);
			simil[4] = pred.innerProduct(vector5, testVector.get(key)) / (norm5 * normKey);
			simil[5] = pred.innerProduct(vector6, testVector.get(key)) / (norm6 * normKey);
			simil[6] = pred.innerProduct(vector7, testVector.get(key)) / (norm7 * normKey);
			
			results.put(key, simil);
		}

		/* To store the maximum category from each inner product */
		Map<String, Integer> finalResult = new HashMap<String, Integer>();
		
		Integer max = 0;

		for(String key : results.keySet()) {
			max = pred.whichIsMax(results.get(key));
			finalResult.put(key, max);
		}

		/* To have a sorted form of the final results */
		Map<String, Integer> finalBoss = new TreeMap<String, Integer>(finalResult);
			
		/* To write the final results to file */
		try
		{
			FileWriter writer = new FileWriter(new File("./submission.csv"));

			for(String key : finalBoss.keySet()) {
				writer.write(key + "," + finalBoss.get(key) + "\n");
			}

			writer.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}

	}
/*
** This method is used to create the vectors for each category.
** These are already stored in an external file, this just reads
** them in and builds a HashMap object to store the data
**
** Each vector is of the form [{word1 = tfidf1, ... , wordN = tfidfN}]
*/
	private Map<String, Double> getVector(String file) {
		BufferedReader reader;
		
		Map<String, Double> result = new HashMap<String, Double>();

		try
		{
			String line = "";
			String[] tokens;

			reader = new BufferedReader(new FileReader(file));

			while((line = reader.readLine()) != null) {
				tokens = line.split("  :: ");
				result.put(tokens[0], Double.parseDouble(tokens[1]));
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
/*
** This method reads in the test data set and builds a HashMap
** containing the product id and a HashMap of the words contained
** in the product description
**
** It has the form <N1={word1 = 1, ... , wordN1 = 1}, ... , Nn={word1 = 1, ... , wordNn = 1}>
*/
	private Map<String, Map<String, Integer>> getTestVect(String file) {
		BufferedReader reader;

		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();

		try
		{
			String line = "";
			String[] tokens;
			String[] innerTokens;

			reader = new BufferedReader(new FileReader(file));

			while((line = reader.readLine()) != null) {
				Map<String, Integer> innerMap = new HashMap<String, Integer>();
				tokens = line.split(",\"");
			  /* tokens[1] has the data */
				tokens[1] = tokens[1].replaceAll("[^a-zA-Z ]","").toLowerCase();
				innerTokens = tokens[1].split(" ");

				for(int i = 0; i < innerTokens.length; i++) {
					if(!innerMap.containsKey(innerTokens[i]))
						innerMap.put(innerTokens[i], 1);
				}

				result.put(tokens[0], innerMap);
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
/*
** This method calculates the norm of a vector
** this is returned as a double
*/
	private double norm(Map<String, Double> map) {
		double result = 0;
		double intermint = 0;

		for(String key : map.keySet()) {
			intermint = map.get(key).doubleValue();
			result += intermint * intermint;
		}

		result = Math.sqrt(result);
		return result;
	}
/*
** This method calculates the inner product between two vectors
** using the dot product
**
** map1 is the vector from the categories
** it is of the form [{word1 = tfidf1, ... , wordn = tfidfn}]
**
** map2 is the test vector
** it is of the form [{word1 = 1, ... , wordk = 1}]
*/
	private double innerProduct(Map<String, Double> map1, Map<String, Integer> map2) {
		double result = 0;
		
		for(String key : map2.keySet()) {
			if(map1.containsKey(key)) {
				result += map1.get(key);
			}
		}
		return result;
	}
	
	private Integer whichIsMax(Double[] array) {
		Integer result = new Integer(-1);
		Double tester = new Double(-1);

		for(int i = 0; i < array.length; i++) {
			if(array[i] > tester) {
				tester = array[i];
				result = i+1;
			}
		}

		return result;
	}
}
