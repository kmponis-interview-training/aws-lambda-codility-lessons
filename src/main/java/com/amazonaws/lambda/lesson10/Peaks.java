package com.amazonaws.lambda.lesson10;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Peaks implements RequestHandler<Object, Integer> {

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

    context.getLogger().log("--------------------------");
    return sol1;
  }

  private Integer getSolution1(int[] NA) {
    int maxNumber = 1000000001;
    int n = NA.length;
    int numberOfBlocks = 0;
    for (int i = 1; i < n; i++) {
      int chunk = n / i;
      for (int j = 0; j < n; j += chunk) {
        if (i * chunk == n) {
          int[] currentChunk = Arrays.copyOfRange(NA, j, Math.min(NA.length, j + chunk));

          int beforeFirst = (i == 1 || j == 0) ? maxNumber : NA[j - 1];
          int afterLast = (i == 1 || j + chunk == n) ? maxNumber : NA[j + chunk];
          int[] currentPeak = getPeak(currentChunk, beforeFirst, afterLast);
          if (currentPeak.length == 0) {
            return numberOfBlocks;
          } else if (i == 1 || (j + chunk == n)) {
            numberOfBlocks = i;
          }
        }
      }
    }

    return numberOfBlocks;
  }

  private int[] getPeak(int[] array, int first, int last) {
    int n = array.length;

    return IntStream.range(0, n).map(i -> {
      int bef = i == 0 ? first : array[i - 1];
      int aft = i == (n - 1) ? last : array[i + 1];
      if (array[i] > bef && array[i] > aft) {
        return i;
      }
      return -1;
    }).filter(i -> i != -1).toArray();
  }

}
