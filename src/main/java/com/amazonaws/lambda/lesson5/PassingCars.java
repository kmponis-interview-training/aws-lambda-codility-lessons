package com.amazonaws.lambda.lesson5;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PassingCars implements RequestHandler<Object, Integer> {

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
		JsonArray jsonA = jsonObject.get("A").getAsJsonArray();
		int[] A = new Gson().fromJson(jsonA, int[].class);

		int n = A.length;
		Integer count = 0;

		for (int i = 0; i < n; i++) {
			if (A[i] == 0) {
				for (int j = i + 1; j < n; j++) {
					if (A[j] == 1) {
						count++;
					}
				}
			}
		}

		return count > 1000000000 ? -1 : count;
	}

}
