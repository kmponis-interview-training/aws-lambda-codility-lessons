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
public class FrogJmpTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"X\":10,\"Y\":85,\"D\":30,\"output\":3}"
				+ ", {\"X\":0,\"Y\":90,\"D\":30,\"output\":-1}" + ", {\"X\":1,\"Y\":1000000000,\"D\":1,\"output\":999999999}"
				+ ", {\"X\":1,\"Y\":10,\"D\":1,\"output\":9}" + ", {\"X\":10,\"Y\":85,\"D\":0,\"output\":-1}"
				+ ", {\"X\":10,\"Y\":90,\"D\":10,\"output\":8}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("FrogJmp");
		return ctx;
	}

	@Test
	public void testFrogJmp() {
		FrogJmp handler = new FrogJmp();
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
