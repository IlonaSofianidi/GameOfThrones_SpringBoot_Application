package com.example.SpringBoot_GameOfThrones.util.utilImpl;

import com.example.SpringBoot_GameOfThrones.util.OKHttpGameOfThronesClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OKHttpGameOfThronesClientImpl implements OKHttpGameOfThronesClient {
    @Override
    public String fetchCharacterData(String requestUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }

    }
}
