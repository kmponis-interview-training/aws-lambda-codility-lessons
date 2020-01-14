package com.amazonaws.lambda.lesson7;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("unused")
public class Brackets implements RequestHandler<Object, Integer> {

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
		String S = jsonObject.get("S").getAsString();

		long start1 = System.nanoTime();
		int sol1 = functionGetSolution1.apply(S);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		long start2 = System.nanoTime();
		int sol2 = functionGetSolution2.apply(S);
		long end2 = System.nanoTime();
		Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;

		context.getLogger().log("Solution 1: " + time1 + " - Solution 2: " + time2 + " sec");
		context.getLogger().log("--------------------------");
		return sol2;
	}

	Function<String, Integer> functionGetSolution1 = s -> {
		int n = s.length();
		if (n == 0) {
			return 1;
		} else if (n % 2 != 0) {
			return 0;
		}

		String[] arrayS = s.chars().mapToObj(c -> ((char) c) + "").toArray(String[]::new);

		Boolean areAllBrackets = functionAreAllBrackets.apply(arrayS);
		if (!areAllBrackets) {
			return 0;
		}

		for (int i = 0; i < n; i++) {
			if (i == 0 && (arrayS[i].equals(")") || arrayS[i].equals("}") || arrayS[i].equals("]"))) {
				return 0;
			}

			if (arrayS[i].equals(")") && (arrayS[i - 1].equals("{") || arrayS[i - 1].equals("["))) {
				return 0;
			}
			if (arrayS[i].equals("}") && (arrayS[i - 1].equals("(") || arrayS[i - 1].equals("["))) {
				return 0;
			}
			if (arrayS[i].equals("]") && (arrayS[i - 1].equals("(") || arrayS[i - 1].equals("{"))) {
				return 0;
			}
		}

		return 1;
	};

	Function<String, Integer> functionGetSolution2 = s -> {
		int n = s.length();
		if (n == 0) {
			return 1;
		} else if (n % 2 != 0) {
			return 0;
		}

		List<String> arrayS = s.chars().mapToObj(ch -> ((char) ch) + "").collect(Collectors.toList());

		Deque<String> brackets = new LinkedList<>();
		Deque<String> square = new LinkedList<>();
		Deque<String> curly = new LinkedList<>();
		for (int i = 0; i < arrayS.size(); i++) {
			String ch = arrayS.get(i);

			if (ch.contains("{")) {
				curly.add("{");
			} else if (ch.contains("}")) {
				if (curly.isEmpty()) {
					return 0;
				} else {
					curly.pollLast();
				}
			}

			if (ch.contains("[")) {
				square.add("[");
			} else if (ch.contains("]")) {
				if (square.isEmpty()) {
					return 0;
				} else {
					square.pollLast();
				}
			}

			if (ch.contains("(")) {
				brackets.add("(");
			} else if (ch.contains(")")) {
				if (brackets.isEmpty()) {
					return 0;
				} else {
					brackets.pollLast();
				}
			}

			if (i + 1 != arrayS.size()) {
				if (ch.contains("(") && Arrays.asList("]", "}").contains(arrayS.get(i + 1))) {
					return 0;
				}
				if (ch.contains("[") && Arrays.asList(")", "}").contains(arrayS.get(i + 1))) {
					return 0;
				}
				if (ch.contains("{") && Arrays.asList(")", "]").contains(arrayS.get(i + 1))) {
					return 0;
				}
			}

		}
		return !brackets.isEmpty() || !square.isEmpty() || !curly.isEmpty() ? 0 : 1;
	};

	static final Function<String[], Boolean> functionAreAllBrackets = arrayS -> {
		Map<String, Long> groupCharacters = Stream.of(arrayS).collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		Boolean areBrackets = (groupCharacters.containsKey("(") && !groupCharacters.containsKey(")"))
				|| (groupCharacters.containsKey(")") && !groupCharacters.containsKey("(")) || (groupCharacters.containsKey("(")
						&& groupCharacters.containsKey(")") && !groupCharacters.get("(").equals(groupCharacters.get(")")));

		Boolean areCurlyBrackets = (groupCharacters.containsKey("{") && !groupCharacters.containsKey("}"))
				|| (groupCharacters.containsKey("}") && !groupCharacters.containsKey("{")) || (groupCharacters.containsKey("{")
						&& groupCharacters.containsKey("}") && !groupCharacters.get("{").equals(groupCharacters.get("}")));

		Boolean areSquareBrackets = (groupCharacters.containsKey("[") && !groupCharacters.containsKey("]"))
				|| (groupCharacters.containsKey("]") && !groupCharacters.containsKey("[")) || (groupCharacters.containsKey("[")
						&& groupCharacters.containsKey("]") && !groupCharacters.get("[").equals(groupCharacters.get("]")));

		return !areBrackets && !areCurlyBrackets && !areSquareBrackets;
	};

}
