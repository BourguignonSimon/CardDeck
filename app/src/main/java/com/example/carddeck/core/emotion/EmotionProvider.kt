package com.example.carddeck.core.emotion

import com.example.carddeck.domain.model.EmotionCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmotionProvider @Inject constructor() {

    private val cards = listOf(
        EmotionCard("joy", "Joy", "Feeling of happiness and delight", "Joy"),
        EmotionCard("gratitude", "Gratitude", "Appreciation for something received", "Joy"),
        EmotionCard("calm", "Calm", "Relaxed and centered state", "Calm"),
        EmotionCard("hope", "Hope", "Optimistic expectation for the future", "Calm"),
        EmotionCard("love", "Love", "Warmth and affection toward others", "Joy"),
        EmotionCard("sadness", "Sadness", "Feeling blue or down", "Sadness"),
        EmotionCard("anger", "Anger", "Frustration or irritation", "Anger"),
        EmotionCard("fear", "Fear", "Sense of worry or threat", "Fear"),
        EmotionCard("surprise", "Surprise", "Unexpected reaction", "Surprise"),
        EmotionCard("confusion", "Confusion", "Lack of clarity or certainty", "Fear"),
        EmotionCard("pride", "Pride", "Sense of accomplishment", "Joy"),
        EmotionCard("curiosity", "Curiosity", "Desire to explore or learn", "Calm")
    )

    fun getEmotionCards(): List<EmotionCard> = cards
}
