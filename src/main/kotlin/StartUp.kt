
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ApplicationScoped
class EagerAppBean {

    @Inject
    lateinit var producer: ProducerExampl

    @Startup
    fun init() {
        GlobalScope.launch {
            producer.sendMessage("input", "not-existent-topic")
        }
        GlobalScope.launch {
            producer.sendMessage("input", "not-existent-topic")
        }
        GlobalScope.launch {
            producer.sendMessage("input", "not-existent-topic")
        }

        GlobalScope.launch {
            producer.sendMessage("input", "existent-topic")
        }
    }
}