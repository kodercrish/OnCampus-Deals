import React, { useState } from 'react';
import { View, ScrollView, TextInput, StyleSheet, Alert, Image } from 'react-native';
import * as ImagePicker from 'expo-image-picker';
import { useForm, Controller } from 'react-hook-form';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { useCreateListingMutation } from '../../api/listingApi';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';
import { API_BASE } from '@env'; // e.g. http://10.0.2.2:8082/api
import { LISTING } from '../../api/endpoints';

export const CreateListingScreen = ({ navigation }: any) => {
  const { userId, token } = useSelector((state: RootState) => state.auth);
  const { control, handleSubmit, reset } = useForm();
  const [images, setImages] = useState<string[]>([]); // local URIs for preview
  const [createListing] = useCreateListingMutation();

  /** Pick a single image and add to list */
  const pickImage = async () => {
    try {
      const result = await ImagePicker.launchImageLibraryAsync({
        // deprecation warning is fine; you can change to ImagePicker.MediaType if you want
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: false,
        quality: 0.8,
      });

      if (!result.canceled) {
        const uri = result.assets[0].uri;
        setImages(prev => [...prev, uri]);
        console.log('picked uri:', uri);
      }
    } catch (err) {
      console.error('image pick error', err);
      Alert.alert('Error', 'Could not pick image');
    }
  };

  /**
   * Submit handler — sends a multipart request:
   * - data (stringified JSON) as text part
   * - images[] as file parts (React Native-style file objects)
   *
   * Includes Authorization header (Bearer token) read from Redux.
   */
  const onSubmit = async (data: any) => {
    if (!userId) {
      Alert.alert('Error', 'You must be logged in to post a listing');
      return;
    }

    try {
      const createReq = {
        title: data.title || '',
        description: data.description || '',
        price: parseFloat(data.price || '0'),
        category: data.category || '',
        sellerId: userId,
      };

      // Build FormData
      const formData = new FormData();
      // Append JSON as plain string (no Blob)
      formData.append('data', JSON.stringify(createReq));

      // Append images as "file" objects recognized by RN / Expo
      for (let i = 0; i < images.length; i++) {
        const uri = images[i];
        const uriParts = uri.split('.');
        const rawExt = uriParts.length > 1 ? uriParts[uriParts.length - 1] : 'jpg';
        const ext = rawExt.split('?')[0].toLowerCase();
        const mime = ext === 'jpg' ? 'image/jpeg' : `image/${ext}`;
        const filename = `image_${Date.now()}_${i}.${ext}`;

        formData.append('images', {
          uri,
          name: filename,
          type: mime,
        } as any);
      }

      // Endpoint: API_BASE + LISTING.CREATE (e.g. http://10.0.2.2:8082/api + /listing/create)
      const endpoint = `${API_BASE}${LISTING.CREATE}`;

      const resp = await fetch(endpoint, {
        method: 'POST',
        headers: {
          // Send JWT so backend auth filter picks it up
          Authorization: token ? `Bearer ${token}` : '',
          // DON'T set Content-Type; leave boundary to be set automatically
        },
        body: formData,
      });

      if (!resp.ok) {
        const text = await resp.text().catch(() => null);
        console.error('create listing failed', resp.status, text);
        Alert.alert('Error', `Failed to create listing (${resp.status})`);
        return;
      }

      const json = await resp.json().catch(() => null);
      console.log('create listing succeeded', json);
      Alert.alert('Success', 'Listing created!');
      reset();
      setImages([]);
      navigation.goBack();
    } catch (err) {
      console.error('upload error', err);
      Alert.alert('Error', 'Failed to create listing (client error)');
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Controller name="title" control={control} render={({ field: { onChange, value } }) => (
        <TextInput style={styles.input} placeholder="Title" onChangeText={onChange} value={value} />
      )} />

      <Controller name="description" control={control} render={({ field: { onChange, value } }) => (
        <TextInput style={[styles.input, { height: 100 }]} placeholder="Description" multiline onChangeText={onChange} value={value} />
      )} />

      <Controller name="price" control={control} render={({ field: { onChange, value } }) => (
        <TextInput style={styles.input} placeholder="Price" keyboardType="numeric" onChangeText={onChange} value={value} />
      )} />

      <Controller name="category" control={control} render={({ field: { onChange, value } }) => (
        <TextInput style={styles.input} placeholder="Category" onChangeText={onChange} value={value} />
      )} />

      <Button title="Select Images" onPress={pickImage} color="#6c757d" />
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', marginVertical: 8 }}>
        {images.map((uri, idx) => (
          <Image key={idx} source={{ uri }} style={{ width: 60, height: 60, margin: 2 }} />
        ))}
      </View>

      <Button title="Post Listing" onPress={handleSubmit(onSubmit)} />
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { padding: 20 },
  input: { borderWidth: 1, borderColor: '#ccc', borderRadius: 8, padding: 10, marginBottom: 15, backgroundColor: '#FFF' },
});
