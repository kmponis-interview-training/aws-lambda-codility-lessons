package com.amazonaws.lambda.lesson7;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

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
public class NestingTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"S\":\"(()(())())\",\"output\":1}" + ", {\"S\":\"())\",\"output\":0}"
				+ ", {\"S\":\"\",\"output\":1}" + ", {\"S\":\"()(()()(((()())(()()))\",\"output\":0}"
				+ ", {\"S\":\"(())()()\",\"output\":1}" + ", {\"S\":\"())(()\",\"output\":0}]}";
		System.out.println(MethodHandles.lookup().lookupClass().getSimpleName());
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(MethodHandles.lookup().lookupClass().getSimpleName());
		return ctx;
	}

	@Test
	public void testSample() {
		Nesting handler = new Nesting();
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
