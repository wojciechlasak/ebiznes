import {getRequestInit, getUrl} from "../utils/request";


export const getBasket = (userId) => {
    return fetch(getUrl(`basket/user/${userId}`), getRequestInit({method: 'GET'}))
        .then(response => response.json())
        .then(data => {
            return data
        })
        .catch((error) => {
            console.log(error)
        })
}

export const checkBasket = (userId) => {
    return fetch(getUrl(`basket/user/${userId}`), getRequestInit({method: 'GET'}))
        .then(response => {
            const result = response.json()
            let isEmpty = Boolean(result.length);
            if(!isEmpty) {
                return result.isOrdered === 0
            } else {
                return isEmpty
            }
        })
        .catch((error) => {
            console.log(error)
        })
}

export const createBasket = (userId) => {
    return fetch(
        getUrl(`addbasket`),
        getRequestInit({
            method: 'POST',
            body: JSON.stringify({user: userId, isOrdered: 0}),
        })
    )
}

export const updateBasket = (basketId,userId) => {
    return fetch(
        getUrl(`updatebasket`),
        getRequestInit({
            method: 'PUT',
            body: JSON.stringify({id: basketId, isOrdered: 1, user: userId}),
        })
    )
}