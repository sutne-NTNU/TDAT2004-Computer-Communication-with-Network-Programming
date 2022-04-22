const backend = require('../backend');

module.exports = function (app)
{
	app.post('/c', (req, res) =>
	{
		backend.compileAndRun_C(req.body.code).then(output => res.status(200).send(output));
	});

	app.post('/python', (req, res) =>
	{
		backend.compileAndRun_python(req.body.code).then(output => res.status(200).send(output));
	});

	app.post('/java', (req, res) =>
	{
		backend.compileAndRun_java(req.body.code).then(output => res.status(200).send(output));
	});

	app.post('/rust', (req, res) =>
	{
		backend.compileAndRun_rust(req.body.code).then(output => res.status(200).send(output));
	});
};