import mongodb from 'mongodb';

export default {
   "port":3000,
   
   //"port": process.env.PORT,
    "mongoUrl": "mongodb://localhost:27017/chat-api",
   "bodyLimit": "100kb"
}
