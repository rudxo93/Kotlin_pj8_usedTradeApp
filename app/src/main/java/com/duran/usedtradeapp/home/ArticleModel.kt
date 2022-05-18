package com.duran.usedtradeapp.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
) {
    // Firebase에 클래스 단위로 올리려면 빈 생성자가 필요하다.
    constructor() : this("", "", 0, "", "")
}