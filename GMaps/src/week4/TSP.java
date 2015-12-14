package week4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class TSP {
	
	
	
	public static void findPathBruteForceClassExample(String filename)
	{
		BufferedReader reader = null;
		String[] cities = null;
		int[][] distances = null;
		try {
            String nextLine;
            reader = new BufferedReader(new FileReader(filename));
            nextLine = reader.readLine();
            if (nextLine == null) {
            	reader.close();
            	throw new IOException("File is empty!");
            }
            int numCities = Integer.parseInt(nextLine);
            System.out.println("There are " + numCities + " cities");
            cities = new String[numCities];
            for (int i = 0; i < numCities; i++) {
            	nextLine = reader.readLine();
            	if (nextLine == null) {
                	reader.close();
                	throw new IOException("Not enough lines in the file!");
                }
            	cities[i] = nextLine;
            }
            
            distances = new int[numCities][numCities];
            for (int i = 0; i < numCities; i++) {
            	nextLine = reader.readLine();
            	if (nextLine == null) {
                	reader.close();
                	throw new IOException("Not enough lines in the file!");
                }
            	String[] distsFrom = nextLine.split(" ");
            	for (int j = 0; j < numCities; j++) {
            		distances[i][j] = Integer.parseInt(distsFrom[j]);
            	}
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Problem loading dictionary file: " + filename);
            e.printStackTrace();
        }
		
		int[] testPermutation = {0, 1, 3, 4, 5, 6, 2};
		System.out.println(calculateDistance(testPermutation, distances));
		
		String[] testPath = new String[cities.length];
		testPath[0] = cities[0];
		for (int i = 1; i < cities.length; i++) {
			testPath[i] = cities[testPermutation[i-1]+1];
		}
		
		for (String city : testPath) {
			System.out.print(city + "->");
		}
		System.out.println(cities[0]);


		String[] bestPath = runBruteForceTSP(cities, distances);
		
		
		
		for (String city : bestPath) {
			System.out.print(city + "->");
		}
		System.out.println(cities[0]);
	}

	private static String[] runBruteForceTSP(String[] cities, int[][] distances) {
		int bestDist = Integer.MAX_VALUE;
		int[] bestPermutation = null;
		int[] permutation = new int[cities.length-1]; 
		int[] directions = new int[cities.length-1];
		for (int i = 0; i < cities.length-1; i++) {
			permutation[i] = i;
			directions[i] = -1;
		}
		directions[0] = 0;
		
		boolean hasNextPermutation = true;
		while (hasNextPermutation) {
			int currDist = calculateDistance(permutation, distances);
			if (currDist < bestDist) {
				bestPermutation = Arrays.copyOf(permutation, permutation.length);
				bestDist = currDist;
			}
			hasNextPermutation = getNextPermutation(permutation, directions);
		}
		String[] toReturn = new String[cities.length];
		toReturn[0] = cities[0];
		for (int i = 1; i < cities.length; i++) {
			toReturn[i] = cities[bestPermutation[i-1]+1];
		}
		System.out.println("The best distance is " + bestDist);
		return toReturn;
	}

	// Implementation of the Steinhaus–Johnson–Trotter algorithm with Even's speed up
	// Based on https://en.wikipedia.org/wiki/Steinhaus%E2%80%93Johnson%E2%80%93Trotter_algorithm
	private static boolean getNextPermutation(int[] permutation, int[] directions) {
		// TODO Auto-generated method stub
		// find the largest element with non-zero direction
		int moveIndex = -1;
		int max = -1;
		for (int i = 0; i < permutation.length; i++) {
			if (directions[i] != 0 && permutation[i] > max) {
				max = permutation[i];
				moveIndex = i;
			}
		}
		if (moveIndex == -1) return false;
		
		// Swap in the appropriate direction
		int swapIndex = moveIndex + directions[moveIndex];
		swap(permutation, swapIndex, moveIndex);
		swap(directions, swapIndex, moveIndex);
		
		if (swapIndex == 0 || swapIndex == permutation.length-1 ||
				permutation[swapIndex+directions[swapIndex]] > permutation[swapIndex]) {
			directions[swapIndex] = 0;
		}
		
		for (int i = 0; i < permutation.length; i++) {
			if (permutation[i] > permutation[swapIndex]) {
				if (i < swapIndex) directions[i] = 1;
				else if (i > swapIndex) directions[i] = -1;
			}
		}
		
		return true;
	}
	
	private static void swap(int[] arr, int a, int b)
	{
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	private static int calculateDistance(int[] permutation, int[][] distances) {
		// TODO Auto-generated method stub
		// Permutation does not include the start city so it's off by one on the 
		// distances
		int dist = 0;
		int currentCity = 0;
		for (int i = 0; i < permutation.length; i++) {
			int nextDist = distances[currentCity][permutation[i]+1];
			dist += nextDist;
			currentCity = permutation[i]+1;
		}
		dist += distances[currentCity][0];
		return dist;
	}
	
	public static void main(String[] args) 
	{
		String filename = "data/tsp.cites";
		findPathBruteForceClassExample(filename);
	}
	
}
