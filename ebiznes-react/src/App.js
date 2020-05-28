import React, {useContext} from 'react';
import './style/main.css';
import './style/menu.css';
import './img/fontello/css/fontello.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Home from "./pages/Home";
import Products from "./pages/Products";
import Product from "./pages/Product";
import Favorite from "./pages/Favorite";
import Basket from "./pages/Basket";
import Order from "./pages/Order";
import NavBar from "./components/NavBar"
import Auth from "./components/Auth";
import UserProvider from "./providers/UserProvider";

function App() {
  return (
    <UserProvider>
      <Router>
        <div>
          <NavBar />
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
