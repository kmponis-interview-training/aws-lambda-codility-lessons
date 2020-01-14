package com.amazonaws.lambda.lesson3;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TapeEquilibrium implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
		context.getLogger().log("------------------------------------");
		context.getLogger().log("Input: " + input);

		JsonObject jsonObject;
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonTree = parser.parse(input.toString());
			jsonObject = jsonTree.getAsJsonObject();
			context.getLogger().log("Use JsonParser to parse a JSON text\n");
		} catch (Exception ex) {
			Gson gson = new GsonBuilder().create();
			JsonElement jsonElement = gson.toJsonTree(input);
			jsonObject = jsonElement.getAsJsonObject();
			context.getLogger().log("Use GsonBuilder to parse a non generic type object\n");
		}
		JsonArray jsonA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(jsonA, int[].class);

		long start7 = System.nanoTime();
		java7Solution(A);
		long end7 = System.nanoTime();

		long start8 = System.nanoTime();
		java8Solution(A);
		long end8 = System.nanoTime();

		long start8Parallel = System.nanoTime();
		int sol = java8ParallelSolution(A);
		long end8Parallel = System.nanoTime();

		System.out.println("Java7: " + (end7 - start7) + " - Java8: " + (end8 - start8) + " - Java8Parallel: "
				+ (end8Parallel - start8Parallel));
		return sol;
	}

	private int java7Solution(int[] A) {
		int minimalDifference = 0;
		int n = A.length;
		for (int i = 1; i < n; i++) {
			int sum1 = 0;
			for (int j = 0; j < i; j++) {
				sum1 = sum1 + A[j];
			}

			int sum2 = 0;
			for (int k = i; k < n; k++) {
				sum2 = sum2 + A[k];
			}

			int difference = Math.abs(sum1 - sum2);

			if (i == 1 || minimalDifference > difference) {
				minimalDifference = difference;
			}
		}

		return minimalDifference;
	}

	private int java8Solution(int[] A) {
		int n = A.length;
		int[] minimalDifference = new int[1];
		IntStream.range(1, n).forEach(i -> {
			int sum1 = Arrays.stream(A, 0, i).sum();
			int sum2 = Arrays.stream(A, i, n).sum();
			int difference = Math.abs(sum1 - sum2);

			if (i == 1 || minimalDifference[0] > difference) {
				minimalDifference[0] = difference;
			}
		});

		return minimalDifference[0];
	}

	private int java8ParallelSolution(int[] A) {
		int n = A.length;
		LinkedList<Integer> minimalDifference = new LinkedList<>();
		IntStream.range(1, n).parallel().forEach(i -> {
			int sum1 = Arrays.stream(A, 0, i).sum();
			int sum2 = Arrays.stream(A, i, n).sum();
			int difference = Math.abs(sum1 - sum2);
			System.out.println("PARALLEL: sum(0," + (i - 1) + ")=" + sum1 + " - sum(" + i + "," + (n - 1) + ")=" + sum2
					+ " - difference: " + difference);
			minimalDifference.add(difference);
		});

		return minimalDifference.stream().min(Integer::compare).orElse(0);
	}

}
