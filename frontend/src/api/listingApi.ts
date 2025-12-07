// src/api/listingApi.ts
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
    // feed -> backend returns { listings: Listing[] }
    getFeed: builder.query<FetchSellerListingsResponse, void>({
      query: () => ({
        url: LISTING.FETCH_ACTIVE_LISTINGS,
        method: 'GET',
      }),
      providesTags: ['Listing'],
    }),

    // fetch single listing (POST with { listingId })
    getListingDetails: builder.query<FetchListingResponse, FetchListingRequest>({
      query: (body) => ({
        url: LISTING.FETCH_LISTING,
        method: 'POST',
        body, // { listingId }
      }),
      providesTags: (result, error, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),

    createListing: builder.mutation<CreateListingResponse, FormData | CreateListingRequest>({
      query: (body) => ({
        url: LISTING.CREATE_LISTING,
        method: 'POST',
        // allow either FormData (multipart upload) or JSON body
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
        url: LISTING.DELETE_LISTING,
        method: 'POST', // backend expects POST for delete in your controller
        body,
      }),
      invalidatesTags: ['Listing', 'UserListings'],
    }),

    getSellerListings: builder.query<FetchSellerListingsResponse, FetchSellerListingsRequest>({
      query: (body) => ({
        url: LISTING.FETCH_SELLER_LISTINGS,
        method: 'POST',
        body,
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
