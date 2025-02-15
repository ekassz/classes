import React, { useEffect, useState } from "react"
import BadgerMessage from './BadgerMessage';
import {Col, Row, Container, Pagination} from 'react-bootstrap';

export default function BadgerChatroom(props) {

    const [messages, setMessages] = useState([]);
    const [activePage, setActivePage] = useState(1);

    const loadMessages = () => {
        fetch(`https://cs571.org/api/s24/hw6/messages?chatroom=${props.name}&page=${activePage}`, {
            headers: {
                "X-CS571-ID": 'bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce'
            }
        }).then(res => res.json()).then(json => {
            setMessages(json.messages)
        })
        .catch(error => console.error("Failed to load messages", error));
    };


    // Why can't we just say []?
    // The BadgerChatroom doesn't unload/reload when switching
    // chatrooms, only its props change! Try it yourself.
    
    useEffect(() =>{
        loadMessages();
    }, [props.name, activePage]);

    

    return (<Container>
        <h1>{props.name} Chatroom</h1>
        {
            /* TODO: Allow an authenticated user to create a post. */
        }
        <hr/>
        
            {messages.length > 0 ?(
                <Row>
                    {
                        /* TODO: Complete displaying of messages. */
                        messages.map((message) => (
                            <Col xs={12} sm={12} md={6} lg={4} key={message.id}>
                            <BadgerMessage
                            title = {message.title}
                            poster = {message.poster}
                            content = {message.content}
                            created = {message.created}
                            
                            ></BadgerMessage>
                            </Col>
                        ))

                    }

                </Row>
                ):(
                
                    <p>There are no messages on this page yet!</p>
                )}
                <Pagination>
                    <Pagination.Item active={activePage ===1} onClick={() => setActivePage(1)}>1</Pagination.Item>
                    <Pagination.Item active={activePage ===2} onClick={() => setActivePage(2)}>2</Pagination.Item>
                    <Pagination.Item active={activePage ===3} onClick={() => setActivePage(3)}>3</Pagination.Item>
                    <Pagination.Item active={activePage ===4} onClick={() => setActivePage(4)}>4</Pagination.Item>
                </Pagination>
                
        </Container>
        
    );
}
