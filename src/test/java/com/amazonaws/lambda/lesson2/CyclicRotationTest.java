package com.amazonaws.lambda.lesson2;

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
public class CyclicRotationTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[9,3,9,3,9,7],\"K\":6,\"output\":[9,3,9,3,9,7]}"
				+ ", {\"A\":[0,0,0],\"K\":1,\"output\":[0,0,0]}" + ", {\"A\":[3,8,9,7,6],\"K\":3,\"output\":[9,7,6,3,8]}"
				+ ", {\"A\":[-5,0,-4],\"K\":30,\"output\":[-5,0,-4]}" + ", {\"A\":[4,-5,0],\"K\":31,\"output\":[0,4,-5]}"
				+ ", {\"A\":[0,0,0],\"K\":1,\"output\":[0,0,0]}" + ", {\"A\":[0,0,0],\"K\":1,\"output\":[0,0,0]}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("CyclicRotationTest");
		return ctx;
	}

	@Test
	public void testCyclicRotation() {
		CyclicRotation handler = new CyclicRotation();
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
