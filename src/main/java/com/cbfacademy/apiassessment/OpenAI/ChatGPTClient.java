package com.cbfacademy.apiassessment.OpenAI;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatGPTClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static Logger logger = LoggerFactory.getLogger(ChatGPTClient.class);

    /**
     * Sends a prompt to the OpenAI GPT-3.5-turbo model specifying the user's goal to generate a
     * workout recommendation.
     *
     * @param value user's goal input
     * @return ChatGPT response
     */
    public static ChatGPTResponse chatGPT(String value) {
        String model = "gpt-3.5-turbo";
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/java/com/cbfacademy/apiassessment/OpenAI/")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        String apiKey = dotenv.get("OPENAI_API_KEY");

        String prompt = "Give me a workout that I can do for achieving this goal: " +
                value + ". give me the json back as a jsonString only in this format: {name: workout I should do for" +
                " achieving my " + value + " goal, suitable_for:[" + value + ", etc]}";

        try {
            BufferedReader br = getBufferedReader(apiKey, model, prompt);

            String line;

            StringBuilder response = new StringBuilder();
            Gson gson = new Gson();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            ChatGPTResponse responseJson = gson.fromJson(response.toString(), ChatGPTResponse.class);
            br.close();
            return responseJson;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Sends a request to the OpenAI API and gets the response as a
     * BufferedReader.
     *
     * @param apiKey api key obtained from OpenAI
     * @param model  ChatGPT model to communicate with (e.g., gpt-3.5-turbo)
     * @param prompt user's question for ChatGPT
     * @return ChatGPT's response as a BufferedReader
     * @throws IOException if there is an issue with the HTTP connection
     */

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
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // ChatGPT response
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));

    }

}
