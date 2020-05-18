import React, { useState, useEffect } from 'react';
import ProductItem from '../components/ProductItem';
import { getUrl, getRequestInit } from "../utils/request";
import { Link } from 'react-router-dom';
import "../style/products.css";

const Basket = () => {
    const [products, setProducts] = useState([])
    const [basketProducts, setBasketProducts] = useState([])

    useEffect(() => {
        fetch(getUrl('basketproduct/basket/1'), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setBasketProducts(data))
            .catch((error) => {
                console.log(error)
            })
    }, [])

    useEffect(() => {
        let promises = [];
        basketProducts.forEach( (basket) => {
            promises.push(fetch(getUrl(`product/${basket.product}`), getRequestInit({method: 'GET'})))
        })
        Promise.all(promises)
            .then(responses =>
                Promise.all(responses.map(res => res.json()))
            )
            .then(data => {
                return setProducts(data)
            })

    }, [basketProducts])

    return (
        <div className="part column">
            <h2>Twój koszyk ({products.length})</h2>
            {products.length !== 0 ?
                <>
                    <Link to={`/order/1`}><button>Złóż zamówienie</button></Link>
                    <div className="flex flex-wrap">
                        {products.map((product) => (
                            <ProductItem key={product.id} product={product} />
                        ))}
                    </div>
                </>
                :
                <h3>Twój koszyk jest pusty wybierz coś z naszej oferty</h3>
            }
            <div className="air"></div>
        </div>
    );
}

export default Basket