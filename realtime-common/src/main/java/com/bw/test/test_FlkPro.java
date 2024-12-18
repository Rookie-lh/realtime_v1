package com.bw.test;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class test_FlkPro {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        

        DataStreamSource<String> streamSource = env.socketTextStream("hadoop102", 999);

        streamSource.print();

        env.execute();


    }
}
