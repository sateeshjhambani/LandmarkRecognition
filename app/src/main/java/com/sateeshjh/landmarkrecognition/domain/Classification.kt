package com.sateeshjh.landmarkrecognition.domain

// this represents the output of the AI model
data class Classification(
    val name: String,
    val score: Float
)