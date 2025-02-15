import createChatDelegator from "./ChatDelegator";
import {isLoggedIn, logout, ofRandom} from "./Util";

const createChatAgent = () => {
    const CS571_WITAI_ACCESS_TOKEN = "ZTWUC6R7NEEOQ66EY27WVYS4NU6C4EX3"; // Put your CLIENT access token here.

    const delegator = createChatDelegator();

    let chatrooms = [];

    const handleInitialize = async () => {
        const resp = await fetch("https://cs571.org/api/s24/hw11/chatrooms", {
            headers: {
                "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
            }
        });
        const data = await resp.json();
        chatrooms = data;

        return "Welcome to BadgerChat! My name is Bucki, how can I help you?";
    }

    const handleReceive = async (prompt) => {
        if (delegator.hasDelegate()) { return delegator.handleDelegation(prompt); }
        const resp = await fetch(`https://api.wit.ai/message?q=${encodeURIComponent(prompt)}`, {
            headers: {
                "Authorization": `Bearer ${CS571_WITAI_ACCESS_TOKEN}`
            }
        })
        const data = await resp.json();
        if (data.intents.length > 0) {
            //const chatNames = data.entities['chatroomNames:chatroomNames'];
            //onst quantities = data.entities['wit$number:number'] ? data.entities['wit$number:number'][0] : null;
            switch (data.intents[0].name) {
                case "get_help": return handleGetHelp();
                case "get_chatrooms": return handleGetChatrooms();
                case "get_messages": return handleGetMessages(data);
                case "login": return handleLogin();
                case "register": return handleRegister();
                case "create_message": return handleCreateMessage(data);
                case "logout": return handleLogout();
                case "whoami": return handleWhoAmI();
            }
        }
        return "Sorry, I didn't get that. Type 'help' to see what you can do!";
    }

    const handleTranscription = async (rawSound, contentType) => {
        const resp = await fetch(`https://api.wit.ai/dictation`, {
            method: "POST",
            headers: {
                "Content-Type": contentType,
                "Authorization": `Bearer ${CS571_WITAI_ACCESS_TOKEN}`
            },
            body: rawSound
        })
        const data = await resp.text();
        const transcription = data
            .split(/\r?\n{/g)
            .map((t, i) => i === 0 ? t : `{${t}`)  // Turn the response text into nice JS objects
            .map(s => JSON.parse(s))
            .filter(chunk => chunk.is_final)       // Only keep the final transcriptions
            .map(chunk => chunk.text)
            .join(" ");                            // And conjoin them!
        return transcription;
    }

    const handleSynthesis = async (txt) => {
        if (txt.length > 280) {
            return undefined;
        } else {
            const resp = await fetch(`https://api.wit.ai/synthesize`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "audio/wav",
                    "Authorization": `Bearer ${CS571_WITAI_ACCESS_TOKEN}`
                },
                body: JSON.stringify({
                    q: txt,
                    voice: "Rebecca",
                    style: "soft"
                })
            })
            const audioBlob = await resp.blob()
            return URL.createObjectURL(audioBlob);
        }
    }

    const handleGetHelp = async () => {
        return ofRandom([
            'Try asking "give me a list of chatrooms", or ask for more help!',
            'Try asking "register for an account", or ask for more help!'
        ])
    }

    const handleGetChatrooms = async () => {
        return `Of course, there are 8 chatrooms: ${chatrooms}`
    }

    const handleGetMessages = async (data) => {
        //const hasSpecifiedNumber = data.entities["wit$number:number"] ? true : false;
        const numComments = data.entities['wit$number:number'] ? data.entities["wit$number:number"][0].value : 1;
        const chatroomEntity = data.entities['chatroomNames:chatroomNames'];
        const chatroomName = chatroomEntity ? chatroomEntity[0].value : undefined;

        let queryParams = [];
        if (chatroomName) {
            queryParams.push(`chatroom=${encodeURIComponent(chatroomName)}`);
        }
        queryParams.push(`num=${numComments}`); // Always add the num parameter since it defaults to 1
        let queryString = queryParams.join('&'); // Join parameters with '&'


        const resp = await fetch(`https://cs571.org/api/s24/hw11/messages?${queryString}`,{
            method: 'GET',
            headers:{
                
                'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
              
            }
        })
        const messages = await resp.json();
        console.log(messages)

        if( messages && messages.messages && messages.messages.length > 0){
            return messages.messages.map(m => `In ${m.chatroom}, ${m.poster} created a post titled '${m.title}' saying '${m.content}`);
        }else{
            return 'No messages to display'
            
        }

    }

    const handleLogin = async () => {
        return await delegator.beginDelegation("LOGIN");
    }

    const handleRegister = async () => {
        return await delegator.beginDelegation("REGISTER");
    }

    const handleCreateMessage = async (data) => {
        if(!await isLoggedIn()){
            return 'You need to be logged in to create a post'
        }
        const chatroomEntity = data.entities['chatroomNames:chatroomNames'];
        if(!chatroomEntity){
            return 'You must specify a chatroom to post in'
        }
        const chatroomName = chatroomEntity[0].value;

        return await delegator.beginDelegation("CREATE", {chatroomName});
    }

    const handleLogout = async () => {
        if(await isLoggedIn()){
            await logout();
            return ofRandom([
                "You have been logged out.",
                "You have been signed out."
            ])
        }else{
            return ofRandom([
                "You are not logged in.",
                "You are currently not logged in."
            ])
        }
        
    }

    const handleWhoAmI = async () => {
        const resp = await fetch(`https://cs571.org/api/s24/hw11/whoami`,{
            method: 'GET',
            headers:{
                'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
               
            },
            //reads my username
            credentials: 'include'
        })
        const user = await resp.json();
        console.log(user)


        if(user.isLoggedIn){
            return `You are currently logged in as ${user.user.username}` 
        }else{
            return 'You are currently not logged in'
        }
    }

    return {
        handleInitialize,
        handleReceive,
        handleTranscription,
        handleSynthesis
    }
}

export default createChatAgent;