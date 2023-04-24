package com.proxy.seller.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String text;
    private int likes;

    public Note() {
    }

    public Note(String text) {
        this.text = text;
        this.likes = 0;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getLikes() {
        return likes;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        this.likes--;
    }
}
