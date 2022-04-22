// @flow

import express from 'express';
import path from 'path';

let app = express();

app.use(express.static(path.join(__dirname, '../public')));

// The listen promise can be used to wait for the web server to start (for instance in your tests)
export let appListen = new Promise<void>((resolve, reject) => {
  app.listen(3000, (error: ?Error) => {
    if (error) return reject(error.message);

    console.log('Express server started');
    resolve();
  });
});
