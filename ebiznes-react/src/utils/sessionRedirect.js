import React from "react";
import { getSession } from "../services/AuthService";
import { Redirect } from "react-router-dom";

export const sessionRedirect = (component) => (
    getSession() ? (
        component
    ) : (
        <Redirect to="/"/>
    )
)