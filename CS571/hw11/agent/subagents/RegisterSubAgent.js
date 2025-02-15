import AIEmoteType from "../../components/chat/messages/AIEmoteType";
import { ofRandom , isLoggedIn} from "../Util";

const createRegisterSubAgent = (end) => {

    let stage;

    let username, password;

    //also followed from class example

    const handleInitialize = async (promptData) => {
        if (await isLoggedIn()) {
            return end(ofRandom([
                "You are already logged in, try logging out first.",
                "You are already signed in, try signing out first."
            ]))
        } else {
            stage = "FOLLOWUP_USERNAME";
            return ofRandom([
                "Sure, what username would you like to use?",
                "Alright, what username do you want?"
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
            "Great, what password would you like to use?",
            "Thanks, what password would you want to use?"
        ]),
        nextIsSensitive: true
    }
    }

    const handleFollowupPassword = async (prompt) => {
        password = prompt;
        const resp = await fetch("https://cs571.org/api/s24/hw11/register", {
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
            return end(ofRandom({
                msg: "Successfully registered!",
                emote: AIEmoteType.SUCCESS
        }))
        } else {
            return end({
                msg: "Sorry, that username is taken.",
                emote: AIEmoteType.ERROR
                
        })
        }      
    }

    return {
        handleInitialize,
        handleReceive
    }
}

export default createRegisterSubAgent;