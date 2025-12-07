// src/api/chatApi.ts
import { baseApi } from './baseApi';
import { CHAT } from './endpoints';

// Minimal types for frontend use
type CreateChatRequest = { user1Id: string; user2Id: string };
type CreateChatResponse = { message: string; chatId: string | null };

type SendMessageRequest = { chatId: string; senderId: string; content: string };
type SendMessageResponse = { message: string; messageId: string | null };

type FetchMessagesRequest = { chatId: string; afterTimestamp?: string | null };
export type Message = {
  id: string;
  chatId: string;
  senderId: string;
  content: string;
  isRead?: boolean;
  createdAt: string;
};
type FetchMessagesResponse = { message: string; messages: Message[] | null };

type FetchChatsRequest = { userId: string };
export type ChatThread = {
  id: string;
  participant1Id: string;
  participant2Id: string;
  createdAt: string;
};
type FetchChatsResponse = { chats: ChatThread[] | null };

export const chatApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    createChat: builder.mutation<CreateChatResponse, CreateChatRequest>({
      query: (body) => ({
        url: CHAT.CREATE,
        method: 'POST',
        body,
      }),
      invalidatesTags: (result, error, arg) => [
        { type: 'Chat' as const, id: `USER_${arg.user1Id}` },
        { type: 'Chat' as const, id: `USER_${arg.user2Id}` },
      ],
    }),

    sendMessage: builder.mutation<SendMessageResponse, SendMessageRequest>({
      query: (body) => ({ url: CHAT.SEND, method: 'POST', body }),
      invalidatesTags: (res, err, arg) => [
        { type: 'Message' as const, id: arg.chatId },
        { type: 'Chat' as const, id: `CHAT_${arg.chatId}` },
        // also invalidate user chat lists if needed (no arg here to get user ids)
      ],
    }),

    fetchMessages: builder.query<FetchMessagesResponse, FetchMessagesRequest>({
      query: (body) => ({ url: CHAT.FETCH_MESSAGES, method: 'POST', body }),
      providesTags: (result, error, arg) =>
        result?.messages
          ? [
              ...result.messages.map((m) => ({ type: 'Message' as const, id: m.id })),
              { type: 'Chat' as const, id: `CHAT_${arg.chatId}` },
            ]
          : [{ type: 'Chat' as const, id: `CHAT_${arg.chatId}` }],
    }),

    fetchMyChats: builder.query<FetchChatsResponse, FetchChatsRequest>({
      query: (body) => ({ url: CHAT.MY_CHATS, method: 'POST', body }),
      providesTags: (result, error, arg) =>
        result?.chats
          ? [
              { type: 'Chat' as const, id: `USER_${arg.userId}` },
              ...result.chats.map((c) => ({ type: 'Chat' as const, id: `CHAT_${c.id}` })),
            ]
          : [{ type: 'Chat' as const, id: `USER_${arg.userId}` }],
    }),
  }),
});

export const {
  useCreateChatMutation,
  useSendMessageMutation,
  useFetchMessagesQuery,
  useFetchMyChatsQuery,
} = chatApi;
