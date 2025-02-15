import React, { useState } from 'react';
import {Button, Col, Form, Row} from 'react-bootstrap';

export default function BadgerRegister() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirm, setPasswordConfirm] = useState("");
    

    function handleRegisterSubmit(e) {
        e?.preventDefault();
        
        if(!username || !password){
            alert('You must provide both a username and password!');
        }
        if(password !== passwordConfirm){
            alert('Your passwords do not match!')
        }
        handleRegister();
    }

    function handleRegister() {
        fetch('https://cs571.org/api/s24/hw6/register', {
            method:"POST",
            credentials: "include",
            headers:{
                "Content-Type" : "application/json",
                "X-CS571-ID" : "bid_63ed070a4e5af5e79ef6893489cebe2f86e0fb75fcf2d0e6577911e9f14d0cce"
            },
            body: JSON.stringify({username, password})

        })
        .then(res => res.json())
        .then(data => {
            if(data.error){
                alert("That username has already been taken!");

            }else{
                alert("Registration was successful!");
            }
        })
    }



    // TODO Create the register component.

    return <>
        <h1>Register</h1>
        <Form>
            <Form.Group as={Row } className='mb-3' controlId='formPlaintextEmail'>
                <Form.Label column sm="2">
                    Username
                </Form.Label>
                <Col sm="10">
                    <Form.Control type='username' value={username} onChange={(e) => setUsername(e.target.value)}/>
                </Col>

            </Form.Group>

            <Form.Group as={Row} className='mb-3' controlId='formPlaintextPassword'>
                <Form.Label column sm="2">
                    Password
                </Form.Label>
                <Col sm="10">
                    <Form.Control type='password' value={password} onChange={(e) => setPassword(e.target.value)}/>
                </Col> 
            </Form.Group>

            <Form.Group as={Row} className='mb-3' controlId='formPlaintextRepeatPassword'>
                <Form.Label column sm="2">
                    Repeat Password
                </Form.Label>
                <Col sm="10">
                    <Form.Control type='password' value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)}/>
                </Col>
            </Form.Group>
            <br/>
            <Button type='submit' onClick={handleRegisterSubmit}>Register</Button>
        </Form>
    </>
}
