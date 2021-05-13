class MemberRepository {
    val members = mutableListOf<Member>()
    var lastId = 0

    fun addMember(loginId: String, loginPw: String, name: String, nickName: String, cellPhoneNo: String, email: String): Int {
        val id = ++lastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        members.add(Member(id, regDate, updateDate, loginId, loginPw, name, nickName, cellPhoneNo, email))
        return id
    }

    fun getMemberByLoginId(loginId: String): Member? {
        for(member in members){
            if(member.loginId == loginId){
                return member
            }
        }
        return null
    }

    fun makeTestMember() {
        for(i in 1.. 9){
            addMember("user${i}", "user${i}","user${i}", "사용자${i}", "010123123${i}" , "user${i}@gmail.com")
        }
    }

    fun getMemberById(id: Int): Member? {
        for(member in members){
            if(member.id == id){
                return member
            }
        }
        return null
    }
}