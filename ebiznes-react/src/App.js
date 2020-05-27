import React, {useContext} from 'react';
import './style/main.css';
import './style/menu.css';
import './img/fontello/css/fontello.css';
import { BrowserRouter as Router, Switch, Route, NavLink } from 'react-router-dom';

import Home from "./pages/Home";
import Products from "./pages/Products";
import Product from "./pages/Product";
import Favorite from "./pages/Favorite";
import Basket from "./pages/Basket";
import Order from "./pages/Order";
import Auth from "./components/Auth";
import SocialLoginButton from "./components/SocialLoginButton";
import UserProvider from "./providers/UserProvider";
import { signOut } from "./services/AuthService";
import {UserContext} from "./providers/UserProvider";

function App() {
  const {user, setUser} = useContext(UserContext);

  return (
    <UserProvider>
      <Router>
        <div>
          <nav className="menu flex column">
            <ul className="flex flex-justify-center flex-align-center">
              <li>
                <NavLink exact={true} activeClassName="is-active" to="/">Home</NavLink>
              </li>
              <li>
                <NavLink activeClassName="is-active" to="/products">Products</NavLink>
              </li>
              <li>
                <NavLink activeClassName="is-active" to="/basket"><i className="icon icon-basket"></i></NavLink>
              </li>
              <li>
                <NavLink activeClassName="is-active" to="/favorite"><i className="icon icon-heart-empty"></i></NavLink>
              </li>
              {
                user ? (
                    <button
                        className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                        type="button"
                        onClick={signOut}
                    >
                      Logout
                    </button>
                ) : (
                    <>
                      <SocialLoginButton provider={"google"} title={"Login with Google"}/>
                      <SocialLoginButton provider={"github"} title={"Login with GitHub"}/>
                    </>
                )
              }
            </ul>
          </nav>

          <Switch>
            <Route path="/basket"  ><Basket /></Route>
            <Route path="/favorite" component={Favorite} />
            <Route path="/products" component={Products} />
            <Route path='/product/:prodId' component={Product} />
            <Route path='/order/:baskId' component={Order} />
            <Route path={"/auth/:provider"} component={Auth}/>
            <Route path="/" component={Home} />
          </Switch>
        </div>
      </Router>
    </UserProvider>
  );
}

export default App;
