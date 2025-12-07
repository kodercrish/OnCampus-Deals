// src/screens/Chat/ChatScreen.tsx
import React, { useState, useEffect, useCallback } from 'react';
import {
  View,
  StyleSheet,
  FlatList,
  TextInput,
  TouchableOpacity,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { Text } from '../../components/common/Text';
import { useFetchMessagesQuery, useSendMessageMutation, Message } from '../../api/chatApi';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

export const ChatScreen = ({ route, navigation }: any) => {
  const { chatId, otherUserName, otherUserId } = route.params ?? {};
  const { userId } = useSelector((s: RootState) => s.auth);

  // fetch messages for this chat
  const { data, isLoading, refetch } = useFetchMessagesQuery({ chatId }, { skip: !chatId });
  const messages: Message[] = data?.messages ?? [];

  const [text, setText] = useState('');
  const [sendMessage] = useSendMessageMutation();

  useEffect(() => {
    if (otherUserName) navigation.setOptions({ title: otherUserName });
  }, [otherUserName]);

  const onSend = useCallback(async () => {
    if (!text?.trim()) return;
    try {
      await sendMessage({ chatId, senderId: userId!, content: text.trim() }).unwrap();
      setText('');
      // refetch messages immediately
      refetch();
    } catch (e) {
      console.warn('send message error', e);
    }
  }, [text, chatId, userId, sendMessage, refetch]);

  const renderItem = ({ item }: { item: Message }) => {
    const mine = item.senderId === userId;
    return (
      <View style={[styles.msgRow, mine ? styles.myMsg : styles.theirMsg]}>
        <Text variant="body">{item.content}</Text>
        <Text variant="caption" style={{ marginTop: 6 }}>
          {new Date(item.createdAt).toLocaleString()}
        </Text>
      </View>
    );
  };

  return (
    <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <View style={styles.list}>
        {isLoading ? (
          <View style={styles.center}>
            <Text>Loading messages…</Text>
          </View>
        ) : (
          <FlatList
            data={messages}
            keyExtractor={(m) => m.id}
            renderItem={renderItem}
            contentContainerStyle={{ padding: 12 }}
            inverted={false}
          />
        )}
      </View>

      <View style={styles.composer}>
        <TextInput
          value={text}
          onChangeText={setText}
          placeholder="Type a message"
          style={styles.input}
        />
        <TouchableOpacity onPress={onSend} style={styles.sendBtn}>
          <Text variant="h3">Send</Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  list: { flex: 1 },
  center: { padding: 20, alignItems: 'center' },
  composer: {
    flexDirection: 'row',
    padding: 8,
    borderTopWidth: 1,
    borderTopColor: '#eee',
    backgroundColor: '#fff',
    alignItems: 'center',
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: '#ddd',
    padding: 10,
    borderRadius: 8,
    marginRight: 8,
  },
  sendBtn: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    backgroundColor: '#2ecc71',
    borderRadius: 8,
  },
  msgRow: {
    padding: 10,
    borderRadius: 8,
    marginBottom: 8,
    maxWidth: '80%',
  },
  myMsg: {
    backgroundColor: '#dcf8c6',
    alignSelf: 'flex-end',
  },
  theirMsg: {
    backgroundColor: '#f0f0f0',
    alignSelf: 'flex-start',
  },
});
