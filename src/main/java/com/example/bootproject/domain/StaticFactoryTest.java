package com.example.bootproject.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StaticFactoryTest extends BaseEntity {

    private String name;
    private int age;
    private String email;

    private StaticFactoryTest(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public static StaticFactoryTest staticFactoryTest(String name, int age, String email) {
        return new StaticFactoryTest(name, age, email);
    }

    public static StaticFactoryTest staticFactoryTest(String name, int age) {
        return new StaticFactoryTest(name, age);
    }
}
