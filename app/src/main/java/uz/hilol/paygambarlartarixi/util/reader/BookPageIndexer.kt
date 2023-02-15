package uz.hilol.paygambarlartarixi.util.reader

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import uz.hilol.paygambarlartarixi.common.util.dpToPx
import uz.hilol.paygambarlartarixi.data.database.AppDatabase
import uz.hilol.paygambarlartarixi.data.database.model.IndexedBookPage
import uz.hilol.paygambarlartarixi.util.setHtmlText

typealias IntPair = Pair<Int, Int>

class BookPageIndexer(
    context: Context,
    private val database: AppDatabase
) {

    private var textView: TextView = TextView(context).apply {
        val params = LinearLayout.LayoutParams(
            screenWidth,
            WRAP_CONTENT
        )
        layoutParams = params
        textSize = 20f
        setLineSpacing(24f, 0f)
    }

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight by lazy {
        val height = Resources.getSystem().displayMetrics.heightPixels
        var result = 0
        val resourceId: Int = Resources.getSystem()
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId)
        }
        height - result
    }
    private val horizontalMargin by lazy {
        dpToPx(MARGIN_HORIZONTAL_DP)
    }

    private val verticalMargin by lazy {
        dpToPx(APPBAR_HEIGHT_DP + MARGIN_TOP_DP + MARGIN_BOTTOM_DP)
    }

    private val widthMeasureSpec: Int by lazy {
        View.MeasureSpec.makeMeasureSpec(
            Resources.getSystem().displayMetrics.widthPixels - horizontalMargin,
            View.MeasureSpec.AT_MOST
        )
    }

    private val heightMeasureSpec: Int by lazy {
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    }

    suspend fun startIndexer(
        contentList: List<String>,
        textSize: Float = 20f
    ) {
        val maxLinesCount = computeTextViewMaxLines(screenHeight - verticalMargin) - 2
        textView.textSize = textSize

        contentList.forEachIndexed { index, baseContent ->
            val content = BookHelper.removeHead(baseContent)
            content?.let {
                divideContentIntoPages(
                    content,
                    maxLinesCount,
                    index,
                    textSize
                )
            }
        }
    }

    private suspend fun divideContentIntoPages(
        content: String,
        maxLines: Int,
        contentIndex: Int,
        fontSize: Float
    ) {
        var startIndex: IntPair = Pair(0, 0)
        var currentIndex: IntPair = Pair(1, 0)
        val pCount = BookHelper.pLineCount(content)

        do {
            currentIndex = Pair(startIndex.first + 1, 0)
            println("current: ${currentIndex.first}")

            var finished = false
            do {
                println("insidecurrent: ${currentIndex.first}")
                val currentText = BookHelper.subContentDeep(content, startIndex, currentIndex)
                val textLines = computeHtmlTextLinesCount(currentText)

                if (textLines == maxLines) {
                    database.indexedBookDao.insertPage(
                        IndexedBookPage(
                            fontSize = fontSize,
                            contentIndex = contentIndex,
                            text = currentText
                        )
                    )

                    startIndex = currentIndex
                    break
                } else if (textLines > maxLines) {
                    currentIndex = Pair(currentIndex.first - 1, 0)
                    var preText = if (currentIndex.first == startIndex.first)
                        ""
                    else
                        BookHelper.subContentDeep(
                            content,
                            startIndex,
                            Pair(currentIndex.first, 0)
                        ) + "\n"

                    val currentPLine = BookHelper.pLineAt(content, currentIndex.first)
                    val currentPLineContent = BookHelper.showPLineContent(currentPLine)

                    val currentPLineContentPrefix = currentPLineContent.prefix
                    val currentPLineContentSuffix = currentPLineContent.suffix
                    val currentPLineContentTextList = currentPLineContent.data.split(" ")

                    val startIndexInLine = if (currentIndex.first == startIndex.first)
                        startIndex.second
                    else
                        0
                    var indexInLine = startIndexInLine + 1

                    //Compute deep PLineText
                    var currentPLineContentSubtextPreText = ""
                    do {
                        val currentPLineContentSubtext = StringBuilder()
                            .append(currentPLineContentPrefix)
                            .append(
                                currentPLineContentTextList.subList(startIndexInLine, indexInLine)
                                    .reduce { acc, s -> "$acc $s" })
                            .append(currentPLineContentSuffix)
                            .toString()

                        val textForCheck = StringBuilder()
                            .append(preText)
                            .append(currentPLineContentSubtext)
                            .toString()

                        if (computeHtmlTextLinesCount(textForCheck) > maxLines) {
                            indexInLine--
                            finished = true
                            val resultIndex = Pair(currentIndex.first, indexInLine)
                            database.indexedBookDao.insertPage(
                                IndexedBookPage(
                                    fontSize = fontSize,
                                    contentIndex = contentIndex,
                                    text = currentPLineContentSubtextPreText
                                )
                            )
                            startIndex = resultIndex
                            break
                        } else {
                            indexInLine++
                        }
                        currentPLineContentSubtextPreText = textForCheck
                    } while (true)

                } else {
                    currentIndex = Pair(currentIndex.first + 1, 0)
                }

            } while (!finished && currentIndex.first <= pCount)

        } while (currentIndex.first < pCount)

        if (startIndex.first != pCount) {
            val text = BookHelper.subContentDeep(
                content,
                startIndex,
                Pair(currentIndex.first - 1, 0)
            )
            database.indexedBookDao.insertPage(
                IndexedBookPage(
                    fontSize = fontSize,
                    contentIndex = contentIndex,
                    text = text
                )
            )
        }
    }

    private fun computeHtmlTextHeight(text: String): Int {
        textView.setHtmlText(text)
        textView.measure(widthMeasureSpec, heightMeasureSpec)
        return textView.measuredHeight
    }

    private fun computeTextHeight(text: String): Int {
        textView.text = text
        textView.measure(widthMeasureSpec, heightMeasureSpec)
        return textView.measuredHeight
    }

    private fun computeHtmlTextLinesCount(text: String): Int {
        textView.setHtmlText(text)
        textView.measure(widthMeasureSpec, heightMeasureSpec)
        return textView.lineCount
    }

    private fun computeTextViewMaxLines(componentMaxHeight: Int): Int {
        val stringBuilder = StringBuilder("")
        var lineCount = 0

        while (componentMaxHeight > computeTextHeight(stringBuilder.toString())) {
            stringBuilder.appendLine("\\n")
            lineCount++
        }
        return lineCount
    }

    companion object {
        const val APPBAR_HEIGHT_DP = 56
        const val MARGIN_TOP_DP = 12
        const val MARGIN_BOTTOM_DP = 48
        const val MARGIN_HORIZONTAL_DP = 12
    }
}
