package com.amazonaws.lambda.lesson3;

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
public class PermMissingElemTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":[2,3,1,5],\"output\":4}" + ", {\"A\":[5,4,2,3],\"output\":1}"
				+ ", {\"A\":[9],\"output\":1}" + ", {\"A\":[1],\"output\":2}" + ", {\"A\":[1,2,3,4,5],\"output\":6}"
				+ ", {\"A\":[2,1],\"output\":3}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("PermMissingElem");
		return ctx;
	}

	@Test
	public void testPermMissingElem() {
		PermMissingElem handler = new PermMissingElem();
		Context ctx = createContext();

		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse((String) input);
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

		for (int i = 0; i < testsArray.size(); i++) {
			JsonObject object = testsArray.get(i).getAsJsonObject();

			int expected = object.get("output").getAsInt();
			int output = handler.handleRequest(object, ctx);
			Assert.assertEquals(expected, output);
		}
	}
}
