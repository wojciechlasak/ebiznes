import React, { createContext, useState, useCallback } from 'react';

export const UserContext = createContext({});

export default function UserProvider({ children }) {
    const [user, setUser] = useState(() => {
        const userString = window.localStorage.getItem("e-biznes-user");

        return userString ? JSON.parse(userString) : null;
    });
    const handleSetUser = useCallback(
        (u) => {
            if (u) {
                window.localStorage.setItem("e-biznes-user", JSON.stringify(u));
            } else {
                window.localStorage.removeItem("e-biznes-user");
            }

            setUser(u);
        },
        [setUser]
    );

    return (
        <UserContext.Provider value={{ user, setUser: handleSetUser }}>
            {children}
        </UserContext.Provider>
    );
}
