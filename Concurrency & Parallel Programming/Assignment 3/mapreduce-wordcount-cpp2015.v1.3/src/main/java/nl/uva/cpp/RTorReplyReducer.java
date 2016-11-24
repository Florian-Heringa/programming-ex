package nl.uva.cpp;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class RTorReplyReducer extends Reducer<Text, IntWritable, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
        // calculate mean sentiment and standard dev for each hashtag.
        int count = 0;

        for (IntWritable val : values) {
            count++;
        }

        Text output = new Text(Integer.toString(count));

        context.write(key, output);
    }
}
