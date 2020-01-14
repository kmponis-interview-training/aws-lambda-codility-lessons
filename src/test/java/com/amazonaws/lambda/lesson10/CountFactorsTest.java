package com.amazonaws.lambda.lesson10;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;
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
public class CountFactorsTest {

  private static Object input;

  @BeforeClass
  public static void createInput() throws IOException {
    System.out.println(MethodHandles.lookup().lookupClass().getSimpleName());

    input = createObject.get();
  }

  private static Supplier<String> createObject = () -> {
    JSONArray jsonMainArray = new JSONArray();
    JSONObject jsonObject;

    jsonObject = new JSONObject();
    jsonObject.put("N", 24);
    jsonObject.put("output", 8);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("N", 1);
    jsonObject.put("output", 1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("N", 5);
    jsonObject.put("output", 2);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("N", 362880);
    jsonObject.put("output", 160);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("N", 5621892);
    jsonObject.put("output", 12);
    jsonMainArray.put(jsonObject);

    JSONObject output = new JSONObject();
    output.put("tests", jsonMainArray);

    System.out.println(output.toString());
    return output.toString();
  };

  private Context createContext() {
    TestContext ctx = new TestContext();
    ctx.setFunctionName(MethodHandles.lookup().lookupClass().getSimpleName());
    return ctx;
  }

  @Test
  public void testSample() {
    CountFactors handler = new CountFactors();
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
