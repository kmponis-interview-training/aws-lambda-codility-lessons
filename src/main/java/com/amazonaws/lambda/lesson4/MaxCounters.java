package com.amazonaws.lambda.lesson4;

import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MaxCounters implements RequestHandler<Object, Object[]> {

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
		Integer N = jsonObject.get("N").getAsInt();
		JsonArray inputA = jsonObject.get("A").getAsJsonArray();
		Integer[] A = new Gson().fromJson(inputA, Integer[].class);

		int[] output = new int[N];
		for (int i = 0; i < A.length; i++) {
			int arrayValue = A[i];

			if (arrayValue <= N) {
				int getI = output[arrayValue - 1];
				output[arrayValue - 1] = getI + 1;
			} else {
				int max = Arrays.stream(output).max().getAsInt();
				Arrays.fill(output, max);
			}
		}

		return Arrays.stream(output).boxed().toArray(Integer[]::new);
	}

}
