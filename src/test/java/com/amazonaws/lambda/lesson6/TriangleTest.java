package com.amazonaws.lambda.lesson6;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class TriangleTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		System.out.println(MethodHandles.lookup().lookupClass().getSimpleName());

		input = createObject.get();
	}

	private static Supplier<String> createObject = () -> {
		JSONArray jsonMainArray = new JSONArray();
		JSONObject jsonObject;

		jsonObject = new JSONObject();
		jsonObject.put("NA", new JSONArray(new Integer[] { 10, 2, 5, 1, 8, 20 }));
		jsonObject.put("output", 1);
		jsonMainArray.put(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("NA", new JSONArray(new Integer[] { 10, 50, 5, 1 }));
		jsonObject.put("output", 0);
		jsonMainArray.put(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("NA", new JSONArray(new Random().ints(1000, -2147483647, 2147483647).toArray()));
		jsonObject.put("output", -1);
		jsonMainArray.put(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("NA", new JSONArray(new Random().ints(10000, -2147483647, 2147483647).toArray()));
		jsonObject.put("output", -1);
		jsonMainArray.put(jsonObject);

//		jsonObject = new JSONObject();
//		jsonObject.put("NA", new JSONArray(new Random().ints(1000000, -2147483647, 2147483647).toArray()));
//		jsonObject.put("output", -1);
//		jsonMainArray.put(jsonObject);

		JSONObject output = new JSONObject();
		output.put("tests", jsonMainArray);

		System.out.println(output.toString());
		return output.toString();
	};

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(MethodHandles.lookup().lookupClass().getSimpleName());
		return ctx;
	}

	@Test
	public void testSample() {
		Triangle handler = new Triangle();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			Integer expected = object.get("output").getAsInt();
			Integer output = handler.handleRequest(object, ctx);
			if (expected != -1) {
				Assert.assertEquals(expected, output);
			}
		}
	}

}
