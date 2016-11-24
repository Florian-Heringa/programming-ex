package nl.uva.cpp;

import java.io.IOException;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class TagSentimentReducer extends Reducer<Text, IntWritable, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
        // calculate mean sentiment and standard dev for each hashtag.
        double sum = 0, sq_sum = 0, mean, variance, sd;
        int count = 0;

        for (IntWritable val : values) {
            sum += val.get();
            sq_sum += val.get() * val.get();
            count++;
        }
        mean = sum / count;
        variance = sq_sum / count - mean * mean;
        sd = Math.sqrt(variance);

        /* cache iterator and
        for (IntWritable val : values) {
            sd += Math.pow((double)val.get() - mean, 2);
        }
        sd /= count;
        sd = Math.sqrt(sd);*/
        
        context.write(key, new Text("mean: " + mean + ", standard deviation: " + sd));
    }
}
