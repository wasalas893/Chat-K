package com.example.myapplication.Utilities


const val BASE_URL="http://192.168.173.241:3000/v1/"
const val SOCKET_URL="http://192.168.173.241:3000/"
const val URL_REGISTER="${BASE_URL}account/register"
const val URL_LOGIN="${BASE_URL}account/login"
const val URL_CREATE_USER="${BASE_URL}user/add"
//login
const val URL_GET_USER="${BASE_URL}user/byEmail/"
//chenal
const val URL_GET_CHANNELS="${BASE_URL}channel/"
//all message
const val URL_GET_MESSAGES="${BASE_URL}message/byChannel/"

//Broadcast Constants
const val BROADCAST_USER_DATA_CHANGE="BROADCAST_USER_DATA_CHANGE"
