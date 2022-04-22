import {BrowserRouter, Route} from 'react-router-dom';
import ReactDOM from 'react-dom';
import React from 'react';
import App from './app';
import './style/index.css';


ReactDOM.render(
	<BrowserRouter>
		<Route exact path = "/" component = {App}/>
	</BrowserRouter>,
	document.getElementById('root')
);

