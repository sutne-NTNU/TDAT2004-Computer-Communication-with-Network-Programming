'use strict';

const express = require('express');
const path = require('path');

//send all files in the /public folder to client
let app = express();
app.use('/', express.static(path.join(__dirname, 'public')));
app.get('/', function (req, res) {res.sendFile('/public/index.html', {root: __dirname});});
app.listen(3000, () => console.log('HTTP-server hosted on port: 3000'));


//List that contains all connected clients
let connections = [];

/*
Creating the websocket server
 */
const net = require('net');
const wsServer = net.createServer(connection =>
{
	connection.on('data', data =>
	{
		//if we recieve a GET from a client, we perform the handshake
		if (data.toString().substring(0, 3) === 'GET')
		{
			let encryptedKey = encryptKey(data);
			connection.write('' +
				'HTTP/1.1 101 Switching Protocols\r\n' +
				'Upgrade: websocket\r\n' +
				'Connection: Upgrade\r\n' +
				'Sec-WebSocket-Accept:' + encryptedKey + "\r\n\r\n");
			connections.push(connection);
		}
		else
		{
			//decode the data then send the data to all the connected clients
			broadcast(decode(data));
		}
	});

	connection.on('end', () =>
	{
		console.log('Client disconnected');
		// When a client disconnects (ends the connection) we remove
		// them from the list of connected clients
		let index = connections.indexOf(connection);
		if (index > -1)
		{
			connections.splice(index, 1);
		}
	});

	connection.on('error', (error) =>
	{
		//if an error occurs we log it in the console
		console.error('Error: ', error);
	});
});

/*
Encrypts the key sent from the client
 */
const crypto = require('crypto');
function encryptKey(data)
{
	let dataString = data.toString();
	let sha1 = crypto.createHash('sha1');
	let key = dataString.substring(dataString.indexOf('Key: ') + 5, dataString.indexOf('==') + 2);
	//now that we have the key, we can return the encrypted key back to the client to accept the handshake
	let rfc6455 = '258EAFA5-E914-47DA-95CA-C5AB0DC85B11';
	return sha1.update(key + rfc6455).digest('base64');
}

/*
Converts an incomming message to a string
 */
function decode(message)
{
	let bytes = Buffer.from(message);
	let length = bytes[1] & 127;
	let maskStart = 2;
	let dataStart = maskStart + 4;
	let str = '';
	for (let i = dataStart; i < dataStart + length; i++)
	{
		let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)];
		str += String.fromCharCode(byte);
	}
	return str;
}

/*
Converts an outgoing string message to a buffer
 */
function encode(message)
{
	return Buffer.concat([
		new Buffer.from([
			0x81,
			"0x" +
			(message.length + 0x10000)
				.toString(16)
				.substr(-2)
				.toUpperCase()
		]),
		Buffer.from(message)
	]);
}


//Encodes the message then sends it to all connected clients
const broadcast = (message) =>
{
	connections.forEach(conn => conn.write(encode(message)));
};


wsServer.on('data', data => console.log(data));
wsServer.on('error', error => console.error('Error: ', error));
wsServer.listen(3001, () => console.log('WebSocket-server listening on port: 3001'));