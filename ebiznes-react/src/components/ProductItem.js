import React from 'react';
import { Link } from "react-router-dom";
import ProductIcons from "./ProductIcons";

const ProductItem = ({ product, isIconVisible }) => {
    return (
        <>
            <Link to={`/product/${product.id}`}>
                <div className="product-item-container">
                    <h2>{product.name}</h2>
                    <img
                        src={require(`../img/${product.photo}`)}
                        alt={product.name}
                    />
                </div>
            </Link>
            {isIconVisible && <ProductIcons productId={product.id} />}
        </>
    )
}

export default ProductItem