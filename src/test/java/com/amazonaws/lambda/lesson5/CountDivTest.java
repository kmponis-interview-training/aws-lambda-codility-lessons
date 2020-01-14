package com.amazonaws.lambda.lesson5;

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
public class CountDivTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":6,\"B\":11,\"K\":2,\"output\":3}"
				+ ", {\"A\":0,\"B\":2000000000,\"K\":1,\"output\":2000000000}" + ", {\"A\":6,\"B\":11,\"K\":3,\"output\":2}"
				+ ", {\"A\":0,\"B\":30000,\"K\":2,\"output\":15000}" + ", {\"A\":1,\"B\":115677,\"K\":45,\"output\":2570}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("CountDivTest");
		return ctx;
	}

	@Test
	public void testCountDiv() {
		CountDiv handler = new CountDiv();
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
