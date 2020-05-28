import React, { createContext, useState, useCallback } from 'react';
import {getRequestInit, getUrl} from "../utils/request";

export const UserContext = createContext({});

export default function UserProvider({ children }) {
    const [user, setUser] = useState(() => {
        const userString = window.localStorage.getItem("e-biznes-user");
        console.log(userString)


        return userString ? JSON.parse(userString) : null;
    });
    const handleSetUser = useCallback(
        (user) => {
            if (user) {
                window.localStorage.setItem("e-biznes-user", JSON.stringify(user));
            } else {
                window.localStorage.removeItem("e-biznes-user");
            }

            setUser(user);
        },
        [setUser]
    );

    return (
        <UserContext.Provider value={{ user, setUser: handleSetUser }}>
            {children}
        </UserContext.Provider>
    );
}
