package com.amazonaws.lambda.lesson8;

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
public class DominatorTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[3,4,3,2,3,-1,3,3],\"output\":[0,2,4,6,7]}"
				+ ", {\"A\":[3,2,3],\"output\":[0,2]}" + ", {\"A\":[3,2,2],\"output\":[1,2]}"
				+ ", {\"A\":[3,2,3,4,3,3,3,-1],\"output\":[0,2,4,5,6]}" + ", {\"A\":[3],\"output\":[0]}"
				+ ", {\"A\":[3,4,3,2,3,-1,3,3],\"output\":[0,2,4,6,7]}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("DominatorTest");
		return ctx;
	}

	@Test
	public void testDominator() {
		Dominator handler = new Dominator();
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
