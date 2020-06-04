import React, {createContext, useCallback, useState,} from 'react';

export const FavoriteContext = createContext([{}, () => {}]);

export default function FavoriteProvider({ children }) {
    const [favorite, setFavorite] = useState(() => {
        const favoriteStorage = window.localStorage.getItem("e-biznes-favorite");

        return favoriteStorage ? JSON.parse(favoriteStorage) : null;
    });

    const handleSetFavorite = useCallback(
        (f) => {
            if (f) {
                window.localStorage.setItem("e-biznes-favorite", JSON.stringify(f));
            } else {
                window.localStorage.removeItem("e-biznes-favorite");
            }

            setFavorite(f);
        },
        [setFavorite]
    );

    return (
        <FavoriteContext.Provider value={{ favorite, setFavorite: handleSetFavorite}}>
            {children}
        </FavoriteContext.Provider>
    );
}