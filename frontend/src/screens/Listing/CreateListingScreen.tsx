import React, { useState } from 'react';
import { View, ScrollView, TextInput, StyleSheet, Alert, Image } from 'react-native';
import * as ImagePicker from 'expo-image-picker';
import { useForm, Controller } from 'react-hook-form';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { useCreateListingMutation, useAddImagesMutation } from '../../api/listingApi';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';

export const CreateListingScreen = ({ navigation }: any) => {
  const { userId } = useSelector((state: RootState) => state.auth);
  const { control, handleSubmit } = useForm();
  const [createListing] = useCreateListingMutation();
  const [addImages] = useAddImagesMutation();
  const [images, setImages] = useState<string[]>([]);

  const pickImage = async () => {
    // No permissions request is necessary for launching the image library
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });
  
    if (!result.canceled) {
      console.log(result.assets[0].uri);
    }
  };

  const onSubmit = async (data: any) => {
    if (!userId) return;
    try {
      // 1. Create Listing
      const req = { ...data, price: parseFloat(data.price), sellerId: userId };
      const res = await createListing(req).unwrap();
      
      // 2. Add Images (Mocking upload by sending URIs directly as if they were URLs)
      if (res.listingId && images.length > 0) {
        await addImages({ listingId: res.listingId, imageUrls: images }).unwrap();
      }

      Alert.alert('Success', 'Listing created!');
      navigation.goBack();
    } catch (e) {
      Alert.alert('Error', 'Failed to create listing');
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
      <View style={{ flexDirection: 'row', flexWrap: 'wrap' }}>
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