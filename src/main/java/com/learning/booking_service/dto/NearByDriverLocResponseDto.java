package com.learning.booking_service.dto;

import lombok.*;
import org.springframework.data.geo.Distance;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearByDriverLocResponseDto {
    String driverId;
    Double latitude;
    Double longitude;
//    Distance distance;
}