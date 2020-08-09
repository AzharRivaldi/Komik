package com.azhar.komik.model;

import java.io.Serializable;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */
public class ModelChapter implements Serializable {

    private String GenreName;
    private String ChapterTitle;
    private String ChapterEndpoint;
    private String ChapterImage;
    private String ImageNumber;

    public String getGenreName() {
        return GenreName;
    }

    public void setGenreName(String genreName) {
        GenreName = genreName;
    }

    public String getChapterTitle() {
        return ChapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        ChapterTitle = chapterTitle;
    }

    public String getChapterEndpoint() {
        return ChapterEndpoint;
    }

    public void setChapterEndpoint(String chapterEndpoint) {
        ChapterEndpoint = chapterEndpoint;
    }

    public String getChapterImage() {
        return ChapterImage;
    }

    public void setChapterImage(String chapterImage) {
        ChapterImage = chapterImage;
    }

    public String getImageNumber() {
        return ImageNumber;
    }

    public void setImageNumber(String imageNumber) {
        ImageNumber = imageNumber;
    }
}
