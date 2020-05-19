import React from 'react';
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

function App() {
  return (
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
          </ul>
        </nav>

        <Switch>
          <Route path="/basket" component={Basket} ></Route>
          <Route path="/favorite" component={Favorite} ></Route>
          <Route path="/products" component={Products} />
          <Route path='/product/:prodId' component={Product} />
          <Route path='/order/:baskId' component={Order} />
          <Route path="/" component={Home}></Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
