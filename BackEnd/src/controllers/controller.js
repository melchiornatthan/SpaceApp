/**
 * Berfungsi sebagai controller ang dimana jika ada request yang diterima maka akan diproses ke query.js
 */
const query = require("../queries/query");

async function register(req, res) {
  try {
    const result = await query.register(req.body);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function login(req, res) {
  try {
    const result = await query.login(req.body);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function additem(req, res) {
  try {
    const result = await query.additem(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function getitems(req, res) {
  try {
    const result = await query.getitems(req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function searchitem(req, res) {
  try {
    const result = await query.searchitem(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function getitemsbyid(req, res) {
  try {
    const result = await query.getitemsbyid(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function deleteitem(req, res) {
  try {
    const result = await query.deleteitem(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function transferitem(req, res) {
  try {
    const result = await query.transferitem(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function gettransaction(req, res) {
  try {
    const result = await query.gettransaction(req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function updatetransaction(req, res) {
  try {
    const result = await query.updatetransaction(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function updateitem(req, res) {
  try {
    const result = await query.updateitem(req.body, req.user);
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

async function getusers(req, res) {
  try {
    const result = await query.getusers();
    res.json(result);
  } catch (err) {
    res.json(err);
  }
}

module.exports = {
  register,
  login,
  additem,
  getitems,
  deleteitem,
  searchitem,
  getitemsbyid,
  transferitem,
  gettransaction,
  updatetransaction,
  getusers,
  updateitem,
};
