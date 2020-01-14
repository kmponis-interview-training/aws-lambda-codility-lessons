package com.amazonaws.lambda.lesson4;

import java.util.TreeSet;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MissingInteger implements RequestHandler<Object, Integer> {

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
		Integer[] A = new Gson().fromJson(inputA, Integer[].class);

		TreeSet<Integer> positiveA = new TreeSet<>();
		for (int i = 0; i < A.length; i++) {
			if (A[i] > 0) {
				positiveA.add(A[i]);
			}
		}

		Object[] ray = positiveA.toArray();
		if (ray.length == 0 || (Integer) ray[0] != 1) {
			return 1;
		}

		if (ray.length == 1) {
			return 2;
		}

		int current = 0;
		int previous = 0;
		for (int i = 1; i < ray.length; i++) {
			current = (Integer) ray[i];
			previous = (Integer) ray[i - 1];

			if (current != previous + 1) {
				return previous + 1;
			}
		}

		return current != 0 ? current + 1 : 1;
	}

}
