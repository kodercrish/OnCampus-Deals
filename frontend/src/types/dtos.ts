import { Listing, ChatThread, Message, ListingImage } from './models';

// Identity
export interface LoginRequest { email: string; password: string; }
export interface LoginResponse { message: string; token?: string; userId?: string; role?: string; } // Added role for admin check
export interface SignupRequest { fullName: string; email: string; password: string; graduationYear?: number; }
export interface SignupResponse { message: string; token?: string; userId?: string; }

// Listing
export interface CreateListingRequest {
  title: string;
  description: string;
  price: number;
  category?: string;
  sellerId: string;
}
export interface CreateListingResponse { message: string; listingId?: string; }

export interface FetchListingRequest { listingId: string; }
export interface FetchListingResponse {
  message?: string;
  listing?: Listing;
  images?: ListingImage[];
}

export interface FetchSellerListingsRequest { sellerId: string; }
export interface FetchSellerListingsResponse { listings: Listing[]; }

export interface EditListingRequest {
  listingId: string; title?: string; description?: string; price?: number; category?: string;
}
export interface EditListingResponse { message: string; }

export interface DeleteListingRequest { listingId: string; }
export interface DeleteListingResponse { message: string; }

export interface AddImagesRequest { listingId: string; imageUrls: string[]; }
export interface AddImagesResponse { message: string; }

export interface DeleteImageRequest { imageId: string; }
export interface DeleteImageResponse { message: string; }

export interface MarkSoldRequest { listingId: string; }
export interface MarkUnavailableRequest { listingId: string; }
export interface MarkStatusResponse { message: string; }

// Chat
export interface CreateChatRequest { listingId: string; sellerId: string; } 
export interface CreateChatResponse { message?: string; chatId?: string; }

export interface FetchChatsRequest { userId: string; }
export interface FetchChatsResponse { chats: ChatThread[]; }

export interface SendMessageRequest { chatId: string; senderId: string; content: string; }
export interface SendMessageResponse { message: string; messageId?: string; }

export interface FetchMessagesRequest { chatId: string; afterTimestamp?: string; }
export interface FetchMessagesResponse { message?: string; messages: Message[]; }

// Search
export interface SearchRequest { keyword?: string; category?: string; minPrice?: number; maxPrice?: number; }
export interface SearchResponse { results: Listing[]; }