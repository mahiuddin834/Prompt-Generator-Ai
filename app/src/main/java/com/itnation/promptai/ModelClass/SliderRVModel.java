package com.itnation.promptai.ModelClass;

public class SliderRVModel {

    String imgLink, promptTxt;

    public SliderRVModel(String imgLink, String promptTxt) {
        this.imgLink = imgLink;
        this.promptTxt = promptTxt;
    }

    public SliderRVModel() {
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
