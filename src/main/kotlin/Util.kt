import java.io.File
import java.text.SimpleDateFormat
fun mapFromJson(jsonStr: String): Map<String, Any> { // 저장한 파일을 map으로 변환하는 함수
    val map = mutableMapOf<String, Any>()
    // ex) map<String -> key값 , Any -> value값> value값은 모든 자료형이 올수있도록 Any로 셋팅
    var jsonStr = jsonStr.drop(1) // 앞쪽 "{" 문장을 제거한 뒤 저장
    jsonStr = jsonStr.dropLast(1) // 뒤쪽 "}" 문장을 제거한 뒤 저장

    val jsonItems = jsonStr.split(",\r\n") // jsonStr를 \r\n을 기준으로 나눠준다

    for (jsonItem in jsonItems) { // 나눠진 jsonItem을 반복문으로 차례차례 비교
        val jsonItemBits = jsonItem.trim().split(":", limit = 2) // 비교후 :를 기준으로 나눠준다 limit으로 2개
        // key값과 value 값으로 나눠준다
        val key = jsonItemBits[0].trim().drop(1).dropLast(1) // key값의 앞(")과 마지막(")을 제거 해준다
        // ex) "id" -> id
        var value = jsonItemBits[1].trim()
        // ex) value = 1
        when { // when을 통하여
            value == "true" -> { // value값이 boolean 타입인지 확인
                map[key] = true  // value값이 true면 true 적용
            }
            value == "false" -> { // value값이 boolean 타입인지 확인
                map[key] = false // value값이 false면 false 적용
            }
            value.startsWith("\"") -> { // value 값이 "로(String) 시작하는지 확인
                map[key] = value.drop(1).dropLast(1) // value 값이 String이면 앞(")과 마지막(")을 제거 해준다
            }
            value.contains(".") -> { // value 값이 .(doble)이 포함하고 있는지 확인
                map[key] = value.toDouble() // value 값을 doble로 변경후 적용
            }
            else -> { // 나머지 value 값이 int인지 확인
                map[key] = value.toInt() // value 값을 int로 변경후 적용
            }
        }
    }

    return map.toMap()
}

fun readStrFromFile(filePath: String): String { // filePath안에 파일을 읽는다
    if (!File(filePath).isFile){ // 파일 유무 확인
        return "" // 없다면 ""리턴
    }
    return File(filePath).readText(Charsets.UTF_8) // filePath파일을 읽은값을 리턴
}

fun writeStrFile(filePath: String, fileContent: String) { // 파일저장
    File(filePath).parentFile.mkdirs() // 해당 경로에 폴더 생성후 저장
    File(filePath).writeText(fileContent) // filePath변수에는 저장할 파일의 위치가 이름이 들있음 fileContent변수에는 해당 파일의 내용이 들어있음

}

fun readIntFromFile(filePath: String, default : Int): Int { // 숫자 파일을 읽기 위해 사용
    val fileContent = readStrFromFile(filePath)

    if( fileContent == ""){
        return default
    }

    return fileContent.toInt()
}

fun writeIntFile(filePath: String, fileContent: Int) { // 숫자 파일을 저장하기 위해 사용
    writeStrFile(filePath, fileContent.toString())
}

fun deleteFile(filePath: String) { // 저장된 파일을 삭제
    File(filePath).delete() // 파일의 객체를 받아 .delete()를 사용하여 저장된 파일을 삭제한다
     
}

fun readLineTrim() = readLine()!!.trim()
object Util {

    fun getNowDateStr(): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return format.format(System.currentTimeMillis())
    }
}