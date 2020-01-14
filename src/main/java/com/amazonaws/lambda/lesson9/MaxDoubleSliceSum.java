package com.amazonaws.lambda.lesson9;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MaxDoubleSliceSum implements RequestHandler<Object, Integer> {

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
    int[] NA = new Gson().fromJson(jsonObject.get("NA").getAsJsonArray(), int[].class);

    long start1 = System.nanoTime();
    Integer sol1 = getSolution1(NA);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " - time " + time1 + " sec");

    long start2 = System.nanoTime();
    Integer sol2 = getSolution2(NA);
    long end2 = System.nanoTime();
    Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 2: " + sol2 + " - time " + time2 + " sec");

    context.getLogger().log("--------------------------");
    return sol1;
  }

  private Integer getSolution1(int[] NA) {
    int n = NA.length;
    int maxSum = 0;
    for (int i = 0; i < n - 2; i++) {
      for (int j = i + 1; j < n - 1; j++) {
        for (int k = j + 1; k < n; k++) {
          int sum = IntStream.of(Arrays.copyOfRange(NA, i + 1, j)).sum()
              + IntStream.of(Arrays.copyOfRange(NA, j + 1, k)).sum();
          maxSum = sum > maxSum ? sum : maxSum;
        }
      }
    }

    return maxSum;
  }

  private Integer getSolution2(int[] NA) {
    int n = NA.length;
    int maxSum = 0;
    for (int i = 0; i < n - 2; i++) {

      int j = i + 1;
      int k = n - 1;
      while (j < k) {
        int sum = IntStream.of(Arrays.copyOfRange(NA, i + 1, j)).sum()
            + IntStream.of(Arrays.copyOfRange(NA, j + 1, k)).sum();
        maxSum = sum > maxSum ? sum : maxSum;

        if (j + 1 < k) {
          k--;
        } else {
          j++;
          k = n - 1;
        }
      }
    }

    return maxSum;
  }

}
