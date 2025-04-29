package com.laioffer.staybooking.model;


import java.util.List;


public record ListingDto(
        Long id,
        String name,
        String address,
        String description,
        Integer guestNumber,
        List<String> images,
        GeoPoint location,
        UserDto host
) {
    public ListingDto(ListingEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getDescription(),
                entity.getGuestNumber(),
                entity.getImageUrls(),
                // simplified the geometry data to only include latitude and longitude,
                new GeoPoint(entity.getLocation().getCoordinate().y, entity.getLocation().getCoordinate().x),
                // removed the password field from the host entity, and only return the username field
                new UserDto(entity.getHost())
        );
    }
}
