const path = require('path');
const reload = require('reload');
const fs = require('fs');
const public_path = path.join(__dirname, '../../client/src');

module.exports = function (app, PORT)
{
	return new Promise((resolve, reject) =>
	{
		reload(app).then(reloader => {
			app.listen(PORT, (error) => {
				if (error) reject(error.message);
				console.log('Server running...');
				reloader.reload();
				fs.watch(public_path, () => reloader.reload(app));
				resolve();
			});
		});
	});
};