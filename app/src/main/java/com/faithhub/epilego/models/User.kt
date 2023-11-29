package com.faithhub.epilego.models

import com.faithhub.epilego.other.Internet

data class User(
    var usrIDNum: String? = "",
    var usrNum: String? = "",
    var usrname: String? = "",
    var fullName: String? = "Add full name",
    var denom: String? = "Add religious denomination",
    var img: String? = "",
    var location: String? = "Add location",
    var logDevice: String? = Internet.userDevice,
    var acctBal: String? = "",
    var payMeth: String? = "",
    var momoNet: String? = "",
    var momoNum: String? = "Add payment number",
    var rcptMail: String? = Internet.defMail,
)