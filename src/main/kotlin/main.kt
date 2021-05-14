val articleRepository = ArticleRepository()
val memberRepository = MemberRepository()
val boardRepository = BoardRepository()
var loginMember: Member? = null
fun main() {
    val systemController = SystemController()
    val boardController = BoardController()
    val articleController = ArticleController()
    val memberController = MemberController()
    val ssgController = SsgController()
//    boardRepository.makeTestBoard()
//    memberRepository.makeTestMember()
//    articleRepository.makeTestArticle()
    println("== 게시판 프로그램 시작 ==")
    while (true) {
        val prompt = if (loginMember == null) {
            print("명령어 : ")
        } else {
            print("${loginMember!!.nickName}님 :")
        }
        val command = readLineTrim()

        val rq = Rq(command)

        when (rq.actionPath) {
            "/ssg/build" ->{
                ssgController.build(rq)
            }
            "/system/exit" -> {
                systemController.exit(rq)
                break
            }
            "/article/write" -> {
                articleController.write(rq)
            }
            "/article/list" -> {
                articleController.list(rq)
            }
            "/article/detail" -> {
                articleController.detail(rq)
            }
            "/article/delete" -> {
                articleController.delete(rq)
            }
            "/article/modify" -> {
                articleController.modify(rq)
            }
            "/member/join" -> {
                memberController.join(rq)
            }
            "/member/login" -> {
                memberController.login(rq)
            }
            "/member/logout" -> {
                memberController.logout(rq)
            }
            "/board/add" -> {
                boardController.add(rq)
            }
            "/board/list" -> {
                boardController.list(rq)
            }
            "/board/modify" -> {
                boardController.modify(rq)
            }
            "/board/delete" -> {
                boardController.delete(rq)
            }
        }
    }
    println("== 게시판 프로그램 끝 ==")
}