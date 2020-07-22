package com.github.saiprasadkrishnamurthy.aes

import edu.stanford.nlp.tagger.maxent.MaxentTagger
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.WhitespaceTokenizer
import java.io.FileInputStream


object Foo {
    @JvmStatic
    fun main(args: Array<String>) {
        val modelStream = FileInputStream("en-pos-maxent.bin")
        val model = POSModel(modelStream)
        val tagger = POSTaggerME(model)
        val text = "Sai lives in chennai. Delhi is where sai lives - He has a home in Chennai too."
        if (model != null) {
            val tagger = POSTaggerME(model)
            if (tagger != null) {
                pps(tagger, text)
//                pps(tagger, "Delhi is where sai lives. Kumar lives in Chennai")
            }
        }
        println(spps(text))

    }

    private fun spps(text: String) {
        val maxentTagger = MaxentTagger("english-left3words-distsim.tagger")
        val tag = maxentTagger.tagString(text)
        val eachTag = tag.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println("Word      " + "Standford tag")
        println("----------------------------------")
        for (i in eachTag.indices) {
            println(eachTag[i].split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "           " + eachTag[i].split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
        }
    }


    private fun pps(tagger: POSTaggerME, text: String) {
        val whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                .tokenize(text)
        val tags = tagger.tag(whitespaceTokenizerLine)
        for (i in whitespaceTokenizerLine.indices) {
            val word = whitespaceTokenizerLine[i].trim { it <= ' ' }
            val tag = tags[i].trim { it <= ' ' }
            print("$tag:$word  ")
        }
        println("\n-----------------------------------------------------------")
    }
}