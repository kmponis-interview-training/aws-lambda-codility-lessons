package com.amazonaws.lambda.lesson6;

import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Triangle implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
		context.getLogger().log("--------------------------");
		context.getLogger().log("Input: " + input);
		JsonObject jsonObject;
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(input.toString());
			jsonObject = jsonElement.getAsJsonObject();
		} catch (Exception ex) {
			context.getLogger().log("Use GsonBuilder to parse a JSON object");
			Gson gson = new GsonBuilder().create();
			JsonElement jsonElement = gson.toJsonTree(input);
			jsonObject = jsonElement.getAsJsonObject();
		}
		int[] NA = new Gson().fromJson(jsonObject.get("NA").getAsJsonArray(), int[].class);

		long start1 = System.nanoTime();
		Integer sol1 = getSolution(NA);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		long start2 = System.nanoTime();
		Integer sol2 = getSolution2(NA);
		long end2 = System.nanoTime();
		Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;

		context.getLogger()
				.log("Solution 1: " + sol1 + " time " + time1 + " - Solution 2: " + sol2 + " time " + time2 + " sec");
		context.getLogger().log("--------------------------");
		return sol2;
	}

	private Integer getSolution(int[] NA) {
		int n = NA.length;
		for (int p = 0; p < n - 2; p++) {
			for (int q = p + 1; q < n - 1; q++) {
				for (int r = q + 1; r < n; r++) {
					if ((NA[p] + NA[q] > NA[r]) && (NA[q] + NA[r] > NA[p]) && (NA[r] + NA[p] > NA[q])) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private Integer getSolution2(int[] NA) {
		// Java program to count number of triangles that can be
		// formed from given array
		// Function to count all possible triangles with arr[]
		// elements
		int n = NA.length;
		// Sort the array elements in non-decreasing order
		Arrays.sort(NA);

		// Initialize count of triangles
		int count = 0;

		// Fix the first element. We need to run till n-3 as
		// the other two elements are selected from arr[i+1...n-1]
		for (int i = 0; i < n - 2; ++i) {
			// Initialize index of the rightmost third element
			int k = i + 2;

			// Fix the second element
			for (int j = i + 1; j < n; ++j) {
				/*
				 * Find the rightmost element which is smaller than the sum of two fixed
				 * elements The important thing to note here is, we use the previous value of k.
				 * If value of arr[i] + arr[j-1] was greater than arr[k], then arr[i] + arr[j]
				 * must be greater than k, because the array is sorted.
				 */
				while (k < n && NA[i] + NA[j] > NA[k]) {
					++k;
				}

				/*
				 * Total number of possible triangles that can be formed with the two fixed
				 * elements is k - j - 1. The two fixed elements are arr[i] and arr[j]. All
				 * elements between arr[j+1] to arr[k-1] can form a triangle with arr[i] and
				 * arr[j]. One is subtracted from k because k is incremented one extra in above
				 * while loop. k will always be greater than j. If j becomes equal to k, then
				 * above loop will increment k, because arr[k] + arr[i] is always/ greater than
				 * arr[k]
				 */
				if (k > j) {
					count += k - j - 1;
				}
			}
		}
		return count >= 1 ? 1 : 0;
	}

}
