package br.com.ecommerce;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());
        var value = "121354654, 685315, 154445463515";
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value); // topic, key, value
        Callback callback = (data, ex) -> { // mensagem enviada >>> registro
            if(ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Successfully sending " + data.topic() + "::: partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        var email = "Thank you for your order! We are processing your order";
        var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);
        producer.send(record, callback).get();
        producer.send(emailRecord, callback).get();
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092"); // onde se conectar (chave, servidores/porta)
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // transformadores de string p/ bytes
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // value >>> mensagem
        return properties;
    }
}