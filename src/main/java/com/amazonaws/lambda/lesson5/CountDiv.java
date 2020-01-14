package com.amazonaws.lambda.lesson5;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CountDiv implements RequestHandler<Object, Integer> {

	@Override
	public Integer handleRequest(Object input, Context context) {
		context.getLogger().log("----------------------------");
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
		int A = jsonObject.get("A").getAsInt();
		int B = jsonObject.get("B").getAsInt();
		int K = jsonObject.get("K").getAsInt();

		long start7 = System.nanoTime();
		java7Solution(A, B, K);
		long end7 = System.nanoTime();

		long start8 = System.nanoTime();
		int sol = java8Solution(A, B, K);
		long end8 = System.nanoTime();

		System.out.println("Java7: " + (end7 - start7) + " - Java8: " + (end8 - start8));
		return sol;
	}

	private int java7Solution(int A, int B, int K) {
		int count = 0;
		for (int i = A; i < B + 1; i++) {
			if (i % K == 0) {
				count++;
			}
		}
		return count;
	}

	private int java8Solution(int A, int B, int K) {
		AtomicInteger count = new AtomicInteger();
		IntStream.range(A, B).parallel().forEach(i -> {
			if (i % K == 0) {
				count.getAndIncrement();
			}
		});
		return count.get();
	}

}
