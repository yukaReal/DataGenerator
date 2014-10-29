package org.finra.datagenerator.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class RunnerPairAnalysis {
	static private Map<Integer, List<Integer>> allPairsFoundAtProcentAll = new HashMap<Integer, List<Integer>>();

	public static void main(String[] args) {
		int runNumber = 3;

		List<Integer> allPairsFoundAt = new ArrayList<Integer>();

		int allPairsFoundAtAverage = 0;

		for (int i = 0; i < runNumber; i++) {
			CreateDataFile.createDataFileDo();
			int currentAllPairsLine = PairsAnalysis.PairsAnalysisDo();

			saveAllPairFoundAtProcent();

			allPairsFoundAt.add(currentAllPairsLine);
			allPairsFoundAtAverage += currentAllPairsLine;
		}

		System.out.println("All pairs found line is :" + allPairsFoundAt);
		System.out.println("Average all pairs found line is :" + allPairsFoundAtAverage * 1.0 / runNumber);
		
		showAllPairsFoundProcents();
	}

	private static void saveAllPairFoundAtProcent() {
		Map<Integer, Integer> currentFoundAtProcent = PairsAnalysis.allPairsFoundAtProcent;
		for (int proc : currentFoundAtProcent.keySet()) {
			if (allPairsFoundAtProcentAll.containsKey(proc)) {
				allPairsFoundAtProcentAll.get(proc).add(currentFoundAtProcent.get(proc));
			} else {
				List<Integer> currentProcValues = new ArrayList<>();
				currentProcValues.add(currentFoundAtProcent.get(proc));
				allPairsFoundAtProcentAll.put(proc, currentProcValues);
			}
		}
	}
	
	private static void showAllPairsFoundProcents() {
		System.out.println("All Pairs founding (<% of all pairs found at>,<line of data file>):");
		SortedSet<Integer> sortedAllPairsUsageProcents = new TreeSet<Integer>();
		sortedAllPairsUsageProcents.addAll(allPairsFoundAtProcentAll.keySet());
		for (int procent : sortedAllPairsUsageProcents) {
			System.out.println(procent + "	" + getAverage(allPairsFoundAtProcentAll.get(procent)));
		}
	}

	private static int getAverage(List<Integer> data) {
		if (data.size() == 0) {
			return 0;
		}
		
		int summary = 0;
		for (int currentValue : data) {
			summary += currentValue;
		}
		return summary / data.size();
	}
}
