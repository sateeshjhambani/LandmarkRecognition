# LandmarkRecognition

A TensorFlow and CameraX-based app that can scan popular landmarks in real-time in the CameraPreview, and use a recognition model to analyze, identify and classify the results.

The app uses the model trained on African landmarks, but other models could be used in the same project to identify other landmarks worldwide.

## Usage

The abstraction that is responsible for using the TensorFlow classifier to identify the provided image and output the list of classifications (this is a representation of the result, containing the name of the landmark and accuracy score.)

```kotlin
interface LandmarkClassifier {

    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}
```

Initialize the TensorFlow ImageClassifier instance, provide the model's path, set the required no of results and the threshold of identification accuracy.

```kotlin
private fun setupClassifier() {
    val baseOptions = BaseOptions.builder()
        .setNumThreads(2)
        .build()

    val options = ImageClassifier.ImageClassifierOptions.builder()
        .setBaseOptions(baseOptions)
        .setMaxResults(maxResults)
        .setScoreThreshold(threshold)
        .build()

    try {
        classfier = ImageClassifier.createFromFileAndOptions(
            context,
            "landmarks.tflite",
            options
        )
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }
}
```

Set up the TensorFlow classifier instance (if not done already), convert the input bitmap image to TensorImage, and get the results, to make sure there are no repetitions we only take results with distinct landmark names.

```kotlin
override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
    if (classfier == null) {
        setupClassifier()
    }

    val imageProcessor = ImageProcessor.Builder().build()
    val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

    val imageProcessingOption = ImageProcessingOptions.builder()
        .setOrientation(getOrientationFromRotation(rotation))
        .build()

    val results = classfier?.classify(tensorImage, imageProcessingOption)

    return results?.flatMap { classifications ->
        classifications.categories.map { category ->
            Classification(
                name = category.displayName,
                score = category.score
            )
        }
    }?.distinctBy { it.name } ?: emptyList()
}
```

## References

[Landmark Recognition Model](https://www.kaggle.com/models/google/landmarks)
[CameraX](https://developer.android.com/media/camera/camerax)
