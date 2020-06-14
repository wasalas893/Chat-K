import mongodb from 'mongodb';

export default {
   "port": 3000,
   "mongoUrl": "mongodb+srv://suna123:suna123@cluster0-dfx9f.mongodb.net/test?retryWrites=true&w=majority",
  // "port": process.env.PORT,
   //"mongoUrl": process.env.MONGODB_URI,
   "bodyLimit": "100kb"
}
