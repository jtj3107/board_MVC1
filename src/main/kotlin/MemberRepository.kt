class MemberRepository {
    fun addMember(
        loginId: String,
        loginPw: String,
        name: String,
        nickName: String,
        cellPhoneNo: String,
        email: String
    ): Int {
        val id = getLastId() + 1
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val member = Member(id, regDate, updateDate, loginId, loginPw, name, nickName, cellPhoneNo, email)

        writeStrFile("data/member/${member.id}.json", member.toJson())
        setLastId(id)
        return id
    }

    private fun getLastId(): Int {
        val lastId = readIntFromFile("data/member/lastId.txt", 0)

        return lastId
    }

    private fun setLastId(newLastId: Int) {
        writeIntFile("data/member/lastId.txt", newLastId)
    }

    fun getMemberByLoginId(loginId: String): Member? {
        val lastId = getLastId()

        for(id in 1.. lastId){
            val member = memberFromFile("data/member/$id.json")

            if(member != null){
                if(member.loginId == loginId){
                    return member
                }
            }
        }
        return null
    }

    fun makeTestMember() {
        for (i in 1..9) {
            addMember("user${i}", "user${i}", "user${i}", "사용자${i}", "010123123${i}", "user${i}@gmail.com")
        }
    }

    fun getMemberById(id: Int): Member? {
        val member = memberFromFile("data/member/$id.json")
        return member
    }

    private fun memberFromFile(jsonFilePath: String): Member? {
        val jsonStr = readStrFromFile(jsonFilePath) // jsonStr변수에 입력받은 jsonFilePath변수를 readStrFromFile 입력하여 해당 파일을 읽어온다

        if (jsonStr == "") { // 만약 읽어온 파일이 없다면("")
            return null // null을 리턴한다
        }
        val map = mapFromJson(jsonStr) // 읽어온 파일을 mapFromJson함수(json파일을 map형식으로 변환하는 함수)에 입력하여 변수 map에 저장

        // Article calss 변수에 맞게 해당 value값을 Int,String등으로 변환 해준다
        // ex) id:Int, regDate:String, title: String...
        val id = map["id"].toString().toInt() // id값은 Int형이므로 toInt()함수를 사용하여 변환해준다
        val regDate = map["regDate"].toString()
        var updateDate = map["updateDate"].toString()
        val loginId = map["loginId"].toString()
        val loginPw = map["loginPw"].toString()
        val name = map["name"].toString()
        val nickName = map["nickName"].toString()
        val cellPhoneNo = map["cellPhoneNo"].toString()
        val email= map["email"].toString()

        return Member(id, regDate, updateDate, loginId, loginPw, name, nickName, cellPhoneNo, email) // 변환한 값을 Member에 담아 리턴에 해준다
    }
}