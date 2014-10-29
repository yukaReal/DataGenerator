package org.finra.datagenerator.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class PairsAnalysis {
	static public int valuesLength = CreateDataFile.Values.values().length;
	static private List<Map<Integer, String>> allPairs = new ArrayList<Map<Integer, String>>();
	static public Map<Integer, Integer> allPairsFoundAtProcent = new HashMap<Integer, Integer>(100);
	static int lineNumber = 0;
	static int allPairsCount = 0;
	
	public static void main(String[] args) {
		PairsAnalysisDo();
	}

	public static int PairsAnalysisDo() {
		lineNumber = 0;
		createAllPair();
		// printAllPairs();
		
		int appPairsFoundAtLine = processDataFile(); 
		if (appPairsFoundAtLine > 0) {
			showAllPairsFoundProcents();
			return appPairsFoundAtLine;
		} else {
			System.err.println("Oops! Not found all pair, update data file generator - add more lines!...");
		}
		
		showResults();
		
		// printAllPairs();
		System.exit(1);
		return -1;
	}

	private static void createAllPair() {
		for (int i1 = 0; i1 < CreateDataFile.numberOfProperties; i1++) {
			for (int i2 = i1 + 1; i2 < CreateDataFile.numberOfProperties; i2++) {
				// System.out.println("Let's create pairs for this properties pair: i = '" + i + "'; i1 = '" + i1+ "';");
				for (int y1 = 0; y1 < valuesLength; y1++) {
					for (int y2 = 0; y2 < valuesLength; y2++) {
						Map<Integer, String> pairCombination = new HashMap<Integer, String>();
						pairCombination.put(i1, CreateDataFile.Values.values()[y1].toString());
						pairCombination.put(i2, CreateDataFile.Values.values()[y2].toString());
						allPairs.add(pairCombination);
						//System.out.println("* " + pairCombination.toString());
					}
				}
			}
		}
		allPairsCount = allPairs.size();
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
		System.out.println("Processed " + lineNumber + " lines from data file ('" + CreateDataFile.fileName + "')...");
	}

	private static int processDataFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(CreateDataFile.fileName))) {
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
		// every line from data file can contain more than one pairs, so, let's save all found pairs and remove it from all pairs list 
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
			saveAllPairsUsage();
			if (allPairs.isEmpty()) {
				System.out.println("Cool! We find all necessary pairs combinations! It takes '" + lineNumber + "' data lines...");
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private static void saveAllPairsUsage() {
		if (allPairs.size() == 0) {
			allPairsFoundAtProcent.put(100, lineNumber);
		} else {
			allPairsFoundAtProcent.put((allPairsCount - allPairs.size()) * 100 / allPairsCount, lineNumber);
		}
	}
	
	private static void showAllPairsFoundProcents() {
		System.out.println("All Pairs founding (<% of all pairs found at>,<line of data file>):");
		SortedSet<Integer> sortedAllPairsUsageProcents = new TreeSet<Integer>();
		sortedAllPairsUsageProcents.addAll(allPairsFoundAtProcent.keySet());
		for (int procent : sortedAllPairsUsageProcents) {
			System.out.println(procent + "," + allPairsFoundAtProcent.get(procent));
		}
	}
}
