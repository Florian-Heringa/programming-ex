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
public class RTorReplyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text tagHash = new Text();

    /* Pattern for RTs and replies */
    private final static Pattern rt = Pattern.compile("w\\srt.*?(?=@)"); //("W *rt.*(?=@)");
    private final static Pattern reply = Pattern.compile("@.*");

    private StanfordCoreNLP pipeline;
    public static final String parseModelPath = "englishPCFG.ser.gz";
    public static final String sentimentModelPath = "sentiment.ser.gz";

    static enum Counters {
        HASH_TAGS
    }

    // TagSentimentMapper() {
    //     // create pipeline in default-constructor.
    //     Properties props = new Properties();
    //     props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    //     props.put("parse.model", parseModelPath);
    //     props.put("sentiment.model", sentimentModelPath);
    //     pipeline = new StanfordCoreNLP(props);
    // }

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

    private Text findType(String message) {

        // For return categorisation
        Text type = new Text();
        // regex matcher instantiations
        Matcher matcherRep;
        Matcher matcherRT;
        boolean foundRT;
        boolean foundRep;

        // check if there is a match and set the @-matcher to the index of the found pattern + 1
        matcherRT = rt.matcher(message);
        foundRT = matcherRT.find();
        if (foundRT) {
            int index = matcherRT.end();
            matcherRep = reply.matcher(message.substring(index + 1));
            //type.set(message.substring(index + 1));
        } else {
            matcherRep = reply.matcher(message);
            //type.set("none");
        }

        foundRep = matcherRep.find();
        
        if (foundRT) {
            if (foundRep) {
                type.set("Both");
            } else {
                type.set("RT-only");
            }
        } else if (foundRep) {
            type.set("Reply-only");
        } else {
            type.set("None");
        }
        return type;

    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String tweet = value.toString().toLowerCase();
        String[] lines = tweet.split("\n");
        String time = lines[0];
        String user = lines[1];
        String message = lines[2];

        Text type = findType(message);

        //Text debug = new Text(message);

        context.write(type, one);
        }
}
