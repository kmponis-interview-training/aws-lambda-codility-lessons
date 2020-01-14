package com.amazonaws.lambda.lesson7;

import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Fish implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
		context.getLogger().log("--------------------------");
		context.getLogger().log("Input: " + input);
		JsonObject jsonObject;
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonTree = parser.parse(input.toString());
			jsonObject = jsonTree.getAsJsonObject();
			context.getLogger().log("Use JsonParser to parse a JSON text");
		} catch (Exception ex) {
			Gson gson = new GsonBuilder().create();
			JsonElement jsonElement = gson.toJsonTree(input);
			jsonObject = jsonElement.getAsJsonObject();
			context.getLogger().log("Use GsonBuilder to parse a non generic type object");
		}
		JsonArray jsonA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(jsonA, int[].class);
		JsonArray jsonB = jsonObject.get("B").getAsJsonArray();
		int[] B = new Gson().fromJson(jsonB, int[].class);

		long start1 = System.nanoTime();
		Integer sol1 = java8Solution1(A, B);
		long end1 = System.nanoTime();

		context.getLogger().log("Java8 Solution 1: " + (end1 - start1));
		context.getLogger().log("--------------------------");
		return sol1;
	}

	private Integer java8Solution1(int[] A, int[] B) {
		int n = A.length;
		int[] alive = new int[n];
		IntStream.range(0, n).filter(i -> B[i] == 1).forEach(i -> {
			IntStream.range(i + 1, n).filter(j -> B[j] == 0).forEach(j -> {
				boolean isAnyAlive = IntStream.range(i + 1, j).filter(k -> alive[k] == 0).count() > 0 ? true : false;
				if (B[j] == 0 && !isAnyAlive) {
					if (A[i] > A[j]) {
						alive[j] = 1;
					} else {
						alive[i] = 1;
					}
				}
			});
		});

		return (int) IntStream.of(alive).filter(k -> k == 0).count();
	}
}
