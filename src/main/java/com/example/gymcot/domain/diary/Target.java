package com.example.gymcot.domain.diary;

import lombok.Getter;

@Getter
public enum Target {
    AEROBIC("유산소 운동"),
    BACK("등 운동"),
    SHOULDER("어깨 운동"),
    ARM("이두근 및 삼두근 운동"),
    CORE("코어 운동"),
    LOWER("하체 운동");

    private String name;

    Target(String name) {
        this.name = name;
    }
}
