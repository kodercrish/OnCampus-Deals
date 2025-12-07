// src/navigation/RootNavigator.tsx
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useSelector } from 'react-redux';
import { RootState } from '../store';
import { AuthNavigator } from './AuthNavigator';
import { TabNavigator } from './TabNavigation';
import { ListingDetailsScreen } from '../screens/Listing/ListingDetailsScreen';
import { ChatScreen } from '../screens/Chat/ChatScreen';
import { CreateListingScreen } from '../screens/Listing/CreateListingScreen';
import TestImageScreen from '../screens/Debug/TestImageScren'; // debug screen

const Stack = createNativeStackNavigator();

export const RootNavigator = () => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {!isAuthenticated ? (
          <Stack.Screen name="Auth" component={AuthNavigator} />
        ) : (
          <>
            <Stack.Screen name="Main" component={TabNavigator} />
            <Stack.Screen name="ListingDetails" component={ListingDetailsScreen} options={{ headerShown: true, title: 'Details' }} />
            <Stack.Screen name="ChatScreen" component={ChatScreen} options={{ headerShown: true }} />
            <Stack.Screen name="CreateListing" component={CreateListingScreen} options={{ headerShown: true, title: 'Sell Item' }} />
            <Stack.Screen name="TestImage" component={TestImageScreen} options={{ headerShown: true, title: 'Test Image' }} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};
