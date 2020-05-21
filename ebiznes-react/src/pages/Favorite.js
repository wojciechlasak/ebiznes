import React, { useState, useEffect } from 'react';
import ProductItem from '../components/ProductItem';
import { getUrl, getRequestInit } from "../utils/request";
import "../style/products.css";

const Favorite = () => {
    const [products, setProducts] = useState([])
    const [favorites, setFavorites] = useState([])
    const [rerender, setRerender] = useState(false)

    useEffect(() => {
        fetch(getUrl('favoriteproduct/favorite/1'), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setFavorites(data))
            .catch((error) => {
                console.log(error)
            })
    }, [rerender])

    useEffect(() => {
        let promises = [];
        favorites.forEach( (favorite) => {
            promises.push(fetch(getUrl(`product/${favorite.product}`), getRequestInit({method: 'GET'})))
        })
        Promise.all(promises)
            .then(responses =>
                Promise.all(responses.map(res => res.json()))
            )
            .then(data => {
                return setProducts(data)
            })

    }, [favorites])

    const handleDelete = (productId, favoriteProduct) => {
        fetch(getUrl(`deletefavoriteproduct/${favoriteProduct[0].id}`), getRequestInit({method: 'DELETE'}))
        setRerender(!rerender)
    }

    return (
        <div className="part column">
            <h2>Twoje ulubione produkty ({products.length})</h2>
            <div className="flex flex-wrap">
                {products.map((product) => (
                    <div className="col4 column">
                        <i className="icon icon-trash" onClick={() => handleDelete(product.id,favorites.filter(obj => obj.product === product.id))}></i>
                        <ProductItem key={product.id} product={product} isIconVisible={false}/>
                    </div>
                ))}
            </div>
            <div className="air"></div>
        </div>
    );
}

export default Favorite