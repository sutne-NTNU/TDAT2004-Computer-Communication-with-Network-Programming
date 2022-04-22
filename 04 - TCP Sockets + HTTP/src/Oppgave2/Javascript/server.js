const express = require("express");

let app = express();

app.listen(8080);

app.get('/', (req, res) =>
{
	console.log(req.headers);
	res.status(200).send("<h1>VELKOMMEN</h1>" +
		"Headeren din var:" +
		"<ul>" +
		"<li>host: " + req.header("host") + "</li>" +
		"<li>connection: " + req.header("connection") + "</li>" +
		"<li>cache-control: " + req.header("cache-control") + "</li>" +
		"<li>upgrade-insecure-request: " + req.header("upgrade-insecure-request") + "</li>" +
		"<li>user-agent: " + req.header("user-agent") + "</li>" +
		"<li>sec-fetch-dest: " + req.header("sec-fetch-dest") + "</li>" +
		"<li>accept: " + req.header("accept") + "</li>" +
		"<li>sec-fethc-site: " + req.header("sec-fetch-site") + "</li>" +
		"<li>sec-fetch-mode: " + req.header("sec-fetch-mode") + "</li>" +
		"<li>sec-fetch-user: " + req.header("sec-fetch-user") + "</li>" +
		"<li>accept-encoding: " + req.header("accept-encoding") + "</li>" +
		"<li>accept-language: " + req.header("accept-language") + "</li>" +
		"<li>cookie: " + req.header("cookie") + "</li>" +
		"<li>id-none-match: " + req.header("if-none-match") + "</li>" +
		"</ul>");
});