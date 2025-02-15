
const createChatAgent = () => {

    const CS571_WITAI_ACCESS_TOKEN = "G4LE7FHI5ZNIPYAAAIWBXNILQOHJYYSO"; // Put your CLIENT access token here.

    let availableItems = [];
    let cart = [];
    

    const handleInitialize = async () => {
        const res = await fetch('https://cs571.org/api/s24/hw10/items',{
            headers:{
                'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
            }
        })
        availableItems = await res.json();

        return "Welcome to BadgerMart Voice! :) Type your question, or ask for help if you're lost!"
    }

    const get_help = () => {
        return 'In BadgerMart Voice, you can get the list of items, the price of an item, add or remove an item from your cart, and checkout!'
    }

    const get_items = async () => {
        if(availableItems.length === 0){
            return 'There are no items available right now.'
        }
        
        const lastItem = availableItems[availableItems.length -1].name;
        const itemsList = availableItems.slice(0, -1).map(item => item.name).join(', ');
        return `We have ${itemsList} and ${lastItem} for sale!`;
    }

    const get_price = async(itemName) => {
        
        const item = availableItems.find(item => item.name.toLowerCase() === itemName.toLowerCase());
        if(item){
            
            const formatPrice = parseFloat(item.price).toFixed(2);
            return `${item.name}s cost $${formatPrice} each. `
        }else{
            return `Sorry, the item ${itemName} is not in stock`
        }
        
    }
    
    const add_item = async(itemName, quantity) => {
    
        quantity = parseInt(quantity, 10);
        if(quantity <= 0 || isNaN(quantity)) {
            return 'That quantity is invalid. Please add at least one item.'
        }
        const item = availableItems.find(item => item.name.toLowerCase() === itemName.toLowerCase());
        if(item){
            const cartItem = cart.find(cartItem => cartItem.name.toLowerCase() === itemName.toLowerCase());
            if(cartItem){
                cartItem.quantity += quantity;
            }else{
                cart.push({name: itemName, quantity: quantity});
            }
            return `Sure, adding ${quantity} ${itemName}(s) to your cart.`;
        } else{
            return `Sorry, the item ${itemName} is not in stock`
        }

    }

    const remove_item = async(itemName, quantities) =>{
        let quantity;
        if (quantities && quantities.value) {
            // This checks if the value is 'a' or 'an' and sets it to 1.
            if (quantities.value === 'a' || quantities.value === 'an') {
                quantity = 1;
            } else {
                // If it's not 'a' or 'an', try to parse it as an integer.
                quantity = parseInt(quantities.value, 10);
            }
        } else {
            quantity = 1;
        }
        if(quantity <= 0){
            return 'That quantity is invalid. Please use a positive number!'
        }

        const item = cart.findIndex(item => item.name.toLowerCase() === itemName.toLowerCase());
        
        if(item !== -1){
            if(cart[item].quantity < quantity){
                cart.splice(item, 1);
                return `All ${itemName}s were removed from your cart since you had less than ${quantity}.`
            }else{
                cart[item].quantity -= quantity;
                return `Removed ${quantity} ${itemName}(s) from your cart`
            }
        }else{
            return `The item ${itemName} is not in your cart`;
        }

    }

    const view_cart = async() => {

        if(cart.length === 0){
            return "Your cart is currently empty."
        }
        let cartSummary = 'Your cart contains: ';
        let totalCost = 0;

        cart.forEach(cartItem =>{
            const item = availableItems.find(item => item.name.toLowerCase() === cartItem.name.toLowerCase());
            const itemCost = item ? item.price * cartItem.quantity : 0;
            totalCost += itemCost;

            cartSummary += `${cartItem.quantity} ${cartItem.name}(s) ($${item.price} each), `
        });

        cartSummary = cartSummary.slice(0,-2);
        totalCost = totalCost.toFixed(2);
        cartSummary += `. The total cost is $${totalCost}.`;
        return cartSummary;
    }

    const checkout = async() =>{
        if(cart.length === 0){
            return 'Your cart is empty, please add some items before checking out!'
        }

        // Initialize the checkoutObject with all items set to 0 quantity
        const checkoutObject = availableItems.reduce((obj, item) => {
            const cartItem = cart.find(cartItem => cartItem.name.toLowerCase() === item.name.toLowerCase());
            obj[item.name] = cartItem ? cartItem.quantity : 0;
            return obj;
        }, {});
        

        console.log(checkoutObject);
        
        const resp = await fetch('https://cs571.org/api/s24/hw10/checkout', {
            method: 'POST',
            headers:{
            'Content-Type': 'application/json',
            'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
            },
            body: JSON.stringify(checkoutObject)
        });
        const data = await resp.json();
        console.log(data.dt);
        console.log(data.confirmationId);
        console.log(data.msg);

        if(data.msg === "Successfully purchased!"){
            cart = [];
            return `Successfully purchased! Your confirmation ID is ${data.confirmationId}.`; 
        }
    }

    const handleReceive = async (prompt) => {
        // TODO: Replace this with your code to handle a user's message!
        const resp = await fetch('https://api.wit.ai/message?q=' + encodeURIComponent(prompt),{
            headers:{
                "Authorization": "Bearer " + CS571_WITAI_ACCESS_TOKEN
            }
        })
        const data = await resp.json();
        console.log(data)

        if(data.intents.length > 0){
            const inStock = data.entities['inStock:inStock'];
            const notStocked = data.entities['notStocked:notStocked'];
            const quantities = data.entities['wit$number:number'] ? data.entities['wit$number:number'][0] : null;
            //const quantityEntity = quantities && quantities.length > 0 ? Math.max(1, Math.floor(quantities[0].value)) : null;

            switch(data.intents[0].name){
                case 'get_help': return get_help();
                case 'get_items': return get_items();
                case 'get_price':
                if(inStock){
                    const itemName = inStock[0].value;
                    return get_price(itemName);
                }else if(notStocked){
                    const itemName = notStocked[0].value;
                    return get_price(itemName);
                }
                case 'add_item': 
                let quantity;
                if (quantities && quantities.value) {
                    // This checks if the value is 'a' or 'an' and sets it to 1.
                    if (quantities.value === 'a' || quantities.value === 'an') {
                        quantity = 1;
                    } else {
                        // If it's not 'a' or 'an', try to parse it as an integer.
                        quantity = parseInt(quantities.value, 10);
                    }
                } else {
                    
                    quantity = 1;
                }

                if(inStock){
                    const itemName = inStock[0].value;
                    return add_item(itemName, quantity);
                }else{
                    return 'We dont have that item in stock right now.'
                }
                case 'remove_item':
                    
                if(inStock){
                    const itemName = inStock[0].value;
                    return remove_item(itemName, quantities);
                }else{
                    return 'Can you specify the item to remove from your cart?';
                }
                case 'view_cart': return view_cart();
                case 'checkout': return checkout();
                
            }
        }
        
        return "Sorry, I didn't get that. Type 'help' to see what you can do!"
    }


    return {
        handleInitialize,
        handleReceive
    }
}

export default createChatAgent;