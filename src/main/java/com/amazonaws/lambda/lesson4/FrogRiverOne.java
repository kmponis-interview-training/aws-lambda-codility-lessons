package com.amazonaws.lambda.lesson4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FrogRiverOne implements RequestHandler<Object, Integer> {

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
		Integer X = jsonObject.get("X").getAsInt();
		JsonArray jsonA = jsonObject.get("A").getAsJsonArray();
		Integer[] A = new Gson().fromJson(jsonA, Integer[].class);

		List<Integer> positionsX = new ArrayList<>();
		for (int i = 0; i < A.length; i++) {
			if (A[i] == X) {
				positionsX.add(i);
			}
		}

		int earliest = -1;
		for (int j = 0; j < positionsX.size(); j++) {
			Integer[] copy = Arrays.copyOfRange(A, 0, positionsX.get(j) + 1);
			Arrays.parallelSort(copy);
			boolean completed = true;
			for (int k = 0; k < copy.length - 1; k++) {
				if (!(copy[k] == copy[k + 1] || copy[k] + 1 == copy[k + 1])) {
					completed = false;
					break;
				}
			}

			if (completed) {
				earliest = positionsX.get(j);
				break;
			}
		}

		return earliest;
	}

}
