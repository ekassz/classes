import { Alert, Button, StyleSheet, Text, View, TextInput } from "react-native";
import React, {useState} from "react";
import * as SecureStore from 'expo-secure-store';

function BadgerLoginScreen({handleLogin, setIsRegistering, handleGuest}) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const onLoginPress = () => {
        if (!username || !password) {
            Alert.alert('Validation Error', 'Username and password are required.');
            return;
        }
        handleLogin(username, password);
    };

    return <View style={styles.container}>
        <Text style={{ fontSize: 36 }}>BadgerChat Login</Text>
        <Text style={styles.text}>Username</Text>
        <TextInput autoCapitalize='none' style={styles.inputBox} placeholder="username" 
        onChangeText={setUsername}
        ></TextInput>

        <Text style={styles.text}>Password</Text>
        <TextInput autoCapitalize='none' style={styles.inputBox} placeholder="password" secureTextEntry={true}
        onChangeText={setPassword}></TextInput>

        <Button color="crimson" title="Login" onPress={onLoginPress} />
        <Button color="grey" title="Signup" onPress={() => setIsRegistering(true)} />
        <Button color="grey" title="Guest" onPress={handleGuest}/>
        </View>;
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    text:{
        fontSize: 18,
        marginTop: 20,
    },
    inputBox:{
        borderColor: 'black',
        width: '65%',
        borderWidth: 1,
        borderRadius: 5,
        padding: 9,
        marginTop: 10,
    },
});

export default BadgerLoginScreen;