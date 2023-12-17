package com.example.eventplanner.Objects;

import java.util.ArrayList;

public class Company {
private String companyName;
private String email;
private String CompanyId;
private String phoneNumberOne;
private  String PhoneNumberTwo;
private String confirmedPassWord;
private ArrayList<String>services;

    public Company(String companyName, String CompanyId, String email, String phoneNumberOne,
                   String getPhoneNumberTwo,String confirmedPassWord, ArrayList<String> services) {
        this.companyName = companyName;
        this.email = email;
        this.phoneNumberOne = phoneNumberOne;
        this.PhoneNumberTwo = getPhoneNumberTwo;
        this. confirmedPassWord= confirmedPassWord;
        this.CompanyId=CompanyId;
        this.services = services;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public String getPhoneNumberTwo() {
        return PhoneNumberTwo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumberOne() {
        return phoneNumberOne;
    }

    public String getGetPhoneNumberTwo() {
        return PhoneNumberTwo;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public String getConfirmedPassWord() {
        return confirmedPassWord;
    }
}
