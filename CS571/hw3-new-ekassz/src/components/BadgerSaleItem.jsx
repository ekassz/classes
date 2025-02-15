export default function BadgerSaleItem(props) {
    return <div>
        <h2>{props.name}</h2>
        <div>
            <button className="inline">-</button>
            <p className="inline">0</p>
            <button className="inline">+</button>
        </div>
    </div>
}