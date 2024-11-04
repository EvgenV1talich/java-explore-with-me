package ru.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * DTO for Hit
 */

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class HitDto implements Serializable {
    Integer id;
    String app;
    String uri;
    InetAddress ip;
    LocalDateTime timestamp;
}
