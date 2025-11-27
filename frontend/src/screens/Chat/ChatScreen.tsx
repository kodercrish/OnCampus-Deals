import React, { useState } from 'react';
import { View, FlatList, TextInput, StyleSheet, KeyboardAvoidingView, Platform } from 'react-native';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { useGetMessagesQuery, useSendMessageMutation } from '../../api/chatApi';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const ChatScreen = ({ route }: any) => {
  const { chatId } = route.params;
  const { userId } = useSelector((state: RootState) => state.auth);
  const [content, setContent] = useState('');

  // Polling every 3 seconds
  const { data } = useGetMessagesQuery(
    { chatId, afterTimestamp: undefined }, 
    { pollingInterval: 3000 }
  );
  
  const [sendMessage] = useSendMessageMutation();

  const handleSend = async () => {
    if (!content.trim() || !userId) return;
    try {
      await sendMessage({ chatId, senderId: userId, content }).unwrap();
      setContent('');
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <KeyboardAvoidingView 
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      style={styles.container}
    >
      <FlatList
        data={data?.messages || []}
        inverted // Chat usually starts from bottom
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={[
            styles.bubble, 
            item.senderId === userId ? styles.mine : styles.theirs
          ]}>
            <Text style={{ color: item.senderId === userId ? '#FFF' : '#000' }}>
              {item.content}
            </Text>
          </View>
        )}
      />
      <View style={styles.inputContainer}>
        <TextInput 
          style={styles.input} 
          value={content} 
          onChangeText={setContent}
          placeholder="Type a message..."
        />
        <Button title="Send" onPress={handleSend} />
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f9f9f9' },
  bubble: { padding: 10, margin: 5, borderRadius: 10, maxWidth: '80%' },
  mine: { alignSelf: 'flex-end', backgroundColor: '#007AFF' },
  theirs: { alignSelf: 'flex-start', backgroundColor: '#E5E5EA' },
  inputContainer: { flexDirection: 'row', padding: 10, backgroundColor: '#FFF' },
  input: { flex: 1, borderWidth: 1, borderColor: '#ccc', borderRadius: 20, paddingHorizontal: 15, marginRight: 10, height: 40 },
});