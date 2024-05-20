package com.itnation.promptai.ModelClass;

public class ExploreCategoryModel {

    String categoryName;

    public ExploreCategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public ExploreCategoryModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
