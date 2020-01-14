package com.amazonaws.lambda.lesson1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BinaryGap implements RequestHandler<Object, Integer> {

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
		Integer N = jsonObject.get("N").getAsInt();

		long start7 = System.nanoTime();
		java7Solution(N);
		long end7 = System.nanoTime();

		long start8 = System.nanoTime();
		int sol = java8Solution(N);
		long end8 = System.nanoTime();

		System.out.println("Java7: " + (end7 - start7) + " - Java8: " + (end8 - start8));
		return sol;
	}

	private Integer java7Solution(Integer N) {
		String binaryN = Integer.toBinaryString(N);
		int binaryGap = 0;
		int maxGap = 0;
		boolean count = false;
		for (int i = 0; i < binaryN.length(); i++) {
			char a = binaryN.charAt(i);
			if (a == '1') {
				maxGap = binaryGap > maxGap ? binaryGap : maxGap;
				binaryGap = 0;
				count = true;
			} else if (a == '0' && count) {
				binaryGap++;
			}
		}
		return maxGap;
	}

	private Integer java8Solution(Integer N) {
		String binaryN = Integer.toBinaryString(N);

		AtomicInteger binaryGap = new AtomicInteger(0);
		AtomicInteger maxGap = new AtomicInteger(0);
		AtomicBoolean count = new AtomicBoolean();
		binaryN.chars().parallel().mapToObj(x -> (char) x).forEach(a -> {
			if (a == '1') {
				maxGap.getAndSet(binaryGap.get() > maxGap.get() ? binaryGap.get() : maxGap.get());
				binaryGap.getAndSet(0);
				count.set(true);
			} else if (a == '0' && count.get()) {
				binaryGap.incrementAndGet();
			}
		});

		return maxGap.get();
	}

}
