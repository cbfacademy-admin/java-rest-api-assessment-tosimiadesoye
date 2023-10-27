package com.cbfacademy.apiassessment.OpenAI;

import io.github.cdimascio.dotenv.Dotenv;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ChatGPTClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static Logger logger = LoggerFactory.getLogger(ChatGPTClient.class);

    public static String chatGPT(String value) {
        String model = "gpt-3.5-turbo";
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/java/com/cbfacademy/apiassessment/OpenAI/")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        String apiKey = dotenv.get("OPENAI_API_KEY");

        String prompt = "Give me a workout that's suitable for " +
                value + " Your response should be in this format e.g  '{name: string, suitable_for:[]}' ";


        try {
//            URL obj = new URL(API_URL);
//            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // the request body
//            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt
//                    + "\"}]}";
//
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = body.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            // ChatGPT response
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            BufferedReader br = getBufferedReader(apiKey, model, prompt);

            String line;

            StringBuilder response = new StringBuilder();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return extractMessageFromJSONResponse(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @NotNull
    private static BufferedReader getBufferedReader(String apiKey, String model, String prompt) throws IOException {
        URL obj = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // the request body
        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt
                + "\"}]}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // ChatGPT response

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br;
    }

    private static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;

        int end = response.indexOf("\"", start);
        logger.info(response.substring(start, end));
        return response.substring(start, end);
    }

}
