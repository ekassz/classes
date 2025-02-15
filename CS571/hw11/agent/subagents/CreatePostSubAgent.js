import { isLoggedIn, ofRandom } from "../Util";

const createPostSubAgent = (end) => {
     const CS571_WITAI_ACCESS_TOKEN = "ZTWUC6R7NEEOQ66EY27WVYS4NU6C4EX3"

    let stage = 'GET_TITLE';
    let chatroomName;
    let title;
    let content;


    const handleInitialize = async (data) => {
        chatroomName = data.chatroomName
        return ofRandom([
            "What would you like the title to be?",
            "Sure, what do you want as the title?"
        ])
    }

    const handleReceive = async (prompt) => {
        switch(stage) {
            case "GET_TITLE": 
            title = prompt;
            stage = "GET_CONTENT";
            return ofRandom([
                "What do you want the content to be?",
                "What is the content of this post?"
            ])
            case "GET_CONTENT":
                content = prompt;
                stage = "CONFIRM";
                return ofRandom([
                    "To confirm do you want to create a post called '" + title + `' in ${chatroomName}?`,
                    "All ready, to confirm you want to make a post '" + title + "' with the content '" + content + "' ?"
                ])
            case "CONFIRM":
                if(prompt.toLowerCase() === 'yes'){
                    const resp = await fetch(`https://cs571.org/api/s24/hw11/messages?chatroom=${chatroomName}`,{
                        method: 'POST',
                        headers:{
                           'X-CS571-ID': 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce', 
                            'Content-Type': 'application/json',
                            "Authorization": `Bearer ${CS571_WITAI_ACCESS_TOKEN}`
                        },
                        body: JSON.stringify({
                            title:title,
                            content:content
                        }),
                        credentials: 'include'
                    })
                    const post = await resp.json();
                    console.log(post)

                    return end("Your post has been created in " + chatroomName + ".")
                }else{
                    return end("Your post wasn't able to be created.")
                }
        }
    }


    return {
        handleInitialize,
        handleReceive
    }
}

export default createPostSubAgent;