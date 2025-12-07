// src/screens/Chat/ChatListScreen.tsx
import React from 'react';
import { View, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useFetchMyChatsQuery } from '../../api/chatApi';
import { Text } from '../../components/common/Text';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

export const ChatListScreen = ({ navigation }: any) => {
  const { userId } = useSelector((s: RootState) => s.auth);
  const { data, isLoading, error } = useFetchMyChatsQuery({ userId }, { skip: !userId });

  const chats = data?.chats ?? [];

  const renderItem = ({ item }: any) => {
    const otherId = item.participant1Id === userId ? item.participant2Id : item.participant1Id;

    return (
      <TouchableOpacity
        style={styles.row}
        onPress={() =>
          navigation.navigate('ChatScreen', {
            chatId: item.id,
            otherUserId: otherId,
            otherUserName: otherId, // you can replace by fetching user later
          })
        }
      >
        <View>
          <Text variant="h3">{otherId}</Text>
          <Text variant="caption">{new Date(item.createdAt).toLocaleString()}</Text>
        </View>
      </TouchableOpacity>
    );
  };

  if (isLoading) {
    return (
      <View style={styles.center}>
        <Text>Loading chats…</Text>
      </View>
    );
  }

  return (
    <FlatList
      data={chats}
      keyExtractor={(c: any) => c.id}
      renderItem={renderItem}
      contentContainerStyle={{ padding: 12 }}
      ListEmptyComponent={
        <View style={styles.center}>
          <Text>No chats yet.</Text>
        </View>
      }
    />
  );
};

const styles = StyleSheet.create({
  row: { padding: 12, borderBottomWidth: 1, borderBottomColor: '#eee' },
  center: { padding: 20, alignItems: 'center' },
});
