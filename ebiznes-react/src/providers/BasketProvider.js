import React, {createContext, useCallback, useState,} from 'react';

export const BasketContext = createContext([{}, () => {}]);

export default function BasketProvider({ children }) {
    const [basket, setBasket] = useState(() => {
        const basketStorage = window.localStorage.getItem("e-biznes-basket");

        return basketStorage ? JSON.parse(basketStorage) : null;
    });

    const handleSetBasket = useCallback(
        (basket) => {
            if (basket) {
                window.localStorage.setItem("e-biznes-basket", JSON.stringify(basket));
            } else {
                window.localStorage.removeItem("e-biznes-basket");
            }

            setBasket(basket);
        },
        [setBasket]
    );

    return (
        <BasketContext.Provider value={{ basket, setBasket: handleSetBasket}}>
            {children}
        </BasketContext.Provider>
    );
}