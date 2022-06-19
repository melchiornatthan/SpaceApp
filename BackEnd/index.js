/**
 * Sebagai titik awal sisi server akan dijalankan
 */
const express = require("express");
const bodyParser = require("body-parser");
const port = process.env.PORT || 3333;
const app = express();
const session = require("express-session");
const cors = require("cors");
const passport = require("passport");
const local = require("./src/middlewares/local");
const store = new session.MemoryStore();
const todoRoutes = require("./src/router/router");

const corsOptions = {
  origin: "*",
  Credentials: true,
  optionsSuccessStatus: 200,
};

app.use(
  session({
    secret: "secret",
    resave: false,
    cookie: {
      maxAge: 6000000000,
    },
    saveUninitialized: false,
    store,
  })
);

app.use(passport.initialize());
app.use(passport.session());

app.use(cors(corsOptions));
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);

app.get("/", (req, res) => {
  if (req.user) {
    res.send(req.user);
  } else {
    res.send("Not logged in");
  }
});

app.use("/spaceapp", todoRoutes);

app.listen(port, () => {
  console.log("Server is running on port: " + port);
});