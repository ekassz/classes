import { Text, TouchableOpacity, StyleSheet } from "react-native";
import BadgerCard from "./BadgerCard"

function BadgerChatMessage(props) {

    const dt = new Date(props.created);

    return <BadgerCard style={{ marginTop: 16, padding: 8, marginLeft: 8, marginRight: 8 }}>
        <Text style={{fontSize: 28, fontWeight: 600}}>{props.title}</Text>
        <Text style={{fontSize: 12}}>by {props.poster} | Posted on {dt.toLocaleDateString()} at {dt.toLocaleTimeString()}</Text>
        <Text></Text>
        <Text>{props.content}</Text>
        {props.showDelete && (
        <TouchableOpacity
        style={styles.deleteButton}
        onPress={props.onDelete}> 
        <Text style={{color: 'white'}}>DELETE POST</Text></TouchableOpacity>
        )}
    </BadgerCard>
}
const styles = StyleSheet.create({
    deleteButton:{
        backgroundColor: 'red',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 10,
        borderRadius: 10,
    }
})
export default BadgerChatMessage;