import { useState } from "react";
import { Alert, Button, StyleSheet, Text, View, TextInput} from "react-native";
import * as SecureStore from 'expo-secure-store';

function BadgerRegisterScreen({handleSignup, setIsRegistering, Nevermind}) {
    const [password, setPassword] = useState("");
    const [passwordVerify, setPasswordVerify] = useState("");
    const [username, setUsername] = useState("");
    

    const onSignupPress = () => {
        if (!username || !password || !passwordVerify) {
            Alert.alert('Error', 'All fields are required.');
            return;
        }
        handleSignup(username, password, passwordVerify);
    };


    return <View style={styles.container}>
        <Text style={{ fontSize: 36 }}>Join BadgerChat!</Text>

        <Text style={styles.text}>Username</Text>
        <TextInput autoCapitalize='none' style={styles.inputBox} placeholder="username"
        value={username}
        onChangeText={text => setUsername(text)}></TextInput>

        <Text style={styles.text}>Password</Text>
        <TextInput autoCapitalize='none' style={styles.inputBox} placeholder="password" secureTextEntry={true}
        value={password}
        onChangeText={text => setPassword(text)}></TextInput>

        <Text style={styles.text}>Confirm Password</Text>
        <TextInput autoCapitalize='none' style={styles.inputBox} secureTextEntry={true}
        value={passwordVerify}
        onChangeText={text => setPasswordVerify(text)}></TextInput>

        <Button color="crimson" title="Signup" onPress={onSignupPress} />
        <Button color="grey" title="Nevermind!" onPress={Nevermind} />
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
    signupBox:{
        borderBlockColor: 'crimson',

        
    }
});

export default BadgerRegisterScreen;