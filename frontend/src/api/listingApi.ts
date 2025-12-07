// src/api/listingApi.ts
import { baseApi } from './baseApi';
import { LISTING } from './endpoints';

// Use `any` for DTOs to avoid strict compile errors; swap to your typed DTOs if available.
export const listingApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getFeed: builder.query<any, void>({
      query: () => ({ url: LISTING.FEED }),
      providesTags: ['Listing'],
    }),

    getListingDetails: builder.query({
      query: ({ listingId }) => ({
        url: LISTING.FETCH,
        method: 'POST',
        body: { listingId },
      }),
    }),

    createListing: builder.mutation<any, any>({
      query: (body) => ({ url: LISTING.CREATE, method: 'POST', body }),
      invalidatesTags: ['Listing', 'UserListings'],
    }),

    addImages: builder.mutation<any, any>({
      query: (body) => ({ url: LISTING.ADD_IMAGES, method: 'POST', body }),
      invalidatesTags: (res, err, arg) => [{ type: 'Listing', id: arg?.listingId }],
    }),

    deleteListing: builder.mutation<any, any>({
      query: (body) => ({ url: LISTING.DELETE, method: 'POST', body }),
      invalidatesTags: ['Listing', 'UserListings'],
    }),

    getSellerListings: builder.query<any, { sellerId: string }>({
      query: ({ sellerId }) => ({ url: LISTING.FETCH_BY_SELLER, params: { sellerId } }),
      providesTags: ['UserListings'],
    }),

    markSold: builder.mutation<any, { listingId: string }>({
      query: (body) => ({ url: LISTING.MARK_SOLD, method: 'POST', body }),
      invalidatesTags: (r, e, arg) => [{ type: 'Listing', id: arg.listingId }],
    }),

    markUnavailable: builder.mutation<any, { listingId: string }>({
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
  useMarkUnavailableMutation,
} = listingApi;
