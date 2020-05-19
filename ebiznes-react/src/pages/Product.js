import React, {useState, useEffect} from 'react';
import { useParams } from 'react-router-dom'
import { getUrl, getRequestInit } from "../utils/request";

const Product = () => {
    const { prodId } = useParams();

    const [opinions, setOpinions] = useState([]);
    const [opinionDesc, setOpinionDesc] = useState(null);
    const [category, setCategory] = useState(null);
    const [product, setProduct] = useState([])
    const [tooltip, setTooltip] = useState("")

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

    const handleAddToCart = () => {
        fetch(
            getUrl(`addbasketproduct`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({quantity: 1, basket: 1, product: product.id}), /* change*/
            })
        )
            .then(() => {
                setTooltip("Dodano do koszyka")
                setTimeout(() => {
                    setTooltip("")
                },2000)
            })
    }

    const handleAddToFavorite = () => {
        fetch(
            getUrl(`addfavoriteproduct`),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify({product: product.id, favorite: 1}), /* change*/
            })
        )
            .then(() => {
                setTooltip("Dodano do ulubionych")
                setTimeout(() => {
                    setTooltip("")
                },2000)
            })
    }

    return (
        <div className="part column-double">
            <h2>{product.name}</h2>
            <div className="air"></div>
            <img
                className="product-photo"
                src={require(`../img/jordan.jpg`)}
                alt={product.name}
            />
            <div className="air"></div>
            {product.description}
            <div className="airmik"></div>
            {category ? `Kategoria: ${category.name}` : "-"}
            <div className="rel">
                {tooltip &&
                <div className="abs tooltip-container flex flex-justify-center flex-align-center">{tooltip}</div>}
                <i className="icon icon-cart-plus" onClick={handleAddToCart}></i>
                <i className="icon icon-heart-empty" onClick={handleAddToFavorite}></i>
            </div>
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