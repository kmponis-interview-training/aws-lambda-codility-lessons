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
public class FrogRiverOneTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}"
				+ ", {\"X\":5,\"A\":[1,3,1,2,2,3,5,4], \"output\":-1}" + ", {\"X\":5,\"A\":[1,3,1,1,2,3,5,4,5], \"output\":8}"
				+ ", {\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}" + ", {\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}"
				+ ", {\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}" + ", {\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}"
				+ ", {\"X\":5,\"A\":[1,3,1,4,2,3,5,4], \"output\":6}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("FrogRiverOneTest");

		return ctx;
	}

	@Test
	public void testFrogRiverOne() {
		FrogRiverOne handler = new FrogRiverOne();
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
