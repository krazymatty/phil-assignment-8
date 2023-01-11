package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CountNumbersApplication {
	public static void main(String[] args) {
		Assignment8 assignment8 = new Assignment8();
		List<Integer> allNumbers = Collections.synchronizedList(new ArrayList<>(1000));
		List<CompletableFuture<Void>> tasks = Collections.synchronizedList(new ArrayList<>(1000));
		ExecutorService executor = Executors.newCachedThreadPool();

		for (int i = 0; i < 1000; i++) {
			CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> assignment8.getNumbers(), executor)
					.thenAccept(numbers -> allNumbers.addAll(numbers));
			tasks.add(task);
		}
		while (tasks.stream().filter(CompletableFuture::isDone).count() < 1000) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done: Size of task list: " + allNumbers.size());

		Map<Integer, Integer> results = allNumbers.stream()
				.collect(Collectors.toMap(i -> i, i -> 1, (oldValue, newValue) -> oldValue + 1));
		System.out.println(results);
	}
}
