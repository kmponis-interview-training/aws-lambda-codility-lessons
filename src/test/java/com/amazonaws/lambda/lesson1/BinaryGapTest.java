package com.amazonaws.lambda.lesson1;

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
public class BinaryGapTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"N\":1041,\"output\":5}" + ", {\"N\":32,\"output\":0}"
				+ ", {\"N\":529,\"output\":4}" + ", {\"N\":20,\"output\":1}" + ", {\"N\":15,\"output\":0}"
				+ ", {\"N\":9,\"output\":2}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("BinaryGapTest");
		return ctx;
	}

	@Test
	public void testBinaryGap() {
		BinaryGap handler = new BinaryGap();
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
