package org.finra.datagenerator.tools;

import java.util.ArrayList;
import java.util.List;

public class RunnerPairAnalysis {

	public static void main(String[] args) {
		int runNumber = 100;
		
		List<Integer> allPairsFoundAt = new ArrayList<Integer>();
		int allPairsFoundAtAverage = 0;
		
		for (int i = 0; i < runNumber; i++) {
			CreateDataFile.createDataFileDo();
			int currentAllPairsLine = PairsAnalysis.PairsAnalysisDo();
			allPairsFoundAt.add(currentAllPairsLine);
			allPairsFoundAtAverage += currentAllPairsLine;
		}
		
		System.out.println("All pairs found line is :" + allPairsFoundAt );
		System.out.println("Average all pairs found line is :" + allPairsFoundAtAverage * 1.0 / runNumber );
	}

}
