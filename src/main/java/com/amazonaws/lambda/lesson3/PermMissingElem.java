package com.amazonaws.lambda.lesson3;

import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PermMissingElem implements RequestHandler<Object, Integer> {

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
		Arrays.sort(a);

		for (int i = 0; i < a.length; i++) {
			if (a[0] != 1) {
				return 1;
			}
			if (i == a.length - 1) {
				return a[i] + 1;
			}
			if ((a[i] + 1) != a[i + 1]) {
				return a[i] + 1;
			}
		}

		return 1;
	}

}
