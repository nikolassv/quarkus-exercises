package com.example.freepress.press;

import java.time.Instant;
import java.util.Random;

/**
 * The press room stamps every run of the paper.
 *
 * <p>When the class is first loaded it records the moment the paper "went to press" and draws
 * the day's guiding quote from the wall of slogans. The masthead shows both, so readers know
 * the issue in front of them is fresh off the press and which thought the collective is sitting
 * with today.
 */
public final class PressRoom {

    /** The moment this run of the paper went to press. */
    public static final Instant WENT_TO_PRESS = Instant.now();

    private static final String[] SLOGANS = {
            "Make Love, Not Lebenslauf.",
            "The goat is already enlightened.",
            "Peace begins in 7/8.",
            "Bring your own tambourine.",
            "Be like the heap: receive, do not judge.",
            "Turn on, tune in, drop the downbeat."
    };

    /** The slogan drawn for this run of the paper. */
    public static final String QUOTE_OF_THE_DAY = SLOGANS[new Random().nextInt(SLOGANS.length)];

    private PressRoom() {
    }
}
