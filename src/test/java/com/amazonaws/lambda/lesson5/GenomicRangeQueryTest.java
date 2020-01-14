package com.amazonaws.lambda.lesson5;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GenomicRangeQueryTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"S\":\"CAGCCTA\",\"P\":[2,5,0],\"Q\":[4,5,6],\"output\":[2,4,1]}"
				+ ", {\"S\":\"C\",\"P\":[0,0,0],\"Q\":[0,0,0],\"output\":[2,2,2]}"
				+ ", {\"S\":\"CA\",\"P\":[0,0,1],\"Q\":[0,1,1],\"output\":[2,1,1]}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("GenomicRangeQuery");
		return ctx;
	}

	@Test
	public void testGenomicRangeQuery() {
		GenomicRangeQuery handler = new GenomicRangeQuery();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			Object[] expected = new Gson().fromJson(object.get("output").getAsJsonArray(), Object[].class);
			Object[] output = handler.handleRequest(object, ctx);
			Assert.assertArrayEquals(expected, output);
		}
	}
}
