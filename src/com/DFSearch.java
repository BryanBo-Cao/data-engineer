package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class DFSearch {

	public static void main(String[] args) {
		
		String currentPackagePath = getCurrentPackagePath();
		// -define .csv file in app
        String fileNameDefined = "data.csv";
        // -File class needed to turn stringName to actual file
        File file = new File(currentPackagePath + fileNameDefined);

        int row = 0, col = 0;
        double[][] arr = new double[row][col];
        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
            	String data = inputStream.next();
            	if (col == 0) {
            		String[] dataArr = data.split(",");
            		col = dataArr.length;
            	}
            	row++;
            }
//            System.out.println(row);
//            System.out.println(col);
            
            arr = new double[row][col];
            
            inputStream = new Scanner(file);
            // hashNext() loops line-by-line
            int i = 0;
            while(inputStream.hasNext()){
                //read single line, put in string
            	String data = inputStream.next();
            	String[] dataArr = data.split(",");
            	for (int j = 0; j < dataArr.length; j++) {
            		if (dataArr[j].equals("NaN"))
            			arr[i][j] = 0;
            		else arr[i][j] = Double.parseDouble(dataArr[j]);
            	}
            	i++;
            }
            // after loop, close scanner
            inputStream.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        
//        for (int i = 0; i < arr.length; i++) {
//        	for (int j = 0; j < arr[0].length; j++) {
//        		System.out.print(arr[i][j] + " ");
//        	}
//        	System.out.println();
//        }
        
//		double[][] arr = new double[][]; // arr is the copy of the original array
		double maxSumS = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] > 0) {
					double sumS = dfSearch(arr, i, j, 1);
					if (sumS > maxSumS) maxSumS = sumS;
				}
			}
		}
		System.out.println(maxSumS);
        
	}
	
	private static String getCurrentPackagePath() {
		Path currentAbsolutePath = Paths.get(".").toAbsolutePath().normalize();
		File file = new File(DFSearch.class.getPackage().getName());
		StringBuilder packageSB = new StringBuilder();
		packageSB.append(currentAbsolutePath + "/src/");
		for (String dir : file.getName().split("\\.")) packageSB.append(dir + "/");
		return packageSB.toString();
	}
	
	private static double dfSearch(double[][] arr, int i, int j, int depth) {
		double sumS = 0;
		// search its neigbors
		if (depth <= 10) {
			sumS = arr[i][j] * arr[i][j];
			arr[i][j] = 0;
			if (i - 1 >= 0 && arr[i - 1][j] == 1) {
				arr[i - 1][j] = 0;
				sumS += dfSearch(arr, i - 1, j, depth + 1);
			}
			if (i + 1 < arr.length && arr[i + 1][j] == 1) {
				arr[i + 1][j] = 0;
				sumS += dfSearch(arr, i + 1, j, depth + 1);
			}
			if (j - 1 >= 0 && arr[i][j - 1] == 1) {
				arr[i][j - 1] = 0;
				sumS += dfSearch(arr, i, j - 1, depth + 1);
			}
			if (j + 1 < arr.length && arr[i][j + 1] == 1) {
				arr[i][j + 1] = 0;
				sumS += dfSearch(arr, i, j + 1, depth + 1);
			}
		}
		
		return sumS;
	}

}

//bo.cao-1@colorad.edu
