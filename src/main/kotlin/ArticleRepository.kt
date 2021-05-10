class ArticleRepository {
    val articles = mutableListOf<Article>()
    var lastId = 0
    fun addArticle(boardId: Int,memberId: Int,title: String, body: String): Int {
        val id = ++lastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        articles.add(Article(id, regDate, updateDate, title, body, memberId, boardId))
        return id
    }

    fun makeTestArticle() {
        for (i in 1..25) {
            addArticle(i%2 +1,i %9 +1,"제목${i}", "내용${i}")
        }
    }

    fun filteredArticles(boardCode : String, searchKeyword: String, page: Int, pageCount: Int): List<Article> {
        val filtered1Articles = filteredSearchKeywordArticles(boardCode,articles, searchKeyword)
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

    private fun filteredSearchKeywordArticles(boardCode: String,articles: MutableList<Article>, searchKeyword: String): List<Article> {
        if(boardCode.isEmpty() && boardCode.isEmpty()){
            return articles
        }
        val filteredArticles = mutableListOf<Article>()
        var boardId = if(boardCode.isEmpty()){
            0
        }else {
           boardRepository.getBoardByCode(boardCode)!!.id
        }
        for(article in articles){
            if(boardId != 0){
                if(article.boardId != boardId){
                    continue
                }
            }
            if(searchKeyword.isNotEmpty()){
                if(!article.title.contains(searchKeyword)){
                    continue
                }
            }
            filteredArticles.add(article)
        }
        return filteredArticles
    }

    fun getArticleById(id: Int): Article? {
        for(article in articles){
            if(article.id == id){
                return article
            }
        }
        return null
    }

    fun remove(article: Article) {
        articles.remove(article)
    }

    fun updateArticle(title: String, body: String, article: Article) {
        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()
    }
}