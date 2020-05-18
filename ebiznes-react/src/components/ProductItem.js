import React from 'react';
import { Link } from "react-router-dom";
import {getRequestInit, getUrl} from "../utils/request";

const ProductItem = ({ product }) => {

    const handleAddToCart = () => {
        fetch(
            getUrl(`addbasketproduct`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({quantity: 1, basket: 1, product: product.id}), /* change*/
            })
        )
    }

    const handleAddToFavorite = () => {
        fetch(
            getUrl(`addfavoriteproduct`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({product: product.id, favorite: 1}), /* change*/
            })
        )
    }

    return (
        <div className="col4 column">
            <Link to={`/product/${product.id}`}>
                <div className="product-item-container">
                    <h2>{product.name}</h2>
                    <img
                        src={require(`../img/${product.photo}`)}
                        alt={product.name}
                    />
                </div>
            </Link>
            <div>
                <i className="icon icon-cart-plus" onClick={handleAddToCart}></i>
                <i className="icon icon-heart-empty" onClick={handleAddToFavorite}></i>
            </div>
        </div>
    )
}

export default ProductItem