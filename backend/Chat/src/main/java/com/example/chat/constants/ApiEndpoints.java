package com.example.chat.constants;

public class ApiEndpoints {
    public static final String BASE_URL = "/api";

    public static final String CHAT_BASE = BASE_URL + "/chat";

    // Chat operations
    public static final String CREATE_CHAT = "/create";
    public static final String SEND_MESSAGE = "/send";
    public static final String FETCH_MESSAGES = "/fetch-messages";
    public static final String FETCH_USER_CHATS = "/my-chats";
    public static final String MARK_READ = "/mark-read";
}
