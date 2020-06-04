import React, {createContext, useCallback, useState,} from 'react';

export const BasketContext = createContext([{}, () => {}]);

export default function BasketProvider({ children }) {
    const [basket, setBasket] = useState(() => {
        const basketStorage = window.localStorage.getItem("e-biznes-basket");

        return basketStorage ? JSON.parse(basketStorage) : null;
    });

    const handleSetBasket = useCallback(
        (b) => {
            if (b) {
                window.localStorage.setItem("e-biznes-basket", JSON.stringify(b));
            } else {
                window.localStorage.removeItem("e-biznes-basket");
            }

            setBasket(b);
        },
        [setBasket]
    );

    return (
        <BasketContext.Provider value={{ basket, setBasket: handleSetBasket}}>
            {children}
        </BasketContext.Provider>
    );
}