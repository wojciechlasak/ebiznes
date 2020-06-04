import React, {useContext, useState} from "react";
import {getRequestInit, getUrl} from "../utils/request";
import {UserContext} from "../providers/UserProvider";
import {BasketContext} from "../providers/BasketProvider";
import {FavoriteContext} from "../providers/FavoriteProvider";

const ProductIcons = ({ productId }) => {
    const [tooltip, setTooltip] = useState("")
    const {user} = useContext(UserContext);
    const {basket} = useContext(BasketContext);
    const {favorite} = useContext(FavoriteContext);

    const handleAdd = (url, body, text) => {
        if(user){
            fetch(
                getUrl(url),
                getRequestInit({
                    method: 'POST',
                    body: JSON.stringify(body),
                })
            )
                .then(() => {
                    setTooltip(text)
                    setTimeout(() => {
                        setTooltip("")
                    },2000)
                })
        } else {
            setTooltip("Musisz się zalogować")
            setTimeout(() => {
                setTooltip("")
            },2000)
        }
    }

    return (
        <div className="rel">
            {tooltip && <div className="abs tooltip-container flex flex-justify-center flex-align-center">{tooltip}</div>}
            <i
                className="icon icon-cart-plus"
                onClick={
                    () => handleAdd(
                        'addbasketproduct',
                        {quantity: 1, basket: basket && basket.id, product: productId},
                        'Dodano do koszyka'
                        )
                }
            ></i>
            <i
                className="icon icon-heart-empty"
                onClick={
                    () => handleAdd(
                        'addfavoriteproduct',
                        {product: productId, favorite: favorite && favorite.id},
                        'Dodano do ulubionych'
                    )
                }
            ></i>
        </div>

    )
}

export default ProductIcons;