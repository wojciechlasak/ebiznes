import {getRequestInit, getUrl} from "../utils/request";


export const getFavorite = (userId) => {
    return fetch(getUrl(`favorite/user/${userId}`), getRequestInit({method: 'GET'}))
        .then(response => response.json())
        .then(data => {
            return data
        })
        .catch((error) => {
            console.log(error)
        })
}

export const checkFavorite = (userId) => {
    return fetch(getUrl(`favorite/user/${userId}`), getRequestInit({method: 'GET'}))
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

export const createFavorite = (userId) => {
    return fetch(
        getUrl(`addfavorite`),
        getRequestInit({
            method: 'POST',
            body: JSON.stringify({user: userId}),
        })
    )
}