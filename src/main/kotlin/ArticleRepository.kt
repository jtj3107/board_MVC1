class ArticleRepository {
    fun getArticles() : List<Article>{ // articles를 저장된 파일에서 불러오기 위하여 사용하는 함수
        val articles = mutableListOf<Article>() // 빈 articles객체 생성

        val lastId = getLastId() // lastId 받기

        for(id in 1.. lastId){ // 반복문을 통해 1 에서 lastId 까지
            val article = articleFromFile("data/article/$id.json") // 저장된 파일을 article변수에 저장

            if(article != null){ // 만약 article변수에 null(파일없음)이 오면 if문을 빠져나가고
                // 만약 articleFromFile함수에 읽어온 파일이 없으면 null이 들어온다
                articles.add(article) // null이 아닐시 articles에 article을 삽입해준다
            }

        }
        return articles // 빈 articles에 저장된 파일을 삽입하여 리턴해준다
    }
    fun articleFromFile(jsonFilePath : String) : Article?{ // 저장된 파일(json형식)을 Article형식으로 변환해주는 함수
        val jsonStr = readStrFromFile(jsonFilePath) // jsonStr변수에 입력받은 jsonFilePath변수를 readStrFromFile 입력하여 해당 파일을 읽어온다

        if (jsonStr == ""){ // 만약 읽어온 파일이 없다면("")
            return null // null을 리턴한다
        }
        val map = mapFromJson(jsonStr) // 읽어온 파일을 mapFromJson함수(json파일을 map형식으로 변환하는 함수)에 입력하여 변수 map에 저장

        // Article calss 변수에 맞게 해당 value값을 Int,String등으로 변환 해준다
        // ex) id:Int, regDate:String, title: String...
        val id = map["id"].toString().toInt() // id값은 Int형이므로 toInt()함수를 사용하여 변환해준다
        val regDate = map["regDate"].toString()
        var updateDate = map["updateDate"].toString()
        var title = map["title"].toString()
        var body = map["body"].toString()
        val memberId = map["memberId"].toString().toInt()
        val boardId = map["boardId"].toString().toInt()

        return Article(id,regDate, updateDate, title, body, memberId, boardId) // 변환한 값을 Article에 담아 리턴에 해준다
    }
    fun getLastId() : Int{ // 마지막 번호를 리턴해주는 함수
        val lastId = readIntFromFile("data/article/lastId.txt", 0) // 파일에 저장된 값를 찾아 lastId에 저장해준다

        return lastId // 저장된 값 리턴
    }
    fun setLastId(newLastId:Int) { // 파일 생성후 저장 할시 번호를 변경해주는 함수
        writeIntFile("data/article/lastId.txt", newLastId) // 해당 위치의 파일을 받은 newLastId로 변경 후 저장
    }

    fun writeArticle(boardId: Int, memberId: Int, title: String, body: String): Int {
        val id = getLastId() +1 // 마지막 번호를 받아 +1
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        val article = Article(id, regDate, updateDate, title, body, memberId, boardId)
        val jsonStr = article.toJson() // 받은 article값을 json형식으로 변환한다

        writeStrFile("data/article/${article.id}.json",jsonStr) // 변환된 article값(jsonStr)을 해당 위치 id번 json형식으로 저장 해준다

        setLastId(id) // 마지막 번호가 겹치지 않도록 +1된 id값을 lastId으로 변경해준다
        return id //
    }

    fun makeTestArticle() {
        /*
        for (i in 1..25) {
            writeArticle(i%2 +1,i %9 +1,"제목${i}", "내용${i}")
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

    fun getArticleById(id: Int): Article? { // id값을 입력받아 id값과 일치하는 파일을 리턴해주는 함수 없을시 null리턴
        val article = articleFromFile("data/article/$id.json") // id값과 같은 저장된 파일을 Article형식으로 변환하여 article변수에 담아준다
        return article // 담긴 article 리턴 없을시 null리턴
    }

    fun remove(article: Article) { // 선택된 article값을 받아 삭제해주는 함수
        deleteFile("data/article/${article.id}.json") // deleteFile이라는 함수에 해당 article값과 같은 id를 찾아 영구삭제 해준다
    }

    fun updateArticle(id:Int,title: String, body: String) {
        val article = getArticleById(id)!!

        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()

        // 파일에서 직접 수정
        val jsonStr = article.toJson() // 변경된 value값을 json형식으로 변환

        writeStrFile("data/article/${article.id}.json",jsonStr) // 변경된 파일을 해당 id이름의 파일로 변경
    }
}