class SsgController {
    fun build(rq: Rq) {
        println("게시판 상세페이지 생성 시작")
        makeArticleDetailPages()
        println("게시판 상세페이지 생성 끝")

        println("게시판 리스트페이지 생성 시작")
        makeArticleListPages()
        println("게시판 리스트페이지 생성 끝")
    }

    private fun makeArticleListPages() {
        val boards = boardRepository.getBoards()

        for (board in boards) {
            makeArticleListPage(board)
        }
    }

    private fun makeArticleListPage(board: Board) {
        val articles = articleRepository.getArticles()
        var fileContent = """
                <meta charset="UTF-8">
            """.trimIndent() + "\n"

        fileContent += "<h1>${board.name} 게시물 리스트</h1>\n"

        for (article in articles) {
            if (article.boardId == board.id) {
                fileContent += "<div><a href=\"article_detail_1.html\">${article.id}번 / 제목 : ${article.title}</a></div>\n"
            }
        }
        val fileName = "article_list_${board.name}.html"
        writeStrFile("ext/${fileName}", fileContent)
        println("${fileName}파일이 생성되었습니다.")

    }

    private fun makeArticleDetailPages() {
        val articles = articleRepository.getArticles()
        for (article in articles) {
            var fileContent = """
                <meta charset="UTF-8">
            """.trimIndent() + "\n"
            val writes = memberRepository.getMemberById(article.memberId)!!
            val board = boardRepository.getBoardById(article.boardId)!!
            fileContent += "<h1>${article.id}번 게시물</h1>\n"
            fileContent += "<div>게시판 : ${board.name}</div>\n"
            fileContent += "<div>번호 : ${article.id}</div>\n"
            fileContent += "<div>제목 : ${article.title}</div>\n"
            fileContent += "<div>내용 : ${article.body}</div>\n"
            fileContent += "<div>작성자 : ${writes.nickName}</div>\n"
            fileContent += "<div>등록날짜 : ${article.regDate}</div>\n"
            fileContent += "<div>갱신날짜 : ${article.updateDate}</div>\n"

            fileContent += "<div><a href=\"#\" onclick=\"history.back();\">뒤로가기</a></div>\n"


            val fileName = "article_detail_${article.id}.html"
            writeStrFile("ext/${fileName}", fileContent)
            println("${fileName}파일이 생성되었습니다.")
        }
    }
}