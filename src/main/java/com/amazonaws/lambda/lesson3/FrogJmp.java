package com.amazonaws.lambda.lesson3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FrogJmp implements RequestHandler<Object, Integer> {

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
		int inputX = jsonObject.get("X").getAsInt();
		int inputY = jsonObject.get("Y").getAsInt();
		int inputD = jsonObject.get("D").getAsInt();

		if (inputX < 1 || inputY < 1 || inputD < 1 || inputY < inputX) {
			return -1;
		}

		int count = 0;
		while (inputX < inputY) {
			inputX = inputX + inputD;
			count++;
		}
		return count;
	}

}
