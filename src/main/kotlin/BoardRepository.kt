class BoardRepository {
    val boards = mutableListOf<Board>()
    var lastId = 0
    fun addBoard(memberId: Int, name: String, code: String): Int {
        val id = ++lastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        boards.add(Board(id, regDate, updateDate, name, code, memberId))
        return id
    }

    fun getBoardByName(name: String): Board? {
        for(board in boards){
            if(board.name == name){
                return board
            }
        }
        return null
    }

    fun getBoardByCode(code: String): Board? {
        for(board in boards){
            if(board.code == code){
                return board
            }
        }
        return null
    }

    fun makeTestBoard() {
        addBoard(1,"공지","notice")
        addBoard(1,"자유","free")
    }

    fun getBoardById(id: Int): Board? {
        for(board in boards){
            if(board.id == id){
                return board
            }
        }
        return null
    }

    fun updateBoard(board: Board, name: String, code: String) {
        board.name = name
        board.code = code
        board.regDate = Util.getNowDateStr()
    }

    fun remove(board: Board) {
        boards.remove(board)
    }
}