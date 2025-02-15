import React, { useContext, useEffect, useState } from "react"
import BadgerBudSummary from "../../BadgerBudSummary";
import BadgerBudsDataContext from "../../../contexts/BadgerBudsDataContext";
import { Col, Container, Row } from "react-bootstrap";

export default function BadgerBudsBasket(props) {
    const [savedBuds, setBuds] = useState([]);
    const buddyData = useContext(BadgerBudsDataContext);

    useEffect(() =>{
        const savedCatIds = JSON.parse(sessionStorage.getItem('savedCatIds')) || [];
        const filteredCats = buddyData.filter(buds => savedCatIds.includes(buds.id));
        setBuds(filteredCats);
    }, [buddyData]);

    const unselect = (buddyId) => {

        setBuds(savedBuds => savedBuds.filter(bud => bud.id !== buddyId));
    };

    const adopt = (id) => {
        setBuds(adpoting => adpoting.filter(buds => buds.id !== id));

    };

    return (<Container>
        
            <h1>Badger Buds Basket</h1>
            <p>These cute cats could be all yours!</p>
            <Row>
                {savedBuds.length > 0 ? (
                savedBuds.map(buds => (
                    <Col xs={12} sm={6} md={4} key={buds.id}>
                        <BadgerBudSummary buds = {buds} onSelect = {unselect} onRemove={adopt}isBasketView={true} />
                    </Col>
                ))
                ):(
                <p>You have no buds in your basket!</p>
                )}
        </Row>
        </Container>
    );
}
