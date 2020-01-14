package com.amazonaws.lambda.lesson5;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MinAvgTwoSlice implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
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
		int sol = java8Solution(A);
		long end8 = System.nanoTime();

		System.out.println("Java7: " + (end7 - start7) + " - Java8: " + (end8 - start8));
		return sol;
	}

	private Integer java7Solution(int[] A) {
		int n = A.length;
		double avg = 0.0;
		double minAvg = 0.0;
		int startingPosition = 0;

		for (int i = 0; i < n - 1; i++) {
			int P = i;
			for (int j = i + 1; j < n; j++) {
				int Q = j;
				// A[P] + A[P + 1] + ... + A[Q]) / (Q − P + 1)
				double sum = Double.valueOf(Arrays.stream(A, P, Q + 1).sum());
				avg = sum / (Q - P + 1);
				if (i == 0 && j == 1) {
					minAvg = avg;
				} else {
					if (avg < minAvg) {
						minAvg = avg;
						startingPosition = i;
					}
				}
				System.out.println(
						A[P] + ", " + A[Q] + " - (" + P + ", " + Q + ") - sum: " + sum + " - avg: " + avg + " - minAvg: " + minAvg);
			}
		}

		return startingPosition;
	}

	private Integer java8Solution(int[] A) {
		int n = A.length;
		AtomicReference<Double> minAvg = new AtomicReference<>(0.0);
		AtomicInteger startingPosition = new AtomicInteger();
		IntStream.range(0, n - 1).forEach(i -> {
			IntStream.range(i + 1, n).forEach(j -> {
				double sum = Double.valueOf(Arrays.stream(A, i, j + 1).sum());
				double avg = sum / (j - i + 1);

				if (i == 0 && j == 1) {
					minAvg.set(avg);
				} else {
					if (avg < minAvg.get()) {
						minAvg.set(avg);
						startingPosition.set(i);
					}
				}
				System.out.println(
						A[i] + ", " + A[j] + " - (" + i + ", " + j + ") - sum: " + sum + " - avg: " + avg + " - minAvg: " + minAvg);
			});
		});

		return startingPosition.get();
	}

}
