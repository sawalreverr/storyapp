package com.example.storyappdicoding

import com.example.storyappdicoding.core.data.response.stories.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "author + $i",
                "quote $i",
            )
            items.add(story)
        }
        return items
    }
}