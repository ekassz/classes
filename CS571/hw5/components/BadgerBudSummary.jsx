import { useContext, useState } from "react";
import BadgerBudsDataContext from "../contexts/BadgerBudsDataContext";
import { Carousel, Button } from "react-bootstrap";

function BadgerBudSummary({buds, onRemove, isBasketView=false, onSelect}){
    const [savedBuds, setBuds] = useState([]);
    const [details, setDetails] = useState(false);
    
    const showMore = () =>{
        setDetails(true);
    };

    const showLess = () => {
        setDetails(false);
    }

    const save = () =>{
        alert(`${buds.name} has been added to your basket!`);
        const savedCatIds = JSON.parse(sessionStorage.getItem('savedCatIds')) || [];
        sessionStorage.setItem('savedCatIds', JSON.stringify([...savedCatIds, buds.id]));

        onRemove(buds.id);
    };

    const unselect = (buddyId) => {
        alert(`${buds.name} has been removed from your basket!`);
        const savedCatIds = JSON.parse(sessionStorage.getItem('savedCatIds')) || [];
        const updateCats = savedCatIds.filter(id => id !== buddyId);
        sessionStorage.setItem('savedCatIds', JSON.stringify(updateCats));

        onSelect(buds.id);


    }

    const adopt = () => {
        alert(`Thank you for adopting ${buds.name}!`);
        const adoptedCatIds = JSON.parse(sessionStorage.getItem('adoptedCatIds')) || [];
        sessionStorage.setItem('adoptedCatIds', JSON.stringify([...adoptedCatIds, buds.id]));

        onRemove(buds.id);

    }
    const image = buds.imgIds.map(imgId => `https://raw.githubusercontent.com/CS571-S24/hw5-api-static-content/main/cats/${imgId}`);

    return(
    //https://getbootstrap.com/docs/4.0/components/card/
    //help for styling
    <div className="col mb-4">
        <div className="card h-100">
            {details && image.length > 1 ? (
                <Carousel>
                    {image.map((src, index) => (
                        <Carousel.Item key={index}>
                             <img className="card-img-top"  src={src} alt={`Picture of ${buds.name}`} />
                        </Carousel.Item>
                    ))}
            </Carousel>
            ):(
                <img className="card-img-top" src={image[0]} alt={`Picture of ${buds.name}`}></img>
            )}
            <div className="card-body">
                <h2 className="card-text">{buds.name}</h2>
                {isBasketView ? (
                    <div className="card-footer">
                        <button onClick={unselect} className="btn btn-primary">Unselect</button>
                        <button onClick={adopt} className="btn btn-secondary">Adopt</button>
                    </div>
                ): ( 
                <div className="card-footer">
                {details ? (
                    <>
                    <div className="additional-details">
                        <p>{buds.gender}</p>
                        <p>{buds.breed}</p>
                        <p>{buds.age}</p>
                        {buds.description && <p> {buds.description}</p>}
                        </div>
                        <button onClick={showLess} className="btn btn-primary">
                            Show Less
                        </button>
                        <button onClick={save} className="btn btn-secondary">Save</button>
                    </>
                ):(
                    <>
                
                    <button onClick={showMore} className="btn btn-primary">
                       Show More</button>
                    <button onClick={save} className="btn btn-secondary">Save</button>
                    
                </>
                )}
                </div>
                )}
                </div>
            </div>
        </div>
    );
}

export default BadgerBudSummary;