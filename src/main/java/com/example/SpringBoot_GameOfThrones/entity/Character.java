package com.example.SpringBoot_GameOfThrones.entity;

import lombok.Data;

@Data
public class Character {
    private String name;
    private int characterId ;
    private String mother;
    private String father;
    private String spouse;
}
