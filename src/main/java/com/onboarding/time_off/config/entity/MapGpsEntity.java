package com.onboarding.time_off.config.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "mapGps",timeToLive = 500)//하루86400
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapGpsEntity {
    @Id
    private String id;

    private double mapX;
    private double mapY;
}
