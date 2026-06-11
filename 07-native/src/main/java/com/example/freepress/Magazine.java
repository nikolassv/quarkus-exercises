package com.example.freepress;

import com.example.freepress.model.Article;
import com.example.freepress.model.Comment;
import com.example.freepress.model.Section;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The current issue of the Wanneeikel Free Press, held entirely in memory.
 *
 * <p>Articles are baked into the binary as plain Java; reader comments accumulate at runtime.
 * Nothing in this class is troublesome for native compilation — it is the part of the
 * application that simply works. The interesting failures live in the {@code press} package.
 */
@ApplicationScoped
public class Magazine {

    private final List<Article> articles = List.of(
            new Article(1, "Make Love, Not Lebenslauf",
                    "Sunflower Brömmelkamp", Section.FREE_LOVE,
                    "Why we burned our employment contracts in the meadow behind the supermarket."),
            new Article(2, "Peace Walk to Wanne-Eickel Hauptbahnhof",
                    "Klaus \"Cosmic\" Düsterloh", Section.PEACE,
                    "Forty barefoot dreamers, one bewildered Bahnhofsvorsteher, and a banjo."),
            new Article(3, "On the Spiritual Necessity of the Rhodes Piano",
                    "Brigitte Sonnenschein", Section.FUSION_JAZZ,
                    "How a wobbling electric piano taught the commune to breathe in 7/8."),
            new Article(4, "Composting as a Path to Inner Peace",
                    "Old Man Pott", Section.PEACE,
                    "The heap does not judge. The heap only receives. Be like the heap.")
    );

    private final Map<Integer, List<Comment>> comments = new ConcurrentHashMap<>();

    public List<Article> all() {
        return articles;
    }

    public Optional<Article> byId(int id) {
        return articles.stream().filter(a -> a.id() == id).findFirst();
    }

    public List<Comment> commentsFor(int articleId) {
        return List.copyOf(comments.getOrDefault(articleId, List.of()));
    }

    public Comment addComment(int articleId, String author, String text) {
        Comment comment = new Comment(author, text, Instant.now());
        comments.computeIfAbsent(articleId, key -> new CopyOnWriteArrayList<>()).add(comment);
        return comment;
    }
}
