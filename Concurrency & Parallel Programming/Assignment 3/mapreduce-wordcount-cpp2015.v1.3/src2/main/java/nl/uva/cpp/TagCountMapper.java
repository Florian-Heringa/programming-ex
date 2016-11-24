package nl.uva.cpp;

import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class TagCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text tagHash = new Text();

    /* Every hashtag consists of a hash character in front of an unspaced phrase
    which in turn consists of one or more word characters ([a-zA-Z_0-9]). */
    private final static Pattern tagPattern = Pattern.compile("#\\w+");

    static enum Counters {
        HASH_TAGS
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String tweet = value.toString().toLowerCase();
        String[] lines = tweet.split("\n");
        String time = lines[0];
        String user = lines[1];
        String message = lines[2];

        Matcher matcher = tagPattern.matcher(message);
        while (matcher.find()) {
            tagHash.set(matcher.group());
            context.write(tagHash, one);

            context.getCounter(Counters.HASH_TAGS).increment(1);
        }

        /*
        for (String word : message.split(" ")) {
            if (word.startsWith("#")) {
                // write (hashtag, 1) as (key, value) in output
                tagHash.set(word);
                context.write(tagHash, one);

                // Increment a counter.
                context.getCounter(Counters.HASH_TAGS).increment(1);
            }
        }*/
    }
} 
