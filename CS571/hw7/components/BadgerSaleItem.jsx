import { Text, View, Image} from "react-native";

export default function BadgerSaleItem(props) {
    
const {items} = props;

    return <View style={{alignItems: 'center', justifyContent:'center'}}>
        <Image style={{width: 250, height: 250}} source={{ uri: items.imgSrc}}/>
        <Text style={{textAlign: "center" , fontSize: 48}}>{items.name}</Text>
        <Text style={{textAlign: "center" ,fontSize: 24}}>${items.price.toFixed(2)} each</Text>
        <Text style={{textAlign: "center" ,fontSize: 24}}>You can order up to {items.upperLimit} units!</Text>
        
    </View>
}
