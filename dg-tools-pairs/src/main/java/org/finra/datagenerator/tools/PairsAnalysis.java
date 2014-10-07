package org.finra.datagenerator.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PairsAnalysis {
	static public int valuesLength = CreateDataFile.Values.values().length;

	static private List<Map<Integer, String>> allPairs = new ArrayList<Map<Integer, String>>();

	static int lineNumber = 0;

	public static void main(String[] args) {
		PairsAnalysisDo();
	}

	public static int PairsAnalysisDo() {
		lineNumber = 0;
		createAllPair();
		// printAllPairs();
		
		int appPairsFoundAtLine = processDataFile(); 
		if (appPairsFoundAtLine > 0) {
			return appPairsFoundAtLine;
		} else {
			
		}
		
		showResults();
		// printAllPairs();
		System.exit(1);
		return -1;
	}

	private static void createAllPair() {
		for (int i = 0; i < CreateDataFile.numberOfProperties; i++) {
			for (int i1 = i + 1; i1 < CreateDataFile.numberOfProperties; i1++) {
				// System.out.println("Let's create pairs for this properties pair: i = '" + i + "'; i1 = '" + i1+ "';");
				for (int y1 = 0; y1 < valuesLength; y1++) {
					for (int y2 = 0; y2 < valuesLength; y2++) {
						Map<Integer, String> pairCombination = new HashMap<Integer, String>();
						pairCombination.put(i, CreateDataFile.Values.values()[y1].toString());
						pairCombination.put(i1, CreateDataFile.Values.values()[y2].toString());
						allPairs.add(pairCombination);
						// System.out.println("* " + pairCombination.toString());
					}
				}
			}
		}
		System.out.println("Created: all pairs ( " + allPairs.size() + " items):");
	}

	private static void printAllPairs() {
		System.out.println("");
		System.out.println(" All pairs ( " + allPairs.size() + " items):");
		for (Map<Integer, String> combination : allPairs) {
			System.out.println("* " + combination.toString());
		}
	}

	private static void showResults() {
		System.out.println("Number of all remain pairs = " + allPairs.size());
		System.out.println("Remain pairs: " + allPairs);
		System.out.println("Processed " + lineNumber + " lines from data file (data.txt)...");
	}

	private static int processDataFile() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/data.txt"))) {
			String line = br.readLine();
			lineNumber++;
			while (line != null) {
				Map<Integer, String> currentLineData = parseLine(line, "\\" + CreateDataFile.separator);
				boolean isAllPairsFound = checkPair(currentLineData);
				if (isAllPairsFound) {
					return lineNumber;
				};
				// System.out.println("all pairs = " + line);
				line = br.readLine();
				lineNumber++;
			}
		} catch (FileNotFoundException e) {
			System.err.println("Opps! " + e);
		} catch (IOException e1) {
			System.err.println("Opps! " + e1);
		}
		System.out.println("Oops! Data.txt file is processed, but we haven't found all necessary pairs!");
		return -1;
	}

	private static Map<Integer, String> parseLine(String line, String separatorValue) {
		Map<Integer, String> result = new HashMap<Integer, String>();

		String[] lineParsed = line.split(separatorValue);
		if (null == lineParsed) {
			System.err.println("Opps! No data in this line of data file...");
			return result;
		}

		if (lineParsed.length != CreateDataFile.numberOfProperties) {
			System.err.println("Opps! Line of data has wrong number of elements!");
			return result;
		}

		for (int i = 0; i < CreateDataFile.numberOfProperties; i++) {
			result.put(i, lineParsed[i]);
		}

		return result;
	}

	private static boolean checkPair(Map<Integer, String> currentLineData) {
//		Map<Integer, String> allPairsToRemove = new HashMap<Integer, String>();
		List<Map<Integer, String>> allPairsToRemove = new ArrayList<Map<Integer, String>>();
		for (Map<Integer, String> allPairsCurrent : allPairs) {
			// System.out.println("allPairsCurrent" + allPairsCurrent + "; currentLineData = " + currentLineData);
			boolean isAllMatch = true;
			for (int position : allPairsCurrent.keySet()) {
				if (!allPairsCurrent.get(position).equals(currentLineData.get(position))) {
					isAllMatch = false;
				}
			}
			if (isAllMatch) {
				// System.out.println("Combination '" + allPairsCurrent + "' is found! Let's remove it from list... ");
				allPairsToRemove.add(allPairsCurrent);
			}
		}
		
		if (!allPairsToRemove.isEmpty()) {
			allPairs.removeAll(allPairsToRemove);
			if (allPairs.isEmpty()) {
				System.out.println("Cool! We find all necessary pairs combinations! It takes '" + lineNumber + "' data lines...");
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
