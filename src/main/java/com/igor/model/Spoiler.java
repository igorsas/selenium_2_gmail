package com.igor.model;

import java.util.Arrays;
import java.util.List;

public class Spoiler {
    private static final List<String> spoilers = Arrays.asList(
            "Natasha was hot, but she's gone.",
            "Mr. Stark died.",
            "They travelled in time.",
            "Captain America has almost gone.");

    public static String getSpoiler(){
        return spoilers.get((int)(Math.random()*spoilers.size()));
    }
}
