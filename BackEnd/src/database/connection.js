/**
 * Berfungsi untuk mengkonfigurasi koneksi yang dilakukan dalam hal ini Database PostgreSQL di akun Azure
 */
const {
    Client
} = require('pg');

const db = new Client({
    user: 'melchior_sbd',
    host: 'melchior-sbd.postgres.database.azure.com',
    database: 'spaceapp',
    password: 'H4s1bu4n',
    port: 5432,
    sslmode: 'require',
    ssl: true
});

db.connect((err) => {
    if (err) {
        console.log(err);
    } else {
        console.log('Connected to the todo database');
    }
});

module.exports = db;