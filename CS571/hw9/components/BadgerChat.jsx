import { useEffect, useState } from 'react';
import { createDrawerNavigator } from '@react-navigation/drawer';
import {createStackNavigator} from '@react-navigation/stack';
import { NavigationContainer } from '@react-navigation/native';
import { Alert } from 'react-native';
import CS571 from '@cs571/mobile-client'
import * as SecureStore from 'expo-secure-store';
import BadgerChatroomScreen from './screens/BadgerChatroomScreen';
import BadgerRegisterScreen from './screens/BadgerRegisterScreen';
import BadgerLoginScreen from './screens/BadgerLoginScreen';
import BadgerLandingScreen from './screens/BadgerLandingScreen';
import BadgerLogoutScreen from './screens/BadgerLogoutScreen';
import BadgerConversionScreen from './screens/BadgerConversionScreen';


const ChatDrawer = createDrawerNavigator();
const ChatStack = createStackNavigator();
export default function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isRegistering, setIsRegistering] = useState(false);
  const [chatrooms, setChatrooms] = useState([]);
  const [guest, setGuest] = useState(false);

  useEffect(() => {
    // hmm... maybe I should load the chatroom names here
    fetch('https://cs571.org/api/s24/hw9/chatrooms',{
      headers:{
        "X-CS571-ID": "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce",
        "Content-Type": "application/json"
      }
    }).then(res => res.json())
    .then(data => setChatrooms(data))
  }, []);

  async function handleLogin(username, password) {
    const response = await fetch('https://cs571.org/api/s24/hw9/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce', 
        },
        body: JSON.stringify({ username, password })
    });
    const data = await response.json();

    if (response.ok) {
        await SecureStore.setItemAsync('jwtToken', data.token);
        await SecureStore.setItemAsync('userId', data.user.id.toString());
        setIsLoggedIn(true);
    } else {
        Alert.alert('Login Failed', data.msg || 'Please check your credentials.');
        setIsLoggedIn(false);
    }
}

  async function handleSignup(username, password, passwordVerify) {
    if (password !== passwordVerify) {
        Alert.alert('Passwords do not match');
        return;
    }

    const response = await fetch('https://cs571.org/api/s24/hw9/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce',
        },
        body: JSON.stringify({ username, password })
    });
    const data = await response.json();
    console.log(data);

    if (response.ok) {
        await SecureStore.setItemAsync('jwtToken', data.token);  
        setIsLoggedIn(true);  // Update the login state
    } else {
      console.error(data);
        Alert.alert('Registration Failed', data.msg);
    }
}

  const handleLogout = async () => {
        await SecureStore.deleteItemAsync('jwtToken');
        setIsLoggedIn(false);
        setIsRegistering(false);
        setGuest(false);
        
    };

  const Nevermind = async () => {
    setIsLoggedIn(false);
    setIsRegistering(false);
    setGuest(false);
  }
  
  const handleGuest = () => {
    setGuest(true);
    setIsLoggedIn(false);

  }
  if (isLoggedIn) {
    return (
      <NavigationContainer>
        <ChatDrawer.Navigator>
          <ChatDrawer.Screen name="Landing" component={BadgerLandingScreen} />
          {
            chatrooms.map(chatroom => {
              return <ChatDrawer.Screen key={chatroom} name={chatroom}>
                {(props) => <BadgerChatroomScreen name={chatroom} />}
              </ChatDrawer.Screen>
            })
          }
          <ChatDrawer.Screen name = "Logout">
            {() => <BadgerLogoutScreen handleLogout={handleLogout}/>}</ChatDrawer.Screen>
        </ChatDrawer.Navigator>
      </NavigationContainer>
    );
  } 
  if (guest) {
    return (
        <NavigationContainer>
            <ChatDrawer.Navigator>
                <ChatDrawer.Screen name="Landing" component={BadgerLandingScreen} />
                {chatrooms.map(chatroom => (
                    <ChatDrawer.Screen key={chatroom} name={chatroom}>
                        {(props) => <BadgerChatroomScreen name={chatroom} isGuest={true} />}
                    </ChatDrawer.Screen>
                ))}
                <ChatDrawer.Screen name="Signup" component={BadgerConversionScreen} />
                <ChatDrawer.Screen 
          name='Register'
          options={{
            drawerLabel: () => null,
            drawerIcon: () => null,
            unmountOnBlur: true,
            drawerItemStyle: {height:0}
          }}
          >{() => <BadgerRegisterScreen handleSignup={handleSignup} setIsRegistering={setIsRegistering} Nevermind={Nevermind}/>}
          </ChatDrawer.Screen>
            </ChatDrawer.Navigator>
        </NavigationContainer>
    );
}

  
  else if (isRegistering) {
    return <BadgerRegisterScreen handleSignup={handleSignup} setIsRegistering={setIsRegistering} Nevermind={Nevermind}/>
    

  } else {
    return <BadgerLoginScreen handleLogin={handleLogin} setIsRegistering={setIsRegistering} handleGuest={handleGuest} />
  }
}