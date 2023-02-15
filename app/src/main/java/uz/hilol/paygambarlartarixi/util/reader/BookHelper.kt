package uz.hilol.paygambarlartarixi.util.reader

object BookHelper {

    fun removeHead(text: String): String? {
        if (text.contains("<p".toRegex()).not()) return null
        val firstIndex = "<p ".toRegex().findAll(text).map { it.range.first }.first()
        val lastIndex = "</body>".toRegex()
            .findAll(text)
            .map { it.range.first }
            .first() - DIFFERENCES_IN_LAST_CHARACTER
        return text
            .substring(firstIndex..lastIndex)
            .lineSequence()
            .map { it.trim() }
            .joinToString("\n")
    }

    fun pLineAt(text: String, index: Int): String {
        if (index >= pLineCount(text)) throw IndexOutOfBoundsException("pContentAt function invalid index")
        val lastPIndex = index + 1
        return subContent(text, index, lastPIndex)
    }

    private fun subContent(text: String, from: Int, to: Int): String {
        if (from == to) return ""

        val firstPIndex =
            if (from == 0) 0
            else "<p ".toRegex().findAll(text).map { it.range.first }.elementAt(from)


        val lastPIndex =
            if (to == pLineCount(text)) text.length
            else "<p ".toRegex().findAll(text).map { it.range.first }.elementAt(to) - 1

        return text.substring(firstPIndex until lastPIndex)
    }

    fun subContentDeep(text: String, from: IntPair, to: IntPair): String {
        if (from.first > to.first) throw IndexOutOfBoundsException("subContentDeep function invalid index")
        if (from == to) return ""

        if (from.first == to.first) {
            val pLine = pLineAt(text, from.first)
            return pLineSubText(pLine, from.second, to.second)
        }

        val forFirstPLine =
            if (from.second == 0)
                pLineAt(text, from.first)
            else
                pLineSubText(pLineAt(text, from.first), from.second)

        val forLastPLine =
            if (to.second == 0)
                ""
            else
                pLineSubText(pLineAt(text, to.first), 0, to.second)

        return if (to.first - from.first == 1)
            StringBuilder().apply {
                append(forFirstPLine)
                if (forLastPLine.isNotEmpty()) {
                    append("\n")
                    append(forLastPLine)
                }
            }.toString()
        else {
            StringBuilder()
                .append(forFirstPLine)
                .append("\n")
                .append(subContent(text, from.first + 1, to.first))
                .apply {
                    if (forLastPLine.isNotEmpty()) {
                        append("\n")
                        append(forLastPLine)
                    }
                }
                .toString()
        }
    }

    fun pLineSubText(text: String, from: Int, to: Int = -1): String {
        val textContent = showPLineContent(text)
        val textContentList = textContent.data.split(" ")
        val listSize = textContentList.size

        val resultList = textContentList.subList(from, if (to == -1) listSize else to)
        val result = resultList.joinToString(" ")
        return StringBuilder()
            .append(textContent.prefix)
            .append(result)
            .append(textContent.suffix)
            .toString()
    }

    fun pLineCount(text: String): Int {
        return "<p ".toRegex().findAll(text).map { it.range.first }.count()
    }


    fun showPLineContent(text: String): PLineContent {
        var resultText = text
        var startIndex = 0
        var lastIndex = 0

        var closeBracket = false
        var closeBracketIndex = 0

        var finishLoop = false
        text.forEachIndexed { index, c ->
            if (finishLoop) return@forEachIndexed

            if (c == '<' && text[index + 1] == '/') {
                startIndex = closeBracketIndex
                lastIndex = index

                resultText = text.substring(startIndex + 1 until lastIndex)
                finishLoop = true
            } else if (c == '>') {
                closeBracket = true
                closeBracketIndex = index
            }
        }

        val prefix = text.substring(0..startIndex)
        val suffix = text.substring(lastIndex until text.count())
        return PLineContent(data = resultText, prefix = prefix, suffix = suffix)
    }


    data class PLineContent(
        val data: String,
        val prefix: String,
        val suffix: String
    )

    private const val DIFFERENCES_IN_LAST_CHARACTER = 2
}