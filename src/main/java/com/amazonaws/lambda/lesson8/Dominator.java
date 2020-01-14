package com.amazonaws.lambda.lesson8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Dominator implements RequestHandler<Object, Object[]> {

	@Override
	public Object[] handleRequest(Object input, Context context) {
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

		long start1 = System.nanoTime();
		Integer[] sol1 = java8Solution1(A);
		long end1 = System.nanoTime();

		long start2 = System.nanoTime();
		Integer[] sol2 = java8Solution2(A);
		long end2 = System.nanoTime();

		context.getLogger().log("Java8 Solution 1: " + (end1 - start1) + " nano" + " - Solution 2: " + (end2 - start2)
				+ " nano - sol1.equals(sol2): " + Arrays.equals(sol1, sol2));
		context.getLogger().log("--------------------------");
		return sol1;
	}

	private Integer[] java8Solution1(int[] A) {
		int n = A.length;
		if (n == 1) {
			return new Integer[] { 0 };
		}

		AtomicInteger index = new AtomicInteger(0);
		Integer[] integerA = Arrays.stream(A).boxed().toArray(Integer[]::new);
		Map.Entry<Integer, Long> numberAndMaxCountMap = Stream.of(integerA).map(i -> index.getAndIncrement())
				.collect(Collectors.groupingBy(i -> A[i], Collectors.counting())).entrySet().stream()
				.max(Comparator.comparing(Entry::getValue)).get();

		Integer[] output = IntStream.range(0, n).filter(i -> numberAndMaxCountMap.getValue() > (float) n / 2)
				.filter(i -> A[i] == numberAndMaxCountMap.getKey()).boxed().toArray(Integer[]::new);

		return output.length == 0 ? new Integer[] { -1 } : output;
	}

	private Integer[] java8Solution2(int[] A) {
		int n = A.length;
		if (n == 1) {
			return new Integer[] { 0 };
		}

		Map<String, Integer> numberAndMaxCountMap = getNumberAndMaxCount(Arrays.copyOf(A, n));
		Integer[] output = IntStream.range(0, n).filter(i -> numberAndMaxCountMap.get("maxCount") > (float) n / 2)
				.filter(i -> A[i] == numberAndMaxCountMap.get("number")).boxed().toArray(Integer[]::new);

		return output.length == 0 ? new Integer[] { -1 } : output;
	}

	private Map<String, Integer> getNumberAndMaxCount(int[] A) {
		Map<String, Integer> numberAndMaxCountMap = new HashMap<>();
		numberAndMaxCountMap.put("number", 0);
		numberAndMaxCountMap.put("maxCount", 0);

		int[] count = new int[1];
		Arrays.sort(A);
		IntStream.range(0, A.length - 1).forEach(i -> {
			if (A[i] == A[i + 1]) {
				count[0] = (i == 0 ? count[0] + 2 : count[0] + 1);
			} else {
				count[0] = 1;
			}

			if (count[0] > numberAndMaxCountMap.get("maxCount")) {
				numberAndMaxCountMap.put("number", A[i]);
				numberAndMaxCountMap.put("maxCount", count[0]);
			}
		});

		return numberAndMaxCountMap;
	}

}
