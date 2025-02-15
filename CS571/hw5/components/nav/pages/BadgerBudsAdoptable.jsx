import React, { useContext, useEffect, useState } from "react"
import BadgerBudSummary from "../../BadgerBudSummary";
import BadgerBudsDataContext from "../../../contexts/BadgerBudsDataContext";
import { Col, Container, Row } from "react-bootstrap";

export default function BadgerBudsAdoptable(props) {
    const [availableBud, setAvailableBud] = useState([]);
    const buddyData = useContext(BadgerBudsDataContext);

    useEffect(() =>{
        const savedCatIds = JSON.parse(sessionStorage.getItem('savedCatIds')) || [];
        const filteredCats = buddyData.filter(buds => !savedCatIds.includes(buds.id));
        setAvailableBud(filteredCats);
    }, [buddyData]);

    const removeCat = (id) => {
        setAvailableBud(currentBuds => currentBuds.filter(buds => buds.id !== id ));
    };


    return (<Container>

        <h1>Available Badger Buds</h1>
        <p>The following cats are looking for a loving home! Could you help?</p>
        
        <Row>
            {availableBud.length > 0 ? (
                availableBud.map(buds =>(
                <Col xs={12} sm={12} md={6} lg={4} xl={3}
                key={buds.id}>
                    <BadgerBudSummary buds={buds} onRemove={removeCat} isBasketView={false}/>
                    </Col>
                    ))
                ):(
                <p>No buds available for adoption!</p>
                )}
            </Row>
        </Container>
        
    
    );
}