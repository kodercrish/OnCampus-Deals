import { baseApi } from './baseApi';
import { CHAT } from './endpoints';
import { 
  CreateChatRequest, CreateChatResponse,
  FetchChatsRequest, FetchChatsResponse,
  FetchMessagesRequest, FetchMessagesResponse,
  SendMessageRequest, SendMessageResponse
} from '../types/dtos';

export const chatApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    createChat: builder.mutation<CreateChatResponse, CreateChatRequest>({
      query: (body) => ({
        url: CHAT.CREATE,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Chat'],
    }),
    getMyChats: builder.query<FetchChatsResponse, void>({
      query: () => CHAT.MY_CHATS,
      providesTags: ['Chat'],
    }),
    getMessages: builder.query<FetchMessagesResponse, FetchMessagesRequest>({
      query: ({ chatId, afterTimestamp }) => ({
        url: CHAT.FETCH_MESSAGES,
        params: { chatId, afterTimestamp },
      }),
      providesTags: ['Message'],
    }),
    sendMessage: builder.mutation<SendMessageResponse, SendMessageRequest>({
      query: (body) => ({
        url: CHAT.SEND,
        method: 'POST',
        body,
      }),
      invalidatesTags: ['Message', 'Chat'], // Refresh chat list (last message) and messages
    }),
  }),
});

export const { 
  useCreateChatMutation, 
  useGetMyChatsQuery, 
  useGetMessagesQuery, 
  useSendMessageMutation 
} = chatApi;