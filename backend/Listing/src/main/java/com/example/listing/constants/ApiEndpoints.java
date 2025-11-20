package com.example.listing.constants;

public class ApiEndpoints {

    public static final String BASE_URL = "/api";

    // Listing base
    public static final String LISTING_BASE = BASE_URL + "/listing";

    // Listing operations
    public static final String CREATE_LISTING = "/create";
    public static final String EDIT_LISTING = "/edit";
    public static final String DELETE_LISTING = "/delete";
    public static final String FETCH_LISTING = "/fetch";
    public static final String FETCH_SELLER_LISTINGS = "/fetch-by-seller";
    public static final String FETCH_ACTIVE_LISTINGS = "/feed";

    // Status change
    public static final String MARK_SOLD = "/mark-sold";
    public static final String MARK_UNAVAILABLE = "/mark-unavailable";

    // Image operations
    public static final String ADD_IMAGES = "/add-images";
    public static final String DELETE_IMAGE = "/delete-image";
}
