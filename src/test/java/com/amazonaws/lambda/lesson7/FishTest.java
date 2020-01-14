package com.amazonaws.lambda.lesson7;

import java.io.IOException;

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
public class FishTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[4,3,2,1,5],\"B\":[0,1,0,0,0],\"output\":2}"
				+ ", {\"A\":[4,3,5,1,2],\"B\":[0,1,0,0,0],\"output\":4}"
				+ ", {\"A\":[4,3,2,1,5],\"B\":[0,0,0,0,1],\"output\":5}"
				+ ", {\"A\":[4,3,2,1,5],\"B\":[1,1,0,1,0],\"output\":3}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("FishTest");
		return ctx;
	}

	@Test
	public void testFish() {
		Fish handler = new Fish();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			Integer expected = object.get("output").getAsInt();
			Integer output = handler.handleRequest(object, ctx);
			Assert.assertEquals(expected, output);
		}
	}
}
