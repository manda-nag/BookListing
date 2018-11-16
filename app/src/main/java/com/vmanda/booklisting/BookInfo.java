package com.vmanda.booklisting;

class BookInfo {
    private final String title;
    private final String author;
    private final String url;
    private final String imageUrl;

    public BookInfo(String title, String author, String url, String imageUrl) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
