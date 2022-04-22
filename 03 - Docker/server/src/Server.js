const express = require('express');

let PORT = 3001;
let app = express();

// app.listen(PORT);

//Reloades client on source changes
require("./reloader")(app, PORT);

//Use Endpoints in these files
require("./routes/middleware")(app);
require("./routes/routes")(app);




