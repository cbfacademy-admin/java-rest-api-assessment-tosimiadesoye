package com.cbfacademy.apiassessment.OpenAI;

import com.cbfacademy.apiassessment.userData.UserData;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.cbfacademy.apiassessment.json.ReadAndWriteToJson.readJsonFile;

@Component
public class ChatGPTClient {
    private static final String API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    OkHttpClient client = new OkHttpClient();


    public String communicateWithChatGPT() throws Exception {

        Dotenv dotenv = Dotenv.configure()
                .directory("./src/main/java/com/cbfacademy/apiassessment/OpenAI/")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();


        List<UserData> getData = readJsonFile(new File("src/main/resources/userData.json"), UserData.class);


        String prompt = "I want to grow my" + getData.get(0).getFitness_goal() + ". give me  "
                + getData.get(0).getFitness_goal() + " exercise workouts and healthy" + getData.get(0).getDietary_preference() + "ideas for breakfast, lunch and dinner that would help me achieve this";

        String apiKey = dotenv.get("OPENAI_API_KEY");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"prompt\": \"" + prompt + "\"}");

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return responseBody;
    }

}

