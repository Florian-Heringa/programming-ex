package nl.uva.cpp;

import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import me.champeau.ld.UberLanguageDetector;

import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

// TODO: remove first two chars of time, user and message, then hashtag beginning line or space
public class TagSentimentMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text tagHash = new Text();

    /* Every hashtag consists of a hash character in front of an unspaced phrase
    which in turn consists of one or more word characters ([a-zA-Z_0-9]). */
    private final static Pattern tagPattern = Pattern.compile("#\\w+");

    private StanfordCoreNLP pipeline;
    public static final String parseModelPath = "englishPCFG.ser.gz";
    public static final String sentimentModelPath = "sentiment.ser.gz";

    static enum Counters {
        HASH_TAGS
    }

    TagSentimentMapper() {
        // create pipeline in default-constructor.
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        props.put("parse.model", parseModelPath);
        props.put("sentiment.model", sentimentModelPath);
        pipeline = new StanfordCoreNLP(props);
    }

    private int findSentiment(String text) {
        int mainSentiment = 0;

        if (text != null && text.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(text);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                // ’AnnotatedTree’ is ’SentimentAnnotatedTree’ in newer versions
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString(); 
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
        }
        return mainSentiment;
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String tweet = value.toString().toLowerCase();
        String[] lines = tweet.split("\n");
        String time = lines[0];
        String user = lines[1];
        String message = lines[2];

        Matcher matcher = tagPattern.matcher(message);
        if (matcher.find()) {

            // detect the language of the tweet only once and if english,
            String lang = UberLanguageDetector.getInstance().detectLang(message);
            if (lang == "en") {

                //  perform sentiment analysis.
                IntWritable tweetSentiment = new IntWritable(findSentiment(message));
                matcher.reset();
                while (matcher.find()) {
                    // for each hashtag, emit (hashtag, sentiment) to the reducers.
                    tagHash.set(matcher.group());
                    context.write(tagHash, tweetSentiment);

                    context.getCounter(Counters.HASH_TAGS).increment(1);
                }
            }
        }
    }
} 
