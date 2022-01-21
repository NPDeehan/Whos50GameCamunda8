package io.camunda.getstarted.whosfifty;


import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

import java.util.List;

public class CelebDetails {
    @JsonProperty("name")
    public String name;

    @JsonProperty("gender")
    public String gender;

    @JsonProperty("age")
    public int age;

    @JsonProperty("height")
    public double height;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("net_worth")
    public double net_worth;

    @JsonProperty("nationality")
    public String nationality;

    @JsonProperty("occupation")
    public List<String> occupation;
}
