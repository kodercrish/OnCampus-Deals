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
            <Stack.Screen 
              name="ListingDetails" 
              component={ListingDetailsScreen} 
              options={{ headerShown: true, title: 'Details' }}
            />
            <Stack.Screen 
              name="ChatScreen" 
              component={ChatScreen} 
              options={({ route }: any) => ({ 
                headerShown: true, 
                title: route.params.otherUserName 
              })}
            />
            <Stack.Screen 
              name="CreateListing" 
              component={CreateListingScreen} 
              options={{ headerShown: true, title: 'Sell Item' }}
            />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};