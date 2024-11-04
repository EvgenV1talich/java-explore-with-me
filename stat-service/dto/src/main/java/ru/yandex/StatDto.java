package ru.yandex;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatDto {

    private String app;
    private String uri;
    private Integer hits;

}
