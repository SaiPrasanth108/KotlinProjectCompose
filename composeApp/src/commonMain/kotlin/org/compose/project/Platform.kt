package org.compose.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform