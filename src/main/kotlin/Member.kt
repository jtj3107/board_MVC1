data class Member(
    val id: Int,
    val regDate: String,
    val updateDate: String,
    val loginId: String,
    val loginPw: String,
    val name: String,
    val nickName: String,
    val cellPhoneNo: String,
    val email: String
){
    fun toJson(): String {
        var jsonStr = ""
        jsonStr += "{"
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "id":$id """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "regDate":"$regDate" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "updateDate":"$updateDate" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "loginId":"$loginId" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "loginPw":"$loginPw" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "name":"$name" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "nickName":"$nickName" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "cellPhoneNo":"$cellPhoneNo" """.trim() + ","
        jsonStr += "\r\n"
        jsonStr += "\t" + """ "email":"$email" """.trim()
        jsonStr += "\r\n"
        jsonStr += "}"

        return jsonStr
    }
}