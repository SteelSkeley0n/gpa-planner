package com.gpa.planner.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenRouterService {

    private static final String API_KEY = "AIzaSyBC-mjldL6HY0zVj0B1Ve0tOo1_A8M-8Ys";

    public String generatePlan(String goalTitle, String description) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .protocols(java.util.Collections.singletonList(okhttp3.Protocol.HTTP_1_1))
                .build();

        String prompt = "You must return ONLY valid JSON. No explanation.\n\n" +
                "Break this goal into tasks.\n\n" +
                "Goal: " + goalTitle + "\n" +
                "Description: " + description + "\n\n" +
                "Return strictly this format:\n" +
                "{\"tasks\":[{\"title\":\"\",\"description\":\"\",\"difficulty\":\"EASY|MEDIUM|HARD\",\"estimatedHours\":0}]}";

        String json = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"text\": \"" + prompt.replace("\"", "\\\"") + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"generationConfig\": {\n" +
                "    \"temperature\": 0.2\n" +
                "  }\n" +
                "}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        //Response response = client.newCall(request).execute();
        Response response = client.newCall(request).execute();

        String responseBody = response.body() != null ? response.body().string() : null;

        System.out.println("STATUS CODE: " + response.code());
        System.out.println("RAW RESPONSE: " + responseBody);

        return responseBody;

        //return response.body().string();
    }
}