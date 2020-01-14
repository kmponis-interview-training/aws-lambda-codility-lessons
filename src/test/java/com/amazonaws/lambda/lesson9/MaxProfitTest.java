package com.amazonaws.lambda.lesson9;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Random;
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
public class MaxProfitTest {

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
    jsonObject.put("NA", new JSONArray(new Integer[] { 23171, 21011, 21123, 21366, 21013, 21367 }));
    jsonObject.put("output", 356);
    jsonMainArray.put(jsonObject);

    jsonObject.put("NA", new JSONArray(new Integer[] {}));
    jsonObject.put("output", 0);
    jsonMainArray.put(jsonObject);

    jsonObject.put("NA", new JSONArray(new Integer[] { 23171, 21011, 21010, 21009 }));
    jsonObject.put("output", 0);
    jsonMainArray.put(jsonObject);

    jsonObject.put("NA", new JSONArray(new Integer[] { 23171 }));
    jsonObject.put("output", 0);
    jsonMainArray.put(jsonObject);

    jsonObject.put("NA", new JSONArray(new Integer[] { 23171, 23171 }));
    jsonObject.put("output", 0);
    jsonMainArray.put(jsonObject);

    jsonObject.put("NA", new JSONArray(new Integer[] { 23171, 23172 }));
    jsonObject.put("output", 1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Random().ints(10000, 0, 200000).toArray())); // 400000
    jsonObject.put("output", -1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Random().ints(100000, 1, 200000).toArray())); // 400000
    jsonObject.put("output", -1);
    jsonMainArray.put(jsonObject);

//    jsonObject = new JSONObject();
//    jsonObject.put("NA",
//        new JSONArray(Stream
//            .concat(new Random().ints(200000, 100000, 120000).boxed(), new Random().ints(200000, 0, 100000).boxed())
//            .mapToInt(i -> i).toArray())); // 400000
//    jsonObject.put("output", -1);
//    jsonMainArray.put(jsonObject);

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
    MaxProfit handler = new MaxProfit();
    Context ctx = createContext();

    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse((String) input);
    JsonObject rootObject = jsonElement.getAsJsonObject();
    JsonArray testsArray = rootObject.get("tests").getAsJsonArray();

    for (int i = 0; i < testsArray.size(); i++) {
      JsonObject object = testsArray.get(i).getAsJsonObject();

      Integer expected = object.get("output").getAsInt();
      Integer output = handler.handleRequest(object, ctx);
      if (-1 != expected) {
        Assert.assertEquals(expected, output);
      }

    }
  }

}
