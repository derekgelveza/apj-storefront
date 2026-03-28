package edu.byui.apj.storefront.web.model;

/**
 * JSON returned by GET /api/me/profile (read-only name and zip for this milestone).
 */
public record UserProfileDto(String name, String zipCode) {
}