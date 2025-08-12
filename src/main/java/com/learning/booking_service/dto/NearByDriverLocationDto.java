package com.learning.booking_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearByDriverLocationDto {
    Double latitude;
    Double longitude;
}
