package com.amazonaws.lambda.lesson7;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Nesting implements RequestHandler<Object, Integer> {

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
		int sol1 = getSolution.apply(S);
		long end1 = System.nanoTime();

		context.getLogger().log("Solution 1: " + (end1 - start1) + " nano");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	Function<String, Integer> getSolution = (S) -> {
		String[] arrayS = S.chars().mapToObj(s -> ((char) s) + "").toArray(String[]::new);
		List<String> openBracket = new ArrayList<>();
		int index = 0;
		for (int i = 0; i < arrayS.length; i++) {
			if (arrayS[i].equals("(")) {
				openBracket.add(arrayS[i]);
				index++;
			}

			if (arrayS[i].equals(")")) {
				if (openBracket.isEmpty()) {
					return 0;
				} else {
					index--;
					openBracket.remove(index);
				}
			}
		}

		return openBracket.isEmpty() ? 1 : 0;
	};

}
