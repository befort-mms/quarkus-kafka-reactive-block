import io.github.oshai.kotlinlogging.KotlinLogging
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Message

@ApplicationScoped
class ProducerExampl(
    @Channel("kafka-output")
    private val kafkaSender: MutinyEmitter<String>,
) {

    private val logger = KotlinLogging.logger {}

    fun sendMessage(input: String, topic: String) {
        logger.info("Sending message to $topic")
        val message = Message.of(input).addMetadata(
        OutgoingKafkaRecordMetadata.builder<String>()
            .withKey(input)
            .withTopic(topic).build(),
        )

        kafkaSender.sendMessage(message)
            .onFailure().invoke { failure -> logger.error(failure) { "Exception trying to send a kafka message" } }
            .onItem().invoke { _ -> logger.debug { "Successfully emitted message" } }
            .await().indefinitely()

        logger.info("Sending message to $topic completed")
    }
}