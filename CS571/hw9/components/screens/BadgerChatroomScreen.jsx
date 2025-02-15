import { useEffect, useState } from "react";
import { StyleSheet, Text, View, FlatList, Pressable, TouchableOpacity, Modal, Alert, TextInput} from "react-native";
import BadgerChatMessage from "../helper/BadgerChatMessage";
import * as SecureStore from 'expo-secure-store';

function BadgerChatroomScreen(props) {
    const [isLoading, setIsLoading] = useState(false);
    const [messages, setMessages] = useState([]);
    const [modalVisible, setModalVisible] = useState(false);
    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const [currentUserId, setCurrentUserId] = useState(null);

    
    function refresh(){
        setIsLoading(true);
        fetch(`https://cs571.org/api/s24/hw9/messages?chatroom=${encodeURIComponent(props.name)}`,{
            method: "GET",
            headers:{
                "X-CS571-ID": "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce",
                "Content-Type": "application/json"
            }
        }).then(res => res.json())
        .then(data => { 
            //console.log(data)
            setMessages(data.messages);
            setIsLoading(false);
            //setModalVisible(false);

        })
    }
    useEffect(() => {
        const getUserDetails = async () => {
        const token = await SecureStore.getItemAsync('jwtToken');
        if (token) {
            const response = await fetch('https://cs571.org/api/s24/hw9/whoami', {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                    "X-CS571-ID": "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce"
                }
            });
            const data = await response.json();
            if (response.ok) {
                setCurrentUserId(data.user.username);
            }
        }
    };
    getUserDetails();
        refresh();
    }, [props.name])

    async function handleCreatePost(){
        const token = await SecureStore.getItemAsync('jwtToken');
        //console.log("token", token);
        if(!title || !body){
            Alert.alert("Both title & body are required.");
        return;
        }

        if (!token) {
        Alert.alert("Authentication Error", "You must be logged in to post messages.");
        return;
    }
        fetch(`https://cs571.org/api/s24/hw9/messages?chatroom=${encodeURIComponent(props.name)}`,{
            method: "POST",
            headers:{
                "X-CS571-ID": "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce",
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
                },
            body: JSON.stringify({title, content: body})
            
        })
        .then(response => response.json())
        .then(data => {
            //console.log("Post response:", data); 
            if (data && data.msg === 'Successfully posted message!') {  
                Alert.alert("Success", data.msg);
                setTitle('');
                setBody('');
                setModalVisible(false);
                refresh();
            } else {
                Alert.alert("Error", "Failed to post message.");
            }
        })

    }

    const handleDelete = async (postId) => {
        const token = await SecureStore.getItemAsync('jwtToken');
        const response = await fetch(`https://cs571.org/api/s24/hw9/messages?id=${postId}`,{
            method: 'DELETE',
            headers:{
               "X-CS571-ID": "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce",
                "Content-Type": "application/json", 
                "Authorization": `Bearer ${token}`,
            }


        })
        if(response.ok){
            Alert.alert("Successfully deleted post!");
            refresh();
        }else{
            Alert.alert('Failed to delete post');
        }
    }

    

    return <View style={{ flex: 1 }}>
        <FlatList
            data = {messages}
            keyExtractor={item => item.id.toString()}
            
            renderItem={({item}) => {
                //console.log(`Rendering item with id: ${item.id}, poster: ${item.poster}, currentUserId: ${currentUserId}` )
                const showDelete = currentUserId && item.poster && currentUserId.trim() === item.poster.trim();
                //console.log(`Show delete for item ${item.id}: ${showDelete}`);
                //console.log(`Rendering item with id: ${item.id}, poster: ${item.poster}, currentUserId: ${currentUserId}` );

                return(
                <BadgerChatMessage
                title = {item.title}
                poster = {item.poster}
                created = {item.created}
                content = {item.content}
                onDelete = {() => handleDelete(item.id)}
                showDelete = {showDelete}

                ></BadgerChatMessage> )
            }}
            onRefresh={refresh}
            refreshing = {isLoading}
            >
            
        </FlatList>
        <Modal
        animationType="slide"
        transparent = {true}
        visible = {modalVisible}
        onRequestClose={() =>{
            setModalVisible(!modalVisible);
        }}
        >
            <View style={styles.modalCenter}>
                <View style={styles.modalBox}>
                    <Text style={styles.titleStyle}>Create A Post</Text>
                    <Text style={styles.textStyle}> Title </Text>
                    <TextInput value={title} style={styles.inputTitle}
                    onChangeText={setTitle}></TextInput>
                    <Text style= {styles.textStyle}> Body </Text>
                    <TextInput value={body} style={styles.inputBody}
                    onChangeText={setBody}></TextInput>

                    <Pressable  style={styles.button} onPress={handleCreatePost} disabled={!title || !body}>
                        <Text>Create Post</Text>
                    </Pressable>

                    <Pressable style={styles.button}
                    onPress={() => setModalVisible(false)}>
                        <Text>Cancel</Text>
                    </Pressable>

                </View>
            </View>
        </Modal>
        
        <TouchableOpacity
        style={styles.addButton}
        onPress={() => {
            if(props.isGuest){
                Alert.alert('Guests cannot post, please Sign Up!');
            }else{
            setModalVisible(true)} } } 
            >
            <Text style={ {color: 'white'}} > ADD POST </Text>
        </TouchableOpacity>
    </View>
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
    modalBox:{
        backgroundColor: 'white',
        padding: 100,
        alignItems: 'flex-start',
        margin: 20,
        shadowColor: 'gray',
        shadowOpacity: 2,
        borderRadius: 10, 
    },
    modalCenter: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 22,
    },
    addButton: {
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 10,
        backgroundColor: '#8b0000',

    },
    inputTitle:{
        borderColor: 'black',
        width: 250,
        borderWidth: 1,
        borderRadius: 5,
        padding: 10,
        marginTop: 10,
        marginVertical: 10,
    },
    inputBody:{
        borderColor: 'black',
        width: 250,
        height: 100,
        borderWidth: 1,
        borderRadius: 5,
        marginTop: 10,
    },
    textStyle:{
        marginTop: 5,
    },
    titleStyle:{
        fontSize: 20,
        marginBottom: 5,
    },
    button:{
        marginTop: 10,
        backgroundColor: '#2196F3',
        padding: 10,
        borderRadius: 5,
        flexDirection: "row"
    }
});

export default BadgerChatroomScreen;