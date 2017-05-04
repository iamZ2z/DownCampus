package com.logan.acthome.more;

import java.util.List;

public class RateActivityUtil {
    public String title; // hold the question
    public int score;
    public String content;
    public int setscore;

    public List<RateActivityUtil> data;

    public RateActivityUtil(String title, String content,
                            int score, int setscore) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.setscore = setscore;
    }




    public List<RateActivityUtil> getData() {
        return data;
    }

    public void setData(List<RateActivityUtil> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSetscore() {
        return setscore;
    }

    public void setSetscore(int setscore) {
        this.setscore = setscore;
    }
}
