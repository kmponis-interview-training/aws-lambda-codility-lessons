package com.amazonaws.lambda.lesson5;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GenomicRangeQuery implements RequestHandler<Object, Object[]> {

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
		String S = jsonObject.get("S").getAsString();
		JsonArray jsonP = jsonObject.get("P").getAsJsonArray();
		Integer[] P = new Gson().fromJson(jsonP, Integer[].class);
		JsonArray jsonQ = jsonObject.get("Q").getAsJsonArray();
		Integer[] Q = new Gson().fromJson(jsonQ, Integer[].class);

		Integer n = P.length;
		Integer[] output = new Integer[n];
		for (int i = 0; i < n; i++) {
			String subString = S.substring(P[i], Q[i] + 1);
			int res = 5;
			for (int j = 0; j < subString.length(); j++) {
				char a = subString.charAt(j);
				if (a == 'A') {
					res = 1;
					break;
				} else if (a == 'C' && res > 2) {
					res = 2;
				} else if (a == 'G' && res > 3) {
					res = 3;
				} else if (a == 'T' && res > 4) {
					res = 4;
				}
			}

			output[i] = res;
		}

		return output;
	}

}
