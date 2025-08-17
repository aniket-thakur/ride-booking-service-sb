package com.learning.booking_service.dto;

import com.learning.entityService.models.Location;
import jakarta.annotation.security.DenyAll;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {
    private Long id;
    private String name;
    private String license_number;
    private int age;
    private String mobile_number;
    private double rating;
}
