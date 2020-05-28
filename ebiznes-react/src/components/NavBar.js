import {NavLink} from "react-router-dom";
import {signOut} from "../services/AuthService";
import SocialLoginButton from "./SocialLoginButton";
import React, {useContext} from "react";
import {UserContext} from "../providers/UserProvider";

const NavBar = () => {
    const {user, setUser} = useContext(UserContext);

    return (
        <nav className="menu flex column">
            <ul className="flex flex-justify-center flex-align-center">
                <li>
                    <NavLink exact={true} activeClassName="is-active" to="/">Home</NavLink>
                </li>
                <li>
                    <NavLink activeClassName="is-active" to="/products">Products</NavLink>
                </li>

                {
                    user ? (
                        <>
                            <li>
                                <NavLink activeClassName="is-active" to="/basket"><i
                                    className="icon icon-basket"></i></NavLink>
                            </li>
                            <li>
                                <NavLink activeClassName="is-active" to="/favorite"><i
                                    className="icon icon-heart-empty"></i></NavLink>
                            </li>
                            <button
                                className="button-base button-red"
                                type="button"
                                onClick={() => {signOut(user); setUser(null)}}
                            >
                                Logout
                            </button>
                        </>
                    ) : (
                        <>
                            <SocialLoginButton provider={"google"} title={"Login with Google"}/>
                            <SocialLoginButton provider={"facebook"} title={"Login with Facebook"}/>
                        </>
                    )
                }
            </ul>
        </nav>
    )
}

export default NavBar;