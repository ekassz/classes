import { isLoggedIn, ofRandom } from "../Util";
import AIEmoteType from "../../components/chat/messages/AIEmoteType";

const createLoginSubAgent = (end) => {

    let stage;

    let username, password;

    const handleInitialize = async (promptData) => {
        //from class example
        if(await isLoggedIn()){
            return end(ofRandom([
                "You are already logged in, try logging out first.",
                "You are already signed in, try signing out first."
            ]))
        }else {
            stage = 'FOLLOWUP_USERNAME';
            return ofRandom([
                "Sure, what is your username?",
                "Alright, what is your username?"
            ])
        }
    }

    const handleReceive = async (prompt) => {
        switch(stage) {
            case "FOLLOWUP_USERNAME": return await handleFollowupUsername(prompt);
            case "FOLLOWUP_PASSWORD": return await handleFollowupPassword(prompt);
        }
        
    }
    const handleFollowupUsername = async (prompt) => {
        username = prompt;
        stage = "FOLLOWUP_PASSWORD";
        return {
            
            msg: ofRandom([
            "Great, and what is your password?",
            "Thanks, and what is your password?"
        ]),
        nextIsSensitive: true 
    }
    }

    const handleFollowupPassword = async (prompt) => {
        password = prompt;
        const resp = await fetch("https://cs571.org/api/s24/hw11/login", {
            method: "POST",
            credentials: "include",
            headers: {
                "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce',
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
        
        if (resp.status === 200) {
            return end({
                msg: "Successfully logged in!",
                emote: AIEmoteType.SUCCESS
        })
        } else {
            return end({
                msg: "Sorry, that username and password is incorrect.",
                emote: AIEmoteType.ERROR
        })
        }      
    }

    return {
        handleInitialize,
        handleReceive
    }
}

export default createLoginSubAgent;