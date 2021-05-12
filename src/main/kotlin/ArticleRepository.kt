class ArticleRepository {
    fun getArticles() : List<Article>{
        val articles = mutableListOf<Article>()

        val lastId = getLastId()

        for(id in 1.. lastId){
            val article = articleFromFile("data/article/$id.json")

            if(article != null){
                articles.add(article)
            }

        }
        return articles
    }
    fun articleFromFile(jsonFilePath : String) : Article?{
        val jsonStr = readStrFromFile(jsonFilePath)

        if (jsonStr == ""){
            return null
        }
        val map = mapFromJson(jsonStr)

        val id = map["id"].toString().toInt()
        val regDate = map["regDate"].toString()
        var updateDate = map["updateDate"].toString()
        var title = map["title"].toString()
        var body = map["body"].toString()
        val memberId = map["memberId"].toString().toInt()
        val boardId = map["boardId"].toString().toInt()

        return Article(id,regDate, updateDate, title, body, memberId, boardId)
    }
    fun getLastId() : Int{
        val lastId = readIntFromFile("data/article/lastId.txt")

        return lastId
    }
    fun setLastId(newLastId:Int) {
        writeIntFile("data/article/lastId.txt", newLastId)
    }

    fun addArticle(boardId: Int, memberId: Int, title: String, body: String): Int {
        val id = getLastId() +1
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val article = Article(id, regDate, updateDate, title, body, memberId, boardId)
        val jsonStr = article.toJson()

        writeStrFile("data/article/${article.id}.json",jsonStr)

        setLastId(id)
        return id
    }

    fun makeTestArticle() {
        /*
        for (i in 1..25) {
            addArticle(i%2 +1,i %9 +1,"제목${i}", "내용${i}")
        }
         */
    }

    fun filteredArticles(boardCode: String, searchKeyword: String, page: Int, pageCount: Int): List<Article> {
        val filtered1Articles = filteredSearchKeywordArticles(boardCode, getArticles(), searchKeyword)
        val filtered2Articles = filteredPageArticles(filtered1Articles, page, pageCount)

        return filtered2Articles
    }

    private fun filteredPageArticles(filtered1Articles: List<Article>, page: Int, pageCount: Int): List<Article> {
        val filteredArticles = mutableListOf<Article>()

        val fromIndex = (page - 1) * pageCount
        val startIndex = filtered1Articles.lastIndex - fromIndex
        var endIndex = startIndex - pageCount + 1
        if (endIndex < 0) {
            endIndex = 0
        }
        for (i in startIndex downTo endIndex) {
            filteredArticles.add(filtered1Articles[i])
        }
        return filteredArticles
    }

    private fun filteredSearchKeywordArticles(
        boardCode: String,
        articles: List<Article>,
        searchKeyword: String
    ): List<Article> {
        if (boardCode.isEmpty() && boardCode.isEmpty()) {
            return articles
        }
        val filteredArticles = mutableListOf<Article>()
        var boardId = if (boardCode.isEmpty()) {
            0
        } else {
            boardRepository.getBoardByCode(boardCode)!!.id
        }
        for (article in articles) {
            if (boardId != 0) {
                if (article.boardId != boardId) {
                    continue
                }
            }
            if (searchKeyword.isNotEmpty()) {
                if (!article.title.contains(searchKeyword)) {
                    continue
                }
            }
            filteredArticles.add(article)
        }
        return filteredArticles
    }

    fun getArticleById(id: Int): Article? {
        val article = articleFromFile("data/article/$id.json")
        return article
    }

    fun remove(article: Article) {
        deleteFile("data/article/${article.id}.json")
    }

    fun updateArticle(id:Int,title: String, body: String) {
        val article = getArticleById(id)!!

        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()

        // 파일에서 직접 수정
        val jsonStr = article.toJson()

        writeStrFile("data/article/${article.id}.json",jsonStr)
    }
}