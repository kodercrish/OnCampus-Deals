import React from 'react';
import { View, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useGetMyChatsQuery } from '../../api/chatApi';
import { Text } from '../../components/common/Text';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

export const ChatListScreen = ({ navigation }: any) => {
  const { userId } = useSelector((state: RootState) => state.auth);
  // Need to pass a dummy arg if FetchChatsRequest requires userId, 
  // though typically it's inferred from token in headers. 
  // DTO says FetchChatsRequest { userId: string }, but usually endpoints like /my-chats don't need params.
  // Assuming endpoint ignores body if GET, or we pass null.
  // SRS endpoint constant: MY_CHATS is a GET.
  const { data, isLoading } = useGetMyChatsQuery();

  if (isLoading) return <Text>Loading chats...</Text>;

  return (
    <FlatList
      data={data?.chats || []}
      keyExtractor={(item) => item.id}
      renderItem={({ item }) => (
        <TouchableOpacity 
          style={styles.item}
          onPress={() => navigation.navigate('ChatScreen', { 
            chatId: item.id, 
            otherUserName: item.otherParticipantName 
          })}
        >
          <Text variant="h2">{item.otherParticipantName}</Text>
          <Text variant="caption">{item.listingTitle}</Text>
          <Text numberOfLines={1}>{item.lastMessage}</Text>
        </TouchableOpacity>
      )}
    />
  );
};

const styles = StyleSheet.create({
  item: { padding: 15, borderBottomWidth: 1, borderBottomColor: '#eee', backgroundColor: '#FFF' },
});