import React, {useEffect, useState} from "react";
import {getRequestInit, getUrl} from "../utils/request";
import ProductItem from "../components/ProductItem";
import { Link } from "react-router-dom";

const Home = () => {
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
            <h1>Witaj na naszej stronie zobacz co mamy w ofercie</h1>
            <div className="flex flex-wrap">
                {products.map((product,index) => {
                    if (index < 3) {
                        return (
                            <div key={product.id} className="col4 column">
                                <ProductItem product={product} isIconVisible={true}/>
                            </div>
                        )
                    }
                    return null;
                })}
            </div>
            <div className="air"></div>
            <Link to='/products'><button className="button-base">Zobacz wszystkie produkty</button></Link>
            <div className="air"></div>
        </div>
    )
}

export default Home;