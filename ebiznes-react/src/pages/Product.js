import React, {useState, useEffect} from 'react';
import { useParams } from 'react-router-dom'
import { getUrl, getRequestInit } from "../utils/request";

const Product = () => {
    const { prodId } = useParams();

    const [opinions, setOpinions] = useState([]);
    const [opinionDesc, setOpinionDesc] = useState(null);
    const [category, setCategory] = useState(null);
    const [product, setProduct] = useState([])

    useEffect(() => {
        async function fetchProduct() {
            let response = await fetch(getUrl(`product/${prodId}`), getRequestInit({method: 'GET'}));
            let data = await response.json()
            setProduct(data);
            response = await fetch(getUrl(`category/${data.category}`), getRequestInit({method: 'GET'}));
            data = await response.json();
            setCategory(data);
        }

        try {
            fetchProduct()
        } catch (error) {
            console.log(error)
        }
    }, [prodId])

    useEffect(() => {
        fetch(getUrl(`opinion/product/${prodId}`), getRequestInit({method: 'GET'}))
            .then(response => response.json())
            .then(data => setOpinions(data))
    }, [prodId])

    const handleSubmit = () => {
        fetch(
            getUrl(`addopinion`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({description: opinionDesc, product: prodId}),
            })
        )
    }

    return (
        <div>
            {product.name}
            {product.description} <br />
            {category ? category.name : "-"}
            <button>Favorite</button>
            <button>Dodaj do koszyka</button>
            <h3>Opinie</h3>
            {opinions !== []
                ? opinions.map( opinion => <div><p>{opinion.description}</p></div>)
                : <h4>Nie ma jeszcze komentarzy dodaj jako pierwszy!</h4>
            }
            <form>
                <textarea
                    placeholder="Dodaj komentarz"
                    type="text"
                    onChange={(e) => {setOpinionDesc(e.target.value)}}
                />
                <button
                    onClick={handleSubmit}
                >
                    Dodaj
                </button>
            </form>
        </div>
    );
}

export default Product