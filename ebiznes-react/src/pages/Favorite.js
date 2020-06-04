import React, {useState, useEffect, useContext} from 'react';
import ProductItem from '../components/ProductItem';
import { getUrl, getRequestInit } from "../utils/request";
import { FavoriteContext } from '../providers/FavoriteProvider';
import "../style/products.css";

const Favorite = () => {
    const [products, setProducts] = useState([])
    const [favorites, setFavorites] = useState([])
    const [rerender, setRerender] = useState(false)
    const { favorite } = useContext(FavoriteContext);

    useEffect(() => {
        fetch(getUrl(`favoriteproduct/favorite/${favorite.id}`), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setFavorites(data))
            .catch((error) => {
                console.log(error)
            })
    }, [rerender])

    useEffect(() => {
        let promises = [];
        favorites.forEach( (favoriteProduct) => {
            promises.push(fetch(getUrl(`product/${favoriteProduct.product}`), getRequestInit({method: 'GET'})))
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
                    <div key={product.id} className="col4 column">
                        <i className="icon icon-trash" onClick={() => handleDelete(product.id,favorites.filter(obj => obj.product === product.id))}></i>
                        <ProductItem  product={product} isIconVisible={false}/>
                    </div>
                ))}
            </div>
            <div className="air"></div>
        </div>
    );
}

export default Favorite