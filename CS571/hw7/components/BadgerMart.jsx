import { Button, Text, View , Pressable, StyleSheet, Alert} from "react-native";
import BadgerSaleItem from "./BadgerSaleItem";

import CS571 from '@cs571/mobile-client'
import { useEffect, useState } from "react";



export default function BadgerMart(props) {
    const [items, setItems] = useState([]);
    const [currIndex, setCurrIndex] = useState(0);
    const [cart, setCart] = useState({});
    
    useEffect(() => {
        fetch("https://cs571.org/api/s24/hw7/items",{
            headers:{
                "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
            }
        })
        .then(res => res.json())
        .then(data => setItems(data))
    }, []);
    
    const nextItem = () => {
        if(currIndex < items.length -1){
            setCurrIndex(currIndex +1);
           // setCount(0);
        }
    }
    const prevItem = () => {
        if(currIndex > 0){
            setCurrIndex(currIndex -1);
          //  setCount(0);
        }
    }
    
    const modifyItemCount = (increment) => { 
        const item = items[currIndex];
        const itemName = item.name;
        const currentCount = cart[itemName] || 0;    
        
        if (increment) {
            const updatedCount = currentCount + 1 <= item.upperLimit ? currentCount + 1 : currentCount;
            setCart(prevCart => ({ ...prevCart, [itemName]: updatedCount }));
        } else {
            const updatedCount = Math.max(currentCount - 1, 0);
            setCart(prevCart => ({ ...prevCart, [itemName]: updatedCount }));
        }
    };

    const countAdd = () => modifyItemCount(true);

    const countSubtract = () => modifyItemCount(false);

    const getTotalItemsCount = () => {
        return Object.values(cart).reduce((total, count) => total + count, 0);
    }

    const total = () => {
        return Object.entries(cart).reduce((total, [itemName, quantity]) => {
            const item = items.find(item => item.name === itemName);
            return total + (item ? item.price * quantity : 0);
        }, 0).toFixed(2);
    }

    const placeOrder = () => {
        Alert.alert(
            'Order Confirmed!', //title
            `Your order contains ${getTotalItemsCount()} items and would have cost $${total()}!`,
            [
                {text: "OK"}
            ]
        )

    }

    const item = items[currIndex];
    const itemName = item?.name;
    const currentCount = cart[itemName] || 0;    
    
    return <View style= {{justifyContent: 'center'}}>    
        <Text style={{fontSize: 28}}>Welcome to Badger Mart!</Text>{
            items.length > 0 && items[currIndex] ? (
                <>
                <View style={{justifyContent:"center", flexDirection: "row"}}>
                <Pressable style={[styles.button, currIndex <= 0 && styles.off]} onPress={prevItem}><Text style={[styles.textStyle]}>PREVIOUS</Text></Pressable>
                <Pressable style={[styles.button, currIndex >= items.length -1 && styles.off]} onPress={nextItem}><Text style={[styles.textStyle]}>NEXT</Text></Pressable>
                </View>
                <BadgerSaleItem items={items[currIndex]}/>    
                
                <View style={{justifyContent: "center", flexDirection:"row"}}>
                    <Pressable style={[styles.button, currentCount <=0 && styles.off]} onPress={countSubtract}><Text style={[styles.textStyle]}>-</Text></Pressable>
                    <Text style={{ fontSize:25, paddingHorizontal: 10 }}>{cart[items[currIndex].name] || 0}</Text>
                    <Pressable style={[styles.button, currentCount >= item?.upperLimit && styles.off]} onPress={countAdd}><Text style={[styles.textStyle]}>+</Text></Pressable>
                </View>

                <Text style={{fontSize:15, justifyContent: "center"}}> You have {getTotalItemsCount()} item(s) costing ${total()} in your cart!</Text>
                <Pressable style={[styles.button]} onPress={placeOrder}><Text style={[styles.textStyle]}>PLACE ORDER</Text></Pressable>
                
                </>
                
            ):(
                <Text>Loading...</Text>
            )
            
        }
    </View>
}

const styles = StyleSheet.create({
    button: {
        borderRadius: 5,
        backgroundColor: '#2196F3',
        padding: 10,
        alignSelf: 'center'
        
    },
    textStyle: {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center'
    },
    off: {
        backgroundColor: 'grey'
    }
})