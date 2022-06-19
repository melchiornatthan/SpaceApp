/**
 * Berfungsi untuk mengirim query ke database
 */

const db = require("../database/connection");
const bcrypt = require("bcrypt");

//melakukan register akun ke table user
async function register(body) {
  const { username, password } = body;
  const hashedPassword = await bcrypt.hash(password, 10);
  const query = `INSERT INTO users (username, password) VALUES ('${username}', '${hashedPassword}')`;
  const result = await db.query(query);
  if (result.rowCount === 1) {
    return {
      message: "User created successfully",
    };
  } else {
    throw new Error("Error registering user");
  }
}

//melakukan login 
async function login(body) {
  const { username, password } = body;
  const query = `SELECT * FROM users WHERE username = '${username}'`;
  const result = await db.query(query);
  if (result.rowCount === 1) {
    const user = result.rows[0];
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (isPasswordValid) {
      return {
        message: "User logged in successfully",
        user,
      };
    } else {
      throw new Error("Invalid password");
    }
  } else {
    throw new Error("Invalid username");
  }
}

//menambahkan item ke table items
async function additem(body, user) {
  const { name, quantity, description } = body;
  const { id } = user;
  const query = `INSERT INTO items (name, quantity, description, userid) VALUES ('${name}', '${quantity}', '${description}', '${id}')`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: `item added successfully`,
    };
  } else {
    throw new Error("Error adding item");
  }
}

//mengambil item dari table
async function getitems(user) {
  const { id } = user;
  const query = `SELECT * FROM items WHERE userid = '${id}' ORDER BY	name ASC;`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: "Item added successfully",
      list: result.rows,
    };
  } else {
    throw new Error("Error adding item");
  }
}

//melakukan pencarian item pada table
async function searchitem(body, user) {
  const { key } = body;
  const { id } = user;
  const query = `SELECT * FROM items WHERE userid = '${id}' AND name ILIKE '%${key}%'`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: "Item added successfully",
      list: result.rows,
    };
  } else {
    return {
      message: "Not Found",
    };
  }
}

//melakukan pengambilan item berdasarkan id
async function getitemsbyid(body, user) {
  const { itemid } = body;
  const { id } = user;
  const query = `SELECT * FROM items WHERE userid = '${id}' AND id = '${itemid}'`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: "Item added successfully",
      list: result.rows,
    };
  } else {
    throw new Error("Error adding item");
  }
}

//menghapus item dari table
async function deleteitem(body, user) {
  const { itemid } = body;
  const { id } = user;
  const query = `DELETE FROM items WHERE id = '${itemid}' AND userid = '${id}'`;
  const result = await db.query(query);
  if (result.rowCount === 1) {
    return {
      message: "Item deleted successfully",
    };
  } else {
    throw new Error("Error deleting item");
  }
}

//menambahkan transaksi ke table transactions
async function transferitem(body, user) {
  const { itemid, receiverid, quantity } = body;
  const { id } = user;
  const query = `INSERT into transactions (itemid, senderid, receiverid, quant_sent) VALUES ('${itemid}', '${id}', '${receiverid}', '${quantity}')`;
  const result = await db.query(query);
  if (result.rowCount === 1) {
    const query2 = `UPDATE items SET quantity = quantity - '${quantity}' WHERE id = '${itemid}' AND userid = '${id}'`;
    const result2 = await db.query(query2);
    if (result2.rowCount === 1) {
      return {
        message: "Item transferred successfully",
      };
    } else {
      throw new Error("Error transferring item");
    }
  } else {
    throw new Error("Error transferring item");
  }
}

//memperbaharui item dari table
async function updateitem(body, user) {
  const { itemid, name, quantity, description } = body;
  const { id } = user;
  const query = `UPDATE items SET name = '${name}', quantity = '${quantity}', description = '${description}' WHERE id = '${itemid}' AND userid = '${id}'`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: `Item updated successfully`,
    };
  } else {
    throw new Error("Error updating item");
  }
}

//mengambil transaksi dari table
async function gettransaction(user) {
  const { id } = user;
  const query = `SELECT transactions.id, users.username, quant_sent, items.name, status FROM transactions INNER JOIN users on transactions.receiverid = users.id INNER JOIN items on transactions.itemid = items.id WHERE transactions.senderid = '${id}' OR transactions.receiverid = '${id}'ORDER BY	id DESC;`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: "Item added successfully",
      list: result.rows,
    };
  } else {
    throw new Error("Error adding item");
  }
}

//mengupdate transaksi yang ada
async function updatetransaction(body, user) {
  const { transactionid } = body;
  const { id } = user;
  const query = `UPDATE transactions SET status = 'accepted' WHERE id = '${transactionid}' AND  receiverid = '${id}'`;
  const result = await db.query(query);
  if (result.rowCount === 1) {
    const query2 = `INSERT into items (name, quantity, description, userid) VALUES ((select name from items where id = (select itemid from transactions where id = '${transactionid}')), (select quant_sent from transactions where id =  '${transactionid}'), (select description from items where id = (select itemid from transactions where id = '${transactionid}')), '${id}')`;
    const result2 = await db.query(query2);
    if (result2.rowCount === 1) {
      return {
        message: "Transaction updated successfully",
      };
    } else {
      throw new Error("Error adding item");
    }
  } else {
    return {
      message: "Not Authorized",
    };
  }
}

//mengambil data user dari table
async function getusers() {
  const query = `SELECT id, username FROM users`;
  const result = await db.query(query);
  if (result.rowCount > 0) {
    return {
      message: "Item added successfully",
      list: result.rows,
    };
  } else {
    throw new Error("Error adding item");
  }
}

module.exports = {
  register,
  login,
  additem,
  getitems,
  searchitem,
  deleteitem,
  getitemsbyid,
  transferitem,
  gettransaction,
  updatetransaction,
  getusers,
  updateitem,
};
