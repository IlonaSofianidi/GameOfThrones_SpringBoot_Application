package com.example.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameCharacter {
    private String name;
    private String url;
    private String mother;
    private String father;
    private String spouse;
}
