package com.amazonaws.lambda.toptap.tech.interview;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.javatuples.Pair;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Numeration implements RequestHandler<Object, Integer> {

  @Override
  public Integer handleRequest(Object input, Context context) {
    context.getLogger().log("------------------------------------");
    context.getLogger().log("Input: " + input);

    JsonObject jsonObject;
    try {
      JsonParser parser = new JsonParser();
      JsonElement jsonTree = parser.parse(input.toString());
      jsonObject = jsonTree.getAsJsonObject();
      context.getLogger().log("Use JsonParser to parse a JSON text");
    } catch (Exception ex) {
      Gson gson = new GsonBuilder().create();
      JsonElement jsonElement = gson.toJsonTree(input);
      jsonObject = jsonElement.getAsJsonObject();
      context.getLogger().log("Use GsonBuilder to parse a non generic type object");
    }
    Integer A = jsonObject.get("A").getAsInt();
    Integer B = jsonObject.get("B").getAsInt();

    long start1 = System.nanoTime();
    Integer sol1 = getSolution1(A, B);
    long end1 = System.nanoTime();
    Double time1 = Math.round(((end1 - (double) start1) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 1: " + sol1 + " - time " + time1 + " sec");

    long start2 = System.nanoTime();
    Integer sol2 = getSolution2(A, B);
    long end2 = System.nanoTime();
    Double time2 = Math.round(((end2 - (double) start2) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 2: " + sol2 + " - time " + time2 + " sec");

    long start3 = System.nanoTime();
    Integer sol3 = getSolution3(A, B);
    long end3 = System.nanoTime();
    Double time3 = Math.round(((end3 - (double) start3) / 1000000000) * 100000d) / 100000d;
    context.getLogger().log("Solution 3: " + sol3 + " - time " + time3 + " sec");

    context.getLogger().log("------------------------------------");
    return sol2;
  }

  private Integer getSolution1(int A, int B) {
    String reserveA = new StringBuffer(A + "").reverse().toString();
    String reserveB = new StringBuffer(B + "").reverse().toString();
    int lengthA = reserveA.length();
    int lengthB = reserveB.length();
    int longestLength = lengthB > lengthA ? lengthB : lengthA;

    AtomicInteger countNumber = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);
    IntStream.range(0, longestLength).forEach(i -> {
      int a = (i >= lengthA ? 0 : reserveA.charAt(i) - '0');
      int b = (i >= lengthB ? 0 : reserveB.charAt(i) - '0');

      if ((a + b + count.get()) > 9) {
        countNumber.getAndIncrement();
        count.getAndSet(1);
      } else {
        count.getAndSet(0);
      }
    });

    return countNumber.get();
  }

  private Integer getSolution2(int A, int B) {
    int[] reserveA = new StringBuffer(A + "").reverse().toString().chars().map(a -> a - '0').toArray();
    int[] reserveB = new StringBuffer(B + "").reverse().toString().chars().map(b -> b - '0').toArray();
    int lengthA = reserveA.length;
    int lengthB = reserveB.length;
    int longestLength = lengthB > lengthA ? lengthB : lengthA;

    AtomicInteger countNumber = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);
    IntStream.range(0, longestLength).forEach(i -> {
      int a = (i >= lengthA ? 0 : reserveA[i]);
      int b = (i >= lengthB ? 0 : reserveB[i]);
      if ((a + b + count.get()) > 9) {
        countNumber.getAndIncrement();
        count.getAndSet(1);
      } else {
        count.getAndSet(0);
      }
    });

    return countNumber.get();
  }

  private Integer getSolution3(int A, int B) {
    int[] reserveA = new StringBuffer(A + "").reverse().toString().chars().map(a -> a - '0').toArray();
    int[] reserveB = new StringBuffer(B + "").reverse().toString().chars().map(b -> b - '0').toArray();
    int lengthA = reserveA.length;
    int lengthB = reserveB.length;
    int longestLength = lengthB > lengthA ? lengthB : lengthA;

    AtomicInteger countNumber = new AtomicInteger(0);
    AtomicInteger count = new AtomicInteger(0);
    IntStream.range(0, longestLength)
        .mapToObj(i -> Pair.with((i >= lengthA ? 0 : reserveA[i]), (i >= lengthB ? 0 : reserveB[i]))).forEach(pair -> {
          if ((pair.getValue0() + pair.getValue1() + count.get()) > 9) {
            countNumber.getAndIncrement();
            count.getAndSet(1);
          } else {
            count.getAndSet(0);
          }
        });

    return countNumber.get();
  }

}
