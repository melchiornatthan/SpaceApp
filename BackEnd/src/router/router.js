/**
 * Berfungsi untuk menetapkan route untuk request HTTP
 */
const express = require("express");
const passport = require("passport");
const router = express.Router();
const controller = require("../controllers/controller");

router.post("/register", controller.register);
router.post("/login", passport.authenticate("local"), controller.login);
router.post("/additem", controller.additem);
router.get("/getitems", controller.getitems);
router.post("/searchitem", controller.searchitem);
router.get("/getitemsbyid", controller.getitemsbyid);
router.delete("/deleteitem", controller.deleteitem);
router.post("/transferitem", controller.transferitem);
router.get("/gettransaction", controller.gettransaction);
router.put("/updatetransaction", controller.updatetransaction);
router.put("/updateitem", controller.updateitem);
router.get("/getusers", controller.getusers);

module.exports = router;
