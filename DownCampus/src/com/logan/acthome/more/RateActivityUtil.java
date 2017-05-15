package com.logan.acthome.more;

import java.util.List;

public class RateActivityUtil {
    public String title; // hold the question
    public int score;
    public String content;
    public String setscore;

    public List<RateActivityUtil> data;

    public RateActivityUtil(String title, String content,
                            int score, String setscore) {
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

    public String getSetscore() {
        return setscore;
    }

    public void setSetscore(String setscore) {
        this.setscore = setscore;
    }
}
