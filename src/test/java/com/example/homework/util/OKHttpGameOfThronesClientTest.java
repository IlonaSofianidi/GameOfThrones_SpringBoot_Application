package com.example.homework.util;

import com.example.homework.util.utilImpl.OKHttpGameOfThronesClientImpl;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.core.Is.is;


public class OKHttpGameOfThronesClientTest {
    @Mock
    OkHttpClient client;
    @InjectMocks
    OKHttpGameOfThronesClientImpl unit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFetchCharacterDataReturnJsonString() throws IOException {
        String requestUrl = "https://anapioficeandfire.com/api/characters?name=Aemma%20Arryn";
        String expected = "[{\"url\":\"https://anapioficeandfire.com/api/characters/49\",\"name\":\"Aemma Arryn\",\"gender\":\"Female\",\"culture\":\"\",\"born\":\"In 82 AC\",\"died\":\"In 105 AC\",\"titles\":[\"Queen\"],\"aliases\":[\"\"],\"father\":\"\",\"mother\":\"\",\"spouse\":\"https://anapioficeandfire.com/api/characters/1076\",\"allegiances\":[\"https://anapioficeandfire.com/api/houses/7\",\"https://anapioficeandfire.com/api/houses/378\"],\"books\":[\"https://anapioficeandfire.com/api/books/9\",\"https://anapioficeandfire.com/api/books/10\",\"https://anapioficeandfire.com/api/books/11\"],\"povBooks\":[],\"tvSeries\":[\"\"],\"playedBy\":[\"\"]}]";
        Assert.assertThat(unit.fetchCharacterData(requestUrl), is(expected));
    }

//    @Test(expected = IOException.class)
//    public void whenFetchCharacterDataThrowIOException() throws IOException {
//        String requestUrl = "https://anapioficeandfire.com/api/characters?name=Aemma%20Arryn";
//        Request request = new Request.Builder()
//                .url(requestUrl)
//                .build();
//        Response response = new Response.Builder().code(404)
//                .request(request).protocol(Protocol.HTTP_1_1).message("NOT_FOUND").build();
//        response.i
//            when(client.newCall(request).execute()).thenReturn(response);
//            unit.fetchCharacterData(requestUrl);
//    }
}