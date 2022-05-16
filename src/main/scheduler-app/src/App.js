import React, {Component} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import './App.css';
import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";
import Logout from "./pages/Logout";
import Header from "./components/Header";
import Error from "./pages/Error";
import './App.css';

class App extends Component {
  render() {
    return (
        <>
          <Header/>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Home/>}/>
              <Route path="/login" element={<Login/>}/>
              <Route path="/register" element={<Register/>}/>
              <Route path="/logout" element={<Logout/>}/>
              <Route path="/error" element={<Error/>}/>
            </Routes>
          </BrowserRouter>
        </>
    );
  }
}

export default App;
