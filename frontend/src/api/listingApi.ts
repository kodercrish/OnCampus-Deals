import { baseApi } from './baseApi';
import { LISTING } from './endpoints';
import { 
  CreateListingRequest, CreateListingResponse, 
  FetchListingRequest, FetchListingResponse,
  FetchSellerListingsRequest, FetchSellerListingsResponse,
  DeleteListingRequest, DeleteListingResponse,
  AddImagesRequest, AddImagesResponse,
  MarkSoldRequest, MarkStatusResponse,
  MarkUnavailableRequest
} from '../types/dtos';

export const listingApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getFeed: builder.query<FetchListingResponse, void>({
      query: () => LISTING.FEED,
      providesTags: ['Listing'],
    }),
    getListingDetails: builder.query<FetchListingResponse, FetchListingRequest>({
      query: ({ listingId }) => ({
        url: LISTING.FETCH,
        params: { listingId },
      }),
      providesTags: (result, error, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),
    createListing: builder.mutation<CreateListingResponse, CreateListingRequest>({
      query: (body) => ({
        url: LISTING.CREATE,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Listing', 'UserListings'],
    }),
    addImages: builder.mutation<AddImagesResponse, AddImagesRequest>({
      query: (body) => ({
        url: LISTING.ADD_IMAGES,
        method: 'POST',
        body,
      }),
      invalidatesTags: (result, error, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),
    deleteListing: builder.mutation<DeleteListingResponse, DeleteListingRequest>({
      query: (body) => ({
        url: LISTING.DELETE,
        method: 'DELETE',
        body, // Some backends require 'data' for DELETE bodies, assuming standard here
      }),
      invalidatesTags: ['Listing', 'UserListings'],
    }),
    getSellerListings: builder.query<FetchSellerListingsResponse, FetchSellerListingsRequest>({
      query: ({ sellerId }) => ({
        url: LISTING.FETCH_BY_SELLER,
        params: { sellerId },
      }),
      providesTags: ['UserListings'],
    }),
    markSold: builder.mutation<MarkStatusResponse, MarkSoldRequest>({
      query: (body) => ({ url: LISTING.MARK_SOLD, method: 'POST', body }),
      invalidatesTags: (r, e, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),
    markUnavailable: builder.mutation<MarkStatusResponse, MarkUnavailableRequest>({
      query: (body) => ({ url: LISTING.MARK_UNAVAILABLE, method: 'POST', body }),
      invalidatesTags: (r, e, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),
  }),
});

export const { 
  useGetFeedQuery, 
  useGetListingDetailsQuery, 
  useCreateListingMutation, 
  useAddImagesMutation, 
  useDeleteListingMutation,
  useGetSellerListingsQuery,
  useMarkSoldMutation,
  useMarkUnavailableMutation
} = listingApi;