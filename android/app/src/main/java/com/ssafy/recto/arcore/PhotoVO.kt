package com.ssafy.recto.arcore

import lombok.NoArgsConstructor

@NoArgsConstructor
data class PhotoVO (
        var photo_seq : Int = 0,
        var user_uid : String = "",
        var photo_id : String = "",
        var photo_date : String = "",
        var photo_url : String = "",
        var video_url : String = "",
        var phrase : String = "",
        var photo_pwd : String = "",
        var design : Int = 0
)