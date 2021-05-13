class BoardRepository {
    fun getBoards(): List<Board> {
        val boards = mutableListOf<Board>() // 빈 articles객체 생성
        val lastId = getLastId() // lastId 받기
        for (id in 1..lastId) { // 반복문을 통해 1 에서 lastId 까지
            val board = boardFromFile("data/board/$id.json") // 저장된 파일을 article변수에 저장
            if (board != null) { // 만약 article변수에 null(파일없음)이 오면 if문을 빠져나가고
                // 만약 articleFromFile함수에 읽어온 파일이 없으면 null이 들어온다
                boards.add(board) // null이 아닐시 articles에 article을 삽입해준다
            }
        }
        return boards // 빈 articles에 저장된 파일을 삽입하여 리턴해준다
    }
    fun addBoard(memberId: Int, name: String, code: String): Int {
        val id = getLastId() + 1
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val board = Board(id, regDate, updateDate, name, code, memberId)
        writeStrFile("data/board/${board.id}.json", board.toJson())

        setLastId(id)
        return id
    }

    private fun setLastId(newLastId: Int) {
        val lastId = writeIntFile("data/board/lastId.txt", newLastId)
    }

    fun getLastId(): Int {
        val lastId = readIntFromFile("data/board/lastId.txt", 0)

        return lastId
    }

    fun getBoardByName(name: String): Board? {
        val lastId = getLastId()

        for (id in 1..lastId) {
            val board = boardFromFile("data/board/$id.json")

            if (board != null) {
                if (board.name == name) {
                    return board
                }
            }
        }
        return null
    }

    fun getBoardByCode(code: String): Board? {
        val lastId = getLastId()

        for (id in 1..lastId) {
            val board = boardFromFile("data/board/$id.json")

            if (board != null) {
                if (board.code == code) {
                    return board
                }
            }
        }
        return null
    }

    fun makeTestBoard() {
        addBoard(1, "공지", "notice")
        addBoard(1, "자유", "free")
    }

    fun getBoardById(id: Int): Board? {
        val board = boardFromFile("data/board/$id.json")

        return board
    }

    private fun boardFromFile(jsonFilePath: String): Board? {
        val jsonStr = readStrFromFile(jsonFilePath)

        if (jsonStr == "") {
            return null
        }
        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        var updateDate = map["updateDate"].toString()
        val name = map["name"].toString()
        val code = map["code"].toString()
        val memberId = map["memberId"].toString().toInt()

        return Board(id, regDate, updateDate, name, code, memberId)
    }

    fun updateBoard(board: Board, name: String, code: String) {
        board.name = name
        board.code = code
        board.updateDate = Util.getNowDateStr()

        val jsonStr = board.toJson()

        writeStrFile("data/board/${board.id}.json", jsonStr)
    }

    fun remove(board: Board) {
        deleteFile("data/board/${board.id}.json")
    }
}