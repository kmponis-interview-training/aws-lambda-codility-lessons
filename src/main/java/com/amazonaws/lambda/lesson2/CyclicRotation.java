package com.amazonaws.lambda.lesson2;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CyclicRotation implements RequestHandler<Object, Object[]> {

	@Override
	public Object[] handleRequest(Object input, Context context) {
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
		Integer[] inputA = new Gson().fromJson(jsonA, Integer[].class);
		int inputK = jsonObject.get("K").getAsInt();

		int n = inputA.length;
		if (n == 0) {
			return inputA;
		}

		Integer[] output = new Integer[n];
		for (int i = 0; i < n; i++) {
			output[(i + inputK) % n] = inputA[i];
		}
		return output;
	}

}
