import React, {useState, useEffect} from 'react';
import {Link, useParams} from 'react-router-dom'
import { getUrl, getRequestInit } from "../utils/request";

const Order = () => {
    const { baskId } = useParams();

    const [payments, setPayments] = useState([])
    const [payment, setPayment] = useState(null)
    const [product, setProduct] = useState([])
    const [completeOrder, setCompleteOrder] = useState(false)

    useEffect(() => {
        fetch(getUrl(`payments`), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setPayments(data))
    }, [])

    const handleSubmit = () => {
        fetch(
            getUrl(`addorder`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({basket: baskId, payment: payment}),
            })
        ).then(() => {
            setCompleteOrder(true)
        })
    }

    const handleSelectChange = (event) => {
        setPayment(event.target.value)
    }

    return (
        <div>
            <Link to="/basket"><button>cofnij</button></Link>
            <h2>Wybierz metode płatności</h2>
            <select onChange={handleSelectChange}>
                <option>Wybierz metode płatności</option>
                {payments.map((payment) => <option value={payment.id}>{payment.name}</option>)}
            </select>
            <button disabled={payment === null} onClick={handleSubmit}>Zamwiam z obowiązkiem zapłaty</button>
            {completeOrder &&
                <div>
                    <h2>Twoje zamówienie zostało przyjęte, zapraszamy do dalszych zakupów</h2>
                    <Link to="/"><button>X</button></Link>
                </div>
            }
        </div>
    );
}

export default Order