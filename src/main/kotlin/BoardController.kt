class BoardController {
    fun add(rq: Rq) {
        if(loginMember == null){
            println("권한이 없습니다.")
            return
        }
        if(loginMember!!.id != 1){
            println("권한이 없습니다.")
            return
        }
        print("게시판 이름 : ")
        val name = readLineTrim()
        val boardName = boardRepository.getBoardByName(name)
        if(boardName != null){
            println("`${name}`(은)는 이미 사용중인 게시판 이름입니다.")
            return
        }
        print("게시판 코드 : ")
        val code = readLineTrim()
        val boardCode = boardRepository.getBoardByCode(code)
        if(boardCode != null){
            println("`${code}`(은)는 이미 사용중인 게시판 코드입니다.")
            return
        }
        val id = boardRepository.addBoard(loginMember!!.id,name, code)
        println("${id}번 ${name}게시판이 생성 되었습니다.")
    }

    fun list(rq: Rq) {
        val boards = boardRepository.getBoards()

        for(board in boards){
            println("번호 : ${board.id} / 게시판 이름 : ${board.name} / 게시판 코드 : ${board.code} / 등록날짜 : ${board.regDate}")
        }
    }

    fun modify(rq: Rq) {
        if(loginMember == null){
            println("권한이 없습니다.")
            return
        }
        val id = rq.getIntParam("id",0)
        if(id <= 0){
            println("게시판 `id`를 입력해주세요")
            return
        }
        val board = boardRepository.getBoardById(id)
        if(board == null){
            println("${id}번 게시판은 존재하지 않습니다.")
            return
        }
        if(board.memberId != loginMember!!.id){
            println("게시판 생성자만 수정가능 합니다.")
            return
        }

        print("새로운 게시판 이름 : ")
        val name = readLineTrim()
        val boardName = boardRepository.getBoardByName(name)
        if(boardName != null){
            println("`${name}`(은)는 이미 사용중인 게시판 이름 입니다.")
            return
        }

        print("새로운 게시판 코드 : ")
        val code = readLineTrim()
        val boardCode = boardRepository.getBoardByCode(code)
        if(boardCode != null){
            println("`${code}`(은)는 이미 사용중인 게시판 코드 입니다.")
            return
        }
        boardRepository.updateBoard(board, name, code)
        println("${id}번 게시판이 수정 되었습니다.")
    }

    fun delete(rq: Rq) {
        if(loginMember == null){
            println("권한이 없습니다.")
            return
        }
        val id = rq.getIntParam("id",0)
        if(id <= 0){
            println("게시판 `id`를 입력해주세요")
            return
        }
        val board = boardRepository.getBoardById(id)
        if(board == null){
            println("${id}번 게시판은 존재하지 않습니다.")
            return
        }
        if(board.memberId != loginMember!!.id){
            println("게시판 생성자만 삭제가능 합니다.")
            return
        }

        boardRepository.remove(board)
        println("${id}번 ${board.name}게시판이 삭제되었습니다.")
    }
}