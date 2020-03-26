package com.amazonaws.lambda.challenges;

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
public class Ruthenium2019Test {

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
    jsonObject.put("NA", new JSONArray(new Integer[] { 1, 1, 3, 4, 3, 3, 4 }));
    jsonObject.put("K", 2);
    jsonObject.put("output", 5);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 4, 5, 5, 4, 2, 2, 4 }));
    jsonObject.put("K", 0);
    jsonObject.put("output", 2);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 2, 5, 1, 3, 3, 2 }));
    jsonObject.put("K", 2);
    jsonObject.put("output", 4);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 1 }));
    jsonObject.put("K", 1);
    jsonObject.put("output", 1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 1 }));
    jsonObject.put("K", 0);
    jsonObject.put("output", 1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 1, 3 }));
    jsonObject.put("K", 2);
    jsonObject.put("output", 2);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Integer[] { 1, 3, 3, 3, 4, 2, 2, 4, 2, 2 }));
    jsonObject.put("K", 1);
    jsonObject.put("output", 5);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Random().ints(100000, 1, 100000).toArray()));
    jsonObject.put("K", 5);
    jsonObject.put("output", -1);
    jsonMainArray.put(jsonObject);

    jsonObject = new JSONObject();
    jsonObject.put("NA", new JSONArray(new Random().ints(100000, 1, 100000).toArray()));
    jsonObject.put("K", 7);
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
    Ruthenium2019 handler = new Ruthenium2019();
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
