package com.example.homework.util;

import java.io.IOException;

public interface OKHttpGameOfThronesClient {
    String fetchCharacterData(String requestUrl) throws IOException;
}
