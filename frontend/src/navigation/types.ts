import { Listing } from '../types/models';

export type RootStackParamList = {
  Auth: undefined;
  Main: undefined;
  ListingDetails: { listingId: string };
  CreateListing: undefined;
  ChatScreen: { chatId: string; otherUserName: string };
};

export type AuthStackParamList = {
  Login: undefined;
  Signup: undefined;
};

export type TabParamList = {
  Feed: undefined;
  Search: undefined;
  Profile: undefined;
  MyChats: undefined;
};