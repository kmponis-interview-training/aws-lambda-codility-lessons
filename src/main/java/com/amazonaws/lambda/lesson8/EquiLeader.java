package com.amazonaws.lambda.lesson8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EquiLeader implements RequestHandler<Object, Integer> {

  @Override
  public Integer handleRequest(Object input, Context context) {
    context.getLogger().log("--------------------------");
    context.getLogger().log("Input: " + input.toString().substring(0, Math.min(input.toString().length(), 10000)));
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
    Integer sol1 = getSolution(NA);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;

    context.getLogger().log("Solution 1: " + sol1 + " time " + time1 + " sec");
    context.getLogger().log("--------------------------");
    return sol1;
  }

  private Integer getSolution(int[] NA) {
    int n = NA.length;
    AtomicInteger leader1 = new AtomicInteger(1000000001);
    AtomicInteger leader2 = new AtomicInteger(1000000002);
    AtomicInteger count = new AtomicInteger(0);
    IntStream.range(0, n - 1).forEach(i -> {
      Integer leader1Int = getLeader.apply(Arrays.copyOfRange(NA, 0, i + 1));
      leader1.set(leader1Int == null ? 1000000001 : leader1Int);

      Integer leader2Int = getLeader.apply(Arrays.copyOfRange(NA, i + 1, n));
      leader2.set(leader2Int == null ? 1000000002 : leader2Int);

      if (leader1.get() == leader2.get()) {
        count.getAndIncrement();
      }
    });

    return count.get();
  }

  Function<int[], Integer> getLeader = a -> {
    Map.Entry<Integer, Long> leader1Entry = Arrays.stream(a).boxed()
        .collect(Collectors.groupingBy(j -> j, Collectors.counting())).entrySet().stream()
        .max(Comparator.comparing(Entry::getValue)).filter(x -> x.getValue() > a.length / 2).orElse(null);
    return leader1Entry != null ? leader1Entry.getKey() : null;
  };

  // TODO Solution not working on with all Codility tests
  private Integer getSolution2(int[] NA) {
    int n = NA.length;
    AtomicInteger count = new AtomicInteger(0);
    IntStream.range(0, n - 1).forEach(i -> {
      Integer leader1 = getLeader.apply(Arrays.copyOfRange(NA, 0, i + 1));
      Integer leader2 = getLeader.apply(Arrays.copyOfRange(NA, i + 1, n));
      if (leader1 != 1000000001 && leader2 != 1000000001 && leader1 == leader2) {
        count.getAndIncrement();
      }
      // Integer leader1 = getLeader.apply(Arrays.copyOfRange(NA, 0, i + 1));
      // if (1000000001 != leader1) {
      // Integer leader2 = getLeader.apply(Arrays.copyOfRange(NA, i + 1, n));
      // if (1000000001 != leader2 && leader1 == leader2) {
      // count.getAndIncrement();
      // }
      // }
    });
    return count.get();
  }

  Function<int[], Integer> getLeader2 = a -> {
    return Arrays.stream(a).boxed().collect(Collectors.groupingBy(j -> j, Collectors.counting())).entrySet().stream()
        .max(Comparator.comparing(e -> e.getValue())).map(e -> { // Entry::getValue
          if (e.getValue() > (a.length / 2)) {
            return e.getKey();
          }
          return 1000000001;
        }).get();
  };

}
