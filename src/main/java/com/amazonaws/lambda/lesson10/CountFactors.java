package com.amazonaws.lambda.lesson10;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CountFactors implements RequestHandler<Object, Integer> {

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

    long start3 = System.nanoTime();
    Integer sol3 = getSolution3(N);
    long end3 = System.nanoTime();
    Double time3 = Math.round(((end3 - (double) start3) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 3: " + sol3 + " - time " + time3 + " sec");

    long start4 = System.nanoTime();
    Integer sol4 = getSolution4(N);
    long end4 = System.nanoTime();
    Double time4 = Math.round(((end4 - (double) start4) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 4: " + sol4 + " - time " + time4 + " sec");

    context.getLogger().log("--------------------------");
    return sol2;
  }

  private Integer getSolution1(Integer N) {
    AtomicInteger count = new AtomicInteger();
    long asd = IntStream.range(1, N + 1).mapToObj(i -> {
      if (N % i == 0) {
        count.getAndIncrement();
      }
      return count.get();
    }).distinct().count();

    return (int) asd;
  }

  private Integer getSolution2(Integer N) {
    AtomicInteger count = new AtomicInteger();
    return (int) IntStream.range(1, N + 1).mapToObj(i -> (N % i == 0) ? count.getAndIncrement() : count.get())
        .distinct().count();
  }

  private Integer getSolution3(Integer N) {
    AtomicInteger count = new AtomicInteger();
    IntStream.range(1, N + 1).filter(i -> (N % i == 0)).forEach(i -> count.getAndIncrement());
    return count.get();
  }

  private Integer getSolution4(Integer N) {
    return (int) IntStream.range(1, N + 1).filter(i -> (N % i == 0)).count();
  }

}
