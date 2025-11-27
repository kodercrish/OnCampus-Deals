import React from 'react';
import { View, StyleSheet } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { logout } from '../../store/authSlice';
import { Button } from '../../components/common/Button';
import { Text } from '../../components/common/Text';
import { useGetSellerListingsQuery } from '../../api/listingApi';

export const ProfileScreen = () => {
  const dispatch = useDispatch();
  const { userId, role } = useSelector((state: RootState) => state.auth);
  
  // Example usage: fetch own listings
  const { data } = useGetSellerListingsQuery({ sellerId: userId || '' }, { skip: !userId });

  return (
    <View style={styles.container}>
      <Text variant="h1">My Profile</Text>
      <Text>User ID: {userId}</Text>
      <Text>Role: {role}</Text>
      
      <View style={{ marginVertical: 20 }}>
        <Text variant="h2">My Active Listings: {data?.listings?.length || 0}</Text>
      </View>

      <Button title="Logout" onPress={() => dispatch(logout())} color="red" />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, alignItems: 'center' },
});