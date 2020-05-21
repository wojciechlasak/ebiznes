import React, {useState, useEffect} from 'react';
import { useParams } from 'react-router-dom'
import { getUrl, getRequestInit } from "../utils/request";
import ProductIcons from "../components/ProductIcons";

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
        <div className="part column-double">
            <h2>{product.name}</h2>
            <div className="flex flex-align-center">
                <div className="col2">
                    {product.photo && <img
                        className="product-photo"
                        src={require(`../img/${product.photo}`)}
                        alt={product.name}
                    />}
                </div>
                <div className="col2">
                    <strong>Opis: </strong>{product.description}
                    <div className="airmik"></div>
                    <strong>Kategoria: </strong>{category ? category.name : "-"}
                    <div className="airmik"></div>
                    <ProductIcons productId={product.id} />
                </div>
            </div>
            <h3>Opinie</h3>
            {Boolean(opinions) !== false
                ? opinions.map( opinion => <div key={opinion.id} className="product-opinion"><p>{opinion.description}</p></div>)
                : <h4>Nie ma jeszcze komentarzy dodaj jako pierwszy!</h4>
            }
            <form>
                <textarea
                    placeholder="Dodaj komentarz"
                    type="text"
                    onChange={(e) => {setOpinionDesc(e.target.value)}}
                />
                <div className="airmin"></div>
                <button
                    className="button-base"
                    onClick={handleSubmit}
                >
                    Dodaj
                </button>
            </form>
            <div className="air"></div>
        </div>
    );
}

export default Product