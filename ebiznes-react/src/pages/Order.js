import React, {useState, useEffect} from 'react';
import {Link, useParams} from 'react-router-dom'
import Select from 'react-select';
import { getUrl, getRequestInit } from "../utils/request";

import "../style/lightbox.css";

const Order = () => {
    const { baskId } = useParams();

    const [payments, setPayments] = useState([])
    const [payment, setPayment] = useState(null)
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
                body: JSON.stringify({basket: baskId, payment: payment.value}),
            })
        ).then(() => {
            setCompleteOrder(true)
        })
    }

    return (
        <div className="part column">
            <div className="airmin"></div>
            <Link to="/basket"><button className="button-base">cofnij</button></Link>
            <h3>Ostatni krok zakupów</h3>
            <div className="select-container">
                <Select
                    value={payment}
                    onChange={payment => setPayment(payment)}
                    options={payments.map((p) => ({value: p.id, label: p.name}))}
                />
            </div>
            <div className="airmin"></div>
            <button className="button-base button-red" disabled={payment === null} onClick={handleSubmit}>Zamwiam z obowiązkiem zapłaty</button>
            {completeOrder &&
                <div className={`lightbox-container ${completeOrder ? "opened" : " "}`}>
                    <Link to="/"><div className="lightbox-bg"/></Link>
                        <div className="lightbox-in">
                            <h2>Twoje zamówienie zostało przyjęte, zapraszamy do dalszych zakupów</h2>
                            <Link to="/">
                                <svg className="lightbox-close" viewBox="0 0 100 100">
                                    <path d="M10 10L90 90" />
                                    <path d="M90 10L10 90" />
                                </svg>
                            </Link>
                        </div>
                </div>
            }
        </div>
    );
}

export default Order