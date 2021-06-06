package com.test.flickrapp.data

data class Photo (
    var id: String,
    var owner: Owner,
    var title: Title,
    var description: Description,
    var dates: Dates
        )