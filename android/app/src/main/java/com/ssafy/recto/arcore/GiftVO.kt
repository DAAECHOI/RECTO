package com.ssafy.recto.arcore

import lombok.NoArgsConstructor

@NoArgsConstructor
data class GiftVO(
        var gift_seq: Int = 0,
        var gift_from: String? = "",
        var photo_seq: Int? = 0,
        var gift_to: String? = ""
){

    constructor(gift_from: String?, photo_seq: Int?, gift_to: String?) : this() {
        this.gift_from = gift_from
        this.photo_seq = photo_seq
        this.gift_to = gift_to
    }

}