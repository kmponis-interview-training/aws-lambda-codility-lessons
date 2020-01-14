package com.amazonaws.lambda.lesson2;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OddOccurrencesInArray implements RequestHandler<Object, Integer> {

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
		JsonArray inputA = jsonObject.get("A").getAsJsonArray();
		int[] a = new Gson().fromJson(inputA, int[].class);

		int aLength = a.length;
		if (aLength % 2 == 0) {
			return -1;
		}

		Set<Integer> oddNumbers = new LinkedHashSet<>();
		for (int i = 0; i < a.length; i++) {
			if (oddNumbers.contains(a[i])) {
				oddNumbers.remove(a[i]);
			} else {
				oddNumbers.add(a[i]);
			}
		}

		if (oddNumbers.size() != 1) {
			return -1;
		} else {
			Iterator<Integer> iter = oddNumbers.iterator();
			if (iter.hasNext()) {
				return iter.next();
			}
		}

		return -1;
	}

}
