package com.amazonaws.lambda.lesson10;

import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MinPerimeterRectangle implements RequestHandler<Object, Integer> {

  @Override
  public Integer handleRequest(Object input, Context context) {
    context.getLogger().log("--------------------------");
    context.getLogger().log("Input: " + input.toString().substring(0, Math.min(input.toString().length(), 1000)));
    JsonObject jsonObject;
    try {
      JsonParser parser = new JsonParser();
      JsonElement jsonElement = parser.parse(input.toString());
      jsonObject = jsonElement.getAsJsonObject();
    } catch (Exception ex) {
      context.getLogger().log("Use GsonBuilder to parse a JSON object");
      Gson gson = new GsonBuilder().create();
      JsonElement jsonElement = gson.toJsonTree(input);
      jsonObject = jsonElement.getAsJsonObject();
    }
    int N = jsonObject.get("N").getAsInt();

    long start1 = System.nanoTime();
    Integer sol1 = getSolution1(N);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " - time " + time1 + " sec");

    long start2 = System.nanoTime();
    Integer sol2 = getSolution2(N);
    long end2 = System.nanoTime();
    Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 2: " + sol2 + " - time " + time2 + " sec");

    context.getLogger().log("--------------------------");
    return sol1;
  }

  private Integer getSolution1(Integer N) {
    int minPerimeter = 2147483647;
    for (int i = 1; i <= N; i++) {
      int a = i;
      int b = N / i;

      if (a > b) {
        break;
      }

      if (a * b == N) {
        minPerimeter = 2 * (a + b) < minPerimeter ? (2 * (a + b)) : minPerimeter;
      }
    }

    return minPerimeter;
  }

  private Integer getSolution2(Integer N) {
    return IntStream.range(1, N + 1).mapToObj(i -> new Integer[] { i, (N / i) })
        .filter(i -> i[0] <= i[1] && i[0] * i[1] == N).mapToInt(i -> 2 * (i[0] + i[1])).min().orElse(0);
  }

}
