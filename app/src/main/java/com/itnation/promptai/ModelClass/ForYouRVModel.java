package com.itnation.promptai.ModelClass;

public class ForYouRVModel {
    String imgLink, promptTxt;

    public ForYouRVModel(String imgLink, String promptTxt) {
        this.imgLink = imgLink;
        this.promptTxt = promptTxt;
    }

    public ForYouRVModel() {
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getPromptTxt() {
        return promptTxt;
    }

    public void setPromptTxt(String promptTxt) {
        this.promptTxt = promptTxt;
    }
}
