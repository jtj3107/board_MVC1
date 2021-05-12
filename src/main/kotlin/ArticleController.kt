class ArticleController {
    fun write(rq: Rq) {
        if(loginMember == null){
            println("로그인후 사용가능합니다.")
            return
        }
        println("게시판을 선택해주세요")
        val boards = boardRepository.boards
        for(board in boards){
            println("번호 : ${board.id} / 이름 : ${board.name} / 코드 : ${board.code}")
        }
        val targetId = readLineTrim().toInt()
        val board = boardRepository.getBoardById(targetId)
        if(board == null){
            println("존재하지 않는 게시판입니다.")
            return
        }
        println("${board.id}번 ${board.name}게시판이 선택되었습니다.")
        print("제목 : ")
        val title = readLineTrim()
        print("내용 : ")
        val body = readLineTrim()

        val id = articleRepository.addArticle(board.id, loginMember!!.id, title, body)
        println("${id}번 게시물이 등록 되었습니다.")

    }

    fun list(rq: Rq) {
        val page = rq.getIntParam("page",1)
        val searchKeyword = rq.getStringParam("searchKeyword", "")
        val boardCode = rq.getStringParam("boardCode", "")

        val filteredArticles = articleRepository.filteredArticles(boardCode,searchKeyword, page, 10)

        for(article in filteredArticles){
            val writes = memberRepository.getMemberById(article.memberId)!!
            val board = boardRepository.getBoardById(article.boardId)!!
            println("게시판이름 : ${board.name} / 번호 : ${article.id} / 제목 : ${article.title} / 등록날짜 : ${article.regDate} / 작성자 : ${writes.nickName} ")
        }
    }

    fun detail(rq: Rq) {
        if(loginMember == null){
            println("로그인후 사용가능합니다.")
            return
        }
        val id = rq.getIntParam("id",0)

        if(id <= 0){
            println("id를 입력해주세요")
            return
        }

        val article = articleRepository.getArticleById(id)

        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다." )
            return
        }

        val writes = memberRepository.getMemberById(article.memberId)!!
        println("번호 : ${article.id}")
        println("제목 : ${article.title}")
        println("내용 : ${article.body}")
        println("작성자 : ${writes.nickName}")
        println("등록날짜 : ${article.regDate}")
        println("갱신날짜 : ${article.updateDate}")
    }

    fun delete(rq: Rq) {
        if(loginMember == null){
            println("로그인후 사용가능합니다.")
            return
        }
        val id = rq.getIntParam("id",0)

        if(id <= 0){
            println("id를 입력해주세요")
            return
        }

        val article = articleRepository.getArticleById(id)

        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다." )
            return
        }
        if(loginMember!!.id != article.memberId){
            println("해당 게시물 작성자만 삭제 가능합니다.")
            return
        }
        articleRepository.remove(article)
        println("${id}번 게시물이 삭제 되었습니다.")
    }

    fun modify(rq: Rq) {
        if(loginMember == null){
            println("로그인후 사용가능합니다.")
            return
        }
        val id = rq.getIntParam("id",0)

        if(id <= 0){
            println("id를 입력해주세요")
            return
        }

        val article = articleRepository.getArticleById(id)

        if(article == null){
            println("${id}번 게시물은 존재하지 않습니다." )
            return
        }
        if(loginMember!!.id != article.memberId){
            println("해당 게시물 작성자만 수정 가능합니다.")
            return
        }
        print("새 제목 : ")
        val title = readLineTrim()
        print("새 내용 : ")
        val body = readLineTrim()

        articleRepository.updateArticle(id,title,body)

        println("${id}번 게시물이 수정 되었습니다.")
    }
}