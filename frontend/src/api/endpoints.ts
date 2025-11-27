import { API_BASE } from '@env';

// export const AUTH_ENDPOINTS = {
//   LOGIN: `${API_BASE}/auth/login`,
//   SIGNUP: `${API_BASE}/auth/signup`,
// };
// // Centralized endpoints file
// export const API_BASE = 'http://localhost:8080/api'; // In real app, use import from @env

export const AUTH = {
  LOGIN: `/auth/login`,
  SIGNUP: `/auth/signup`,
};

export const LISTING = {
  CREATE: `/listing/create`,
  FEED: `/listing/feed`,
  FETCH: `/listing/fetch`, // expects ?listingId=
  FETCH_BY_SELLER: `/listing/fetch-by-seller`,
  EDIT: `/listing/edit`,
  DELETE: `/listing/delete`,
  ADD_IMAGES: `/listing/add-images`,
  MARK_SOLD: `/listing/mark-sold`,
  MARK_UNAVAILABLE: `/listing/mark-unavailable`,
};

export const CHAT = {
  CREATE: `/chat/create`,
  SEND: `/chat/send`,
  FETCH_MESSAGES: `/chat/fetch-messages`,
  MY_CHATS: `/chat/my-chats`,
};

export const SEARCH = {
  LISTINGS: `/search/listings`,
};

export const ADMIN = {
  FETCH_ALL_LISTINGS: `/admin/fetch-all-listings`,
  DELETE_LISTING: `/admin/delete-listing`,
};