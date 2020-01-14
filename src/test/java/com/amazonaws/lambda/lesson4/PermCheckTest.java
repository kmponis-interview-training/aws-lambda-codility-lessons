package com.amazonaws.lambda.lesson4;

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
public class PermCheckTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[3,1,2,4], \"output\":1}" + ", {\"A\":[3,1,4], \"output\":0}"
				+ ", {\"A\":[1000,1004,2,1000,3], \"output\":0}" + ", {\"A\":[3,2,4], \"output\":0}"
				+ ", {\"A\":[3,4,2,1,2], \"output\":0}, {\"A\":[3,1,2,4], \"output\":1}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("PermCheckTest");
		return ctx;
	}

	@Test
	public void testPermCheck() {
		PermCheck handler = new PermCheck();
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
