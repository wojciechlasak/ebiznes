import React, {useState} from "react";
import {getRequestInit, getUrl} from "../utils/request";

const ProductIcons = ({ productId }) => {
    const [tooltip, setTooltip] = useState("")

    const handleAdd = (url, body, text) => {
        console.log(getUrl(url))
        fetch(
            getUrl(url),
            getRequestInit({
                method: 'POST',
                body: JSON.stringify(body), /* change*/
            })
        )
            .then(() => {
                setTooltip(text)
                setTimeout(() => {
                    setTooltip("")
                },2000)}
            )

    }

    return (
        <div className="rel">
            {tooltip && <div className="abs tooltip-container flex flex-justify-center flex-align-center">{tooltip}</div>}
            <i
                className="icon icon-cart-plus"
                onClick={
                    () => handleAdd(
                        'addbasketproduct',
                        {quantity: 1, basket: 1, product: productId},
                        'Dodano do koszyka'
                        )
                }
            ></i>
            <i
                className="icon icon-heart-empty"
                onClick={
                    () => handleAdd(
                        'addfavoriteproduct',
                        {product: productId, favorite: 1},
                        'Dodano do ulubionych'
                    )
                }
            ></i>
        </div>

    )
}

export default ProductIcons;