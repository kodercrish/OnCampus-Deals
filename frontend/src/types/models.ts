// inferred models based on context
export interface User {
    id: string;
    fullName: string;
    email: string;
  }
  
  export interface ListingImage {
    id: string;
    url: string;
  }
  
  export interface Listing {
    id: string;
    title: string;
    description: string;
    price: number;
    category: string;
    sellerId: string;
    sellerName?: string; // Inferred for UI convenience
    status: 'AVAILABLE' | 'SOLD' | 'UNAVAILABLE';
    images: ListingImage[];
    createdAt: string;
  }
  
  export interface Message {
    id: string;
    chatId: string;
    senderId: string;
    content: string;
    timestamp: string;
  }
  
  export interface ChatThread {
    id: string;
    listingId: string;
    listingTitle: string;
    otherParticipantName: string;
    lastMessage?: string;
    updatedAt: string;
  }