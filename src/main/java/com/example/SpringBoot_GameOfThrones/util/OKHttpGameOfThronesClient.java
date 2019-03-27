package com.example.SpringBoot_GameOfThrones.util;

import java.io.IOException;

public interface OKHttpGameOfThronesClient {
    String fetchCharacterData(String requestUrl) throws IOException;
}
