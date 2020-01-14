package com.amazonaws.lambda.toptap.tech.interview;

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
public class NumerationTest {

	private static Object input;

	@BeforeClass
	public static void createInput() throws IOException {
		String jsonString = "{\"tests\":[{\"A\":123,\"B\":56,\"output\":0}" + ", {\"A\":555,\"B\":555,\"output\":3}"
				+ ", {\"A\":900,\"B\":11,\"output\":0}" + ", {\"A\":145,\"B\":55,\"output\":2}"
				+ ", {\"A\":0,\"B\":0,\"output\":0}" + ", {\"A\":1,\"B\":99999,\"output\":5}"
				+ ", {\"A\":999045,\"B\":1055,\"output\":5}" + ", {\"A\":101,\"B\":809,\"output\":1}"
				+ ", {\"A\":189,\"B\":209,\"output\":1}]}";
		System.out.println(jsonString);

		input = jsonString;
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("NumerationTest");
		return ctx;
	}

	@Test
	public void testNumeration() {
		Numeration handler = new Numeration();
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
