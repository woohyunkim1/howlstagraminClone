package dto

class ContentDTO(
    var explain : String? = null,//설명관리
    var imageUrl : String? = null,//url 저장
    var uid : String ? = null,// 어던 유저가 올렸는지 관리
    var userId: String? = null,//유저의 아이디
    var timestamp : Long ? = null,//업로드 시간 관리
    var favoriteCount : Int = 0,//좋아요 눌린 수 count
    var favorites : Map<String, Boolean> = HashMap()// 누가 좋아요를 눌렀는지
){
    data class Comment(//나중에 댓글을 남겼을 때 데이터 관리를 위해
        var uid: String ?= null,//누가 댓글을 남렸는지
        var userId : String ?= null,//댓글을 남긴 유저의 아이디
        var comment: String ?= null,//뭐라고 남겼는지
        var timestamp: Long?= null//몃 시 몇 분에 올렸는지
    )
}