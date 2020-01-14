package com.amazonaws.lambda.lesson6;

import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Distinct implements RequestHandler<Object, Integer> {

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
		int[] NA = new Gson().fromJson(jsonObject.get("A").getAsJsonArray(), int[].class);

		long start1 = System.nanoTime();
		Integer sol1 = getSolution(NA);
		long end1 = System.nanoTime();
		Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

		context.getLogger().log("Solution 1: " + sol1 + " time " + time1 + " sec");
		context.getLogger().log("--------------------------");
		return sol1;
	}

	private Integer getSolution(int[] A) {
		long n = Arrays.stream(A).distinct().count();

		return (int) n;
	}

}
