package com.amazonaws.lambda.challenges;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Ruthenium2019 implements RequestHandler<Object, Integer> {

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
    int K = jsonObject.get("K").getAsInt();

    long start1 = System.nanoTime();
    Integer sol1 = getSolution(NA, K);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " time " + time1 + " sec");

    long start2 = System.nanoTime();
    Integer sol2 = getSolution2(NA, K);
    long end2 = System.nanoTime();
    Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 2: " + sol2 + " time " + time2 + " sec");

    context.getLogger().log("--------------------------");
    return sol2;
  }

  private Integer getSolution(int[] na, int k) {
    int n = na.length;
    if (k >= n - 1) {
      return n;
    }

    int maxCount = 0;
    for (int i = 0; i < n - 1; i++) {
      if (i != 0 && na[i] == na[i - 1]) {
        continue;
      }

      int previous = -1;
      int count = 0;
      int kTemp = k;
      for (int j = i; j < n; j++) {
        if (previous == -1) {
          previous = na[j];
          count = 1;
        } else {
          if (na[j] == previous) {
            count++;
          } else if (kTemp > 0) {
            count++;
            kTemp--;
          } else {
            break;
          }
        }
      }
      maxCount = count + kTemp > maxCount ? count + kTemp : maxCount;
    }

    return maxCount > n ? n : maxCount;
  }

  private Integer getSolution2(int[] na, int k) {
    int n = na.length;
    if (k >= n - 1) {
      return n;
    }

    AtomicInteger maxCount = new AtomicInteger(0);
    IntStream.range(0, n - 1).filter(i -> !(i != 0 && na[i] == na[i - 1])).forEach(i -> {
      int previous = -1;
      int countTemp = 0;
      int kTemp = k;
      for (int j = i; j < n; j++) {
        if (previous == -1) {
          previous = na[j];
          countTemp = 1;
        } else {
          if (na[j] == previous) {
            countTemp++;
          } else if (kTemp > 0) {
            countTemp++;
            kTemp--;
          } else {
            break;
          }
        }
      }

      int count = countTemp + kTemp > n ? n : countTemp + kTemp;
      maxCount.set(count > maxCount.get() ? count : maxCount.get());
    });

    return maxCount.get();
  }

}
