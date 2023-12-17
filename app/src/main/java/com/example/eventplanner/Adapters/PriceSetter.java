package com.example.eventplanner.Adapters;

import android.content.Context;
import android.content.SharedPreferences;

public class PriceSetter {
Double buffe,ethFood,fastingBuffe,birthdayCake,iceCream,cookies,
        weddingCake,beer,water,softDrink,hotDrinks,decor,band,stage,speaker;

    public PriceSetter(Double buffe, Double ethFood, Double fastingBuffe, Double birthdayCake, Double iceCream, Double cookies, Double weddingCake, Double beer, Double water, Double softDrink, Double hotDrinks, Double decor, Double band, Double stage, Double speaker) {
        this.buffe = buffe;
        this.ethFood = ethFood;
        this.fastingBuffe = fastingBuffe;
        this.birthdayCake = birthdayCake;
        this.iceCream = iceCream;
        this.cookies = cookies;
        this.weddingCake = weddingCake;
        this.beer = beer;
        this.water = water;
        this.softDrink = softDrink;
        this.hotDrinks = hotDrinks;
        this.decor = decor;
        this.band = band;
        this.stage = stage;
        this.speaker = speaker;
    }

    public Double getBuffe() {
        return buffe;
    }

    public Double getEthFood() {
        return ethFood;
    }

    public Double getFastingBuffe() {
        return fastingBuffe;
    }

    public Double getBirthdayCake() {
        return birthdayCake;
    }

    public Double getIceCream() {
        return iceCream;
    }

    public Double getCookies() {
        return cookies;
    }

    public Double getWeddingCake() {
        return weddingCake;
    }

    public Double getBeer() {
        return beer;
    }

    public Double getWater() {
        return water;
    }

    public Double getSoftDrink() {
        return softDrink;
    }

    public Double getHotDrinks() {
        return hotDrinks;
    }

    public Double getDecor() {
        return decor;
    }

    public Double getBand() {
        return band;
    }

    public Double getStage() {
        return stage;
    }

    public Double getSpeaker() {
        return speaker;
    }
}
