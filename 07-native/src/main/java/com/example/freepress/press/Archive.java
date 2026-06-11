package com.example.freepress.press;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.List;

/**
 * The bound archive of back issues.
 *
 * <p>The commune never kept a tidy database. What they did keep was a hand-typed JSON ledger
 * of every back issue, passed down on a single floppy disk. On request, that ledger is parsed
 * with Jackson into {@link ArchivedArticle} records and rendered as citation lines for the
 * archive page.
 */
@ApplicationScoped
public class Archive {

    /** The commune's back-issue ledger, exactly as it was typed on the floppy. */
    private static final String LEDGER = """
            [
              { "year": 1967, "title": "The Sit-In at the Trinkhalle",        "author": "Sunflower Brömmelkamp", "section": "PEACE" },
              { "year": 1968, "title": "Twelve Ways to Love Your Neighbour",  "author": "Brigitte Sonnenschein", "section": "FREE_LOVE" },
              { "year": 1969, "title": "Coltrane Came to Me in a Dream",       "author": "Klaus \\"Cosmic\\" Düsterloh", "section": "FUSION_JAZZ" },
              { "year": 1971, "title": "Our Goat Has Achieved Enlightenment",  "author": "Old Man Pott", "section": "PEACE" },
              { "year": 1972, "title": "Modal Scales and the Inner Light",     "author": "Brigitte Sonnenschein", "section": "FUSION_JAZZ" }
            ]
            """;

    @Inject
    ObjectMapper objectMapper;

    /**
     * Reads the ledger and returns one citation line per back issue, e.g.
     * {@code 1969 · "Coltrane Came to Me in a Dream" — Klaus "Cosmic" Düsterloh (FUSION_JAZZ)}.
     */
    public List<String> citations() {
        try {
            ArchivedArticle[] entries = objectMapper.readValue(LEDGER, ArchivedArticle[].class);
            return Arrays.stream(entries)
                    .map(a -> "%d · \"%s\" — %s (%s)".formatted(a.year(), a.title(), a.author(), a.section()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("The archive ledger could not be read", e);
        }
    }
}
