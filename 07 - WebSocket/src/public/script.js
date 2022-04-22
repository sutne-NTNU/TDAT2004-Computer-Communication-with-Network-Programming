/*
Canvas
 */
let canvas = document.getElementById('paint');
canvas.width = 690;
canvas.height = 420;
let ctx = canvas.getContext('2d');
let sketch = document.getElementById('sketch');
getComputedStyle(sketch);
let mouse = {x: 0, y: 0};


// Drawing
ctx.lineJoin = 'round';
ctx.lineCap = 'round';
function getColor(c) {colour = c;}
document.getElementById("slider").oninput = function ()
{
	width = this.value;
};

let width = 1;
let colour = 'black';
let x1 = 0;
let y1 = 0;
//sends all infomration about what to draw for all clients
const send = () =>
{
	ws.send(JSON.stringify(
		{
			'width': width,
			'colour': colour,
			'x1': x1,
			'y1': y1,
			'x2': mouse.x,
			'y2': mouse.y
		}));
	//update location of mouse
	x1 = mouse.x;
	y1 = mouse.y;
};

/*
Mousemovement
 */
//keeps track of where the mouse is over the canvas
canvas.addEventListener('mousemove', function (e)
{
	mouse.x = e.pageX - this.offsetLeft;
	mouse.y = e.pageY - this.offsetTop;
}, false);
//when mouse is pressed down
canvas.addEventListener('mousedown', function (e)
{
	x1 = mouse.x;
	y1 = mouse.y;
	//when moving the mouse, use the paint function
	canvas.addEventListener('mousemove', send, false);
}, false);

//when realeasing mouse
document.addEventListener('mouseup', function ()
{
	//remove eventlistener for painting
	canvas.removeEventListener('mousemove', send, false);
}, false);



/*
Connects to the WebSocket
 */
let ws = new WebSocket('ws://192.168.0.196:3001');
ws.onmessage = message =>
{
	if (message.data[0] !== '{') console.log('Server says: ', message.data);
	else
	{
		let info = JSON.parse(message.data);
		ctx.beginPath();
		ctx.lineWidth = info.width;
		ctx.strokeStyle = info.colour;
		ctx.moveTo(info.x1, info.y1);
		ctx.lineTo(info.x2, info.y2);
		ctx.stroke();
	}
};
ws.onopen = () =>
{
	ws.send("Hello!");
};