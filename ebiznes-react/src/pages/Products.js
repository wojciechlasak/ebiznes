import React, { useState, useEffect } from 'react';
import ProductItem from '../components/ProductItem';
import { getUrl, getRequestInit } from "../utils/request";
import "../style/products.css";

const Products = () => {
    const [products, setProducts] = useState([])

    useEffect(() => {
        fetch(getUrl('products'), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setProducts(data))
            .catch((error) => {
                console.log(error)
            })
    }, [])

    return (
        <div className="part column">
            <h2>Buty naszej oferty ({products.length})</h2>
            <div className="flex flex-wrap">
                {products.map((product) => (
                    <ProductItem key={product.id} product={product} />
                ))}
            </div>
            <div className="air"></div>
        </div>
    );
}

export default Products