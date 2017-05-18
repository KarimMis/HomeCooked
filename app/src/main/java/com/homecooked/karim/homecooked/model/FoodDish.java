package com.homecooked.karim.homecooked.model;

/**
 * Created by cazankova on 08/05/2017.
 */

public class FoodDish {



    String foodName ;
    String weight ;
    String ingredients ;
    String  dish_about;
    String serves;
    String price ;
    String timeToPrepared;
    String foodUrl;


    public FoodDish(){}

  public   FoodDish(String foodName,String ingredients,String  dish_about, String serves ,String weight , String price,String timeToPrepared ,String foodUrl){

        this.foodName=foodName;
        this.ingredients=ingredients;
        this.dish_about=dish_about;
        this.serves=serves;
        this.weight = weight;
        this.price = price;
        this.timeToPrepared= timeToPrepared;
        this.foodUrl = foodUrl;
    }


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeToPrepared() {
        return timeToPrepared;
    }

    public void setTimeToPrepared(String timeToPrepared) {
        this.timeToPrepared = timeToPrepared;
    }

    public String getFoodUrl() {
        return foodUrl;
    }

    public void setFoodUrl(String foodUrl) {
        this.foodUrl = foodUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDish_about() {
        return dish_about;
    }

    public void setDish_about(String dish_about) {
        this.dish_about = dish_about;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }
}
