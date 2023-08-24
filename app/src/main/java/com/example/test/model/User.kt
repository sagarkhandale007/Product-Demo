package com.example.test.model

//data class User(val name: String, val email: String)


//data class User(val image: String ?=null, val price: Double ?=null, val rating: HashMap<String,Double> , val description: String ?=null, val id: Double ?=null,val category: String ?=null,val title: String ?=null)

data class User(
    val category: Any,
    val title: Any,
    val image: Any,
    val price: Any,
    val description: Any,
    val rate: Double,
    val count: Int
)
