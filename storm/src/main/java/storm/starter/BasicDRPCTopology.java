package storm.starter;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * This topology is a basic example of doing distributed RPC on top of Storm. It implements a function that appends a
 * "!" to any string you send the DRPC function.
 * <p/>
 * See https://github.com/nathanmarz/storm/wiki/Distributed-RPC for more information on doing distributed RPC on top of
 * Storm.
 */
public class BasicDRPCTopology {
    public static class ExclaimBolt extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            String input = tuple.getString(1);
            collector.emit(new Values(tuple.getValue(0), input + "!"));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));
        }

    }

    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("exclamation");
        builder.addBolt(new ExclaimBolt(), 3);

        Config conf = new Config();

        if (args == null || args.length == 0) {
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();

            cluster.submitTopology("drpc-demo", conf, builder.createLocalTopology(drpc));

            for (String word : new String[]{"hello", "goodbye"}) {
                System.out.println("Result for \"" + word + "\": " + drpc.execute("exclamation", word));
            }

            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
