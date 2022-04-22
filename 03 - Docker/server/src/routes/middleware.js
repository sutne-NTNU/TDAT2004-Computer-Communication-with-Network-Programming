const express = require('express');
const path = require('path');
const cors = require('cors');
const public_path = path.join(__dirname, '/../../client/src');

module.exports = function (app)
{
	app.use(express.static(public_path));
	app.use(express.json());
	app.use(cors({origin: true}));
};